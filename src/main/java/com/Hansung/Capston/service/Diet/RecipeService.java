package com.Hansung.Capston.service.Diet;

import com.Hansung.Capston.common.DayOfWeek;
import com.Hansung.Capston.common.DietType;
import com.Hansung.Capston.dto.Diet.Ingredient.PreferredIngredientResponse;
import com.Hansung.Capston.dto.Diet.Ingredient.RecommendedIngredientResponse;
import com.Hansung.Capston.dto.Diet.Nutrition.NutritionLogResponse;
import com.Hansung.Capston.dto.Diet.Nutrition.RecommendedNutrientResponse;
import com.Hansung.Capston.dto.Diet.Recipe.RecommendRecipeResponse;
import com.Hansung.Capston.entity.Diet.Food.FoodData;
import com.Hansung.Capston.entity.Diet.Nutrient.NutritionLog;
import com.Hansung.Capston.entity.Diet.Recipe.RecipeData;
import com.Hansung.Capston.entity.Diet.Recipe.RecommendedRecipe;
import com.Hansung.Capston.entity.UserInfo.User;
import com.Hansung.Capston.repository.Diet.Food.FoodDataRepository;
import com.Hansung.Capston.repository.Diet.Recipe.RecipeDataRepository;
import com.Hansung.Capston.repository.Diet.Recipe.RecommendedRecipeRepository;
import com.Hansung.Capston.repository.UserInfo.UserRepository;
import com.Hansung.Capston.service.ApiService.OpenAiApiService;
import com.opencsv.bean.CsvToBeanBuilder;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Service
@Transactional
@Slf4j
public class RecipeService {
  private final RecipeDataRepository recipeDataRepository;
  private final FoodDataRepository foodDataRepository;
  private final RecommendedRecipeRepository recommendedRecipeRepository;
  private final UserRepository userRepository;

  private final IngredientService ingredientService;
  private final OpenAiApiService openAiApiService;
  private final NutrientService nutrientService;

  public RecipeService(RecipeDataRepository recipeDataRepository,
      FoodDataRepository foodDataRepository,
                       RecommendedRecipeRepository recommendedRecipeRepository, IngredientService ingredientService, OpenAiApiService openAiApiService, UserRepository userRepository,
      NutrientService nutrientService) {
    this.recipeDataRepository = recipeDataRepository;
    this.foodDataRepository = foodDataRepository;
    this.recommendedRecipeRepository = recommendedRecipeRepository;
      this.ingredientService = ingredientService;
      this.openAiApiService = openAiApiService;
    this.userRepository = userRepository;
    this.nutrientService = nutrientService;
  }

  // 레시피 데이터 불러오기
  public List<RecipeData> loadRecipeData() {
    return recipeDataRepository.findAll();
  }

  // 연관 레시피 설정하기
  public void saveRelatedRecipeData() {
    List<RecipeData> allRecipes = recipeDataRepository.findAll();
    int topN = 3; // 연관 레시피 개수 설정

    Map<RecipeData, List<RecipeData>> relatedRecipesMap = findRelatedRecipes(allRecipes, topN);

    for (Map.Entry<RecipeData, List<RecipeData>> entry : relatedRecipesMap.entrySet()) {
      RecipeData baseRecipe = entry.getKey();
      List<RecipeData> relatedRecipes = entry.getValue();

      // 연관 레시피 ID들을 해당 필드에 저장
      if (!relatedRecipes.isEmpty()) {
        baseRecipe.setRelatedRecipe1(String.valueOf(relatedRecipes.get(0).getRecipeId()));
      }
      if (relatedRecipes.size() > 1) {
        baseRecipe.setRelatedRecipe2(String.valueOf(relatedRecipes.get(1).getRecipeId()));
      }
      if (relatedRecipes.size() > 2) {
        baseRecipe.setRelatedRecipe3(String.valueOf(relatedRecipes.get(2).getRecipeId()));
      }
      recipeDataRepository.save(baseRecipe);
    }
  }

  // 추천 식단 불러오기
  public List<RecommendRecipeResponse> loadRecommendRecipe(String userId) {
    List<RecommendedRecipe> recommendedRecipes = recommendedRecipeRepository.findByUser_UserId(userId);
    List<RecommendRecipeResponse> recommendedRecipeResponses = new ArrayList<>();
    for (RecommendedRecipe recommendedRecipe : recommendedRecipes) {
      recommendedRecipeResponses.add(RecommendRecipeResponse.toDto(recommendedRecipe));
    }

    return recommendedRecipeResponses;
  }

  // 추천 식단 설정하기
  public void setRecommendRecipe(String userId) {
    List<RecommendedIngredientResponse> recList = ingredientService.loadRecommendedIngredient(userId);
    List<PreferredIngredientResponse> prefList = ingredientService.loadPreferredIngredients(userId);

    List<String> allIngredients = new ArrayList<>();
    recList.forEach(r -> allIngredients.add(r.getIngredient().getIngredientName()));
    prefList.forEach(p -> allIngredients.add(p.getIngredient().getIngredientName()));

    Set<RecipeData> matchingRecipesSet = new HashSet<>();
    for (String ingredient : allIngredients) {
      matchingRecipesSet.addAll(recipeDataRepository.findByIngredient(ingredient));
    }

    List<RecipeData> finalRecipeCandidates = new ArrayList<>();
    int targetRecipeCount = 50; // LLM에 전달하고 싶은 총 레시피 개수 목표

    // 1. 재료 기반 레시피 추가 (우선 순위)
    if (!matchingRecipesSet.isEmpty()) {
      finalRecipeCandidates.addAll(matchingRecipesSet);
      log.info("✅ 재료 기반으로 {}개의 레시피가 매칭되었습니다.", finalRecipeCandidates.size());
    } else {
      log.info("ℹ️ 재료 기반으로 매칭되는 레시피가 없습니다.");
    }

    // 2. 목표 개수(targetRecipeCount)에 미달하면 영양소 기반 레시피 추가
    if (finalRecipeCandidates.size() < targetRecipeCount) {
      log.info("ℹ️ 레시피 후보가 부족하여 영양소 기반 레시피를 추가합니다. 현재 {}개, 목표 {}개.", finalRecipeCandidates.size(), targetRecipeCount);

      List<RecommendedNutrientResponse> nutritionLog = nutrientService.loadRecommendedNutrients(userId);

      if (nutritionLog.isEmpty()) {
        log.warn("🚨 영양소 로그가 비어있어 추가 레시피 추천이 불가능합니다. userId: {}", userId);
      } else {
        Double recCalories = (nutritionLog.getFirst().getCalories()+nutritionLog.getLast().getCalories())/2;
        Double recCarbohydrates = (nutritionLog.getFirst().getCarbohydrate()+nutritionLog.getLast().getCarbohydrate())/2;
        Double recProtein = (nutritionLog.getFirst().getProtein()+nutritionLog.getLast().getProtein())/2;
        Double recFat = (nutritionLog.getFirst().getFat()+nutritionLog.getLast().getFat())/2;

        List<RecipeData> allRecipes = recipeDataRepository.findAll();
        List<RecipeScore> scoredRecipes = new ArrayList<>();

        double calorieRange = recCalories > 0 ? recCalories : 1.0;
        double carbohydrateRange = recCarbohydrates > 0 ? recCarbohydrates : 1.0;
        double proteinRange = recProtein > 0 ? recProtein : 1.0;
        double fatRange = recFat > 0 ? recFat : 1.0;

        for (RecipeData recipe : allRecipes) {
          if (finalRecipeCandidates.contains(recipe)) {
            continue;
          }

          double recipeCalories = recipe.getCalories() != null ? recipe.getCalories() : 0.0;
          double recipeCarbohydrate = recipe.getCarbohydrate() != null ? recipe.getCarbohydrate() : 0.0;
          double recipeProtein = recipe.getProtein() != null ? recipe.getProtein() : 0.0;
          double recipeFat = recipe.getFat() != null ? recipe.getFat() : 0.0;

          double score = 0.0;
          score += Math.abs((recipeCalories - recCalories) / calorieRange);
          score += Math.abs((recipeCarbohydrate - recCarbohydrates) / carbohydrateRange);
          score += Math.abs((recipeProtein - recProtein) / proteinRange);
          score += Math.abs((recipeFat - recFat) / fatRange);

          scoredRecipes.add(new RecipeScore(recipe, score));
        }

        // 점수가 낮은 순서대로 정렬
        scoredRecipes.sort(Comparator.comparingDouble(RecipeScore::getScore));

        // 부족한 개수만큼 영양소 기반 레시피를 추가
        Random random = new Random();
        int recipesToAdd = targetRecipeCount - finalRecipeCandidates.size();

        List<RecipeData> topScoredNutrientRecipes = scoredRecipes.stream()
            .map(RecipeScore::getRecipe)
            .collect(Collectors.toList());

        // 상위 N개(넉넉하게) 중에서 랜덤하게 뽑기
        int nutrientCandidatePoolSize = Math.min(topScoredNutrientRecipes.size(), 200); // 영양소 기반으로 뽑을 후보 풀 (예: 상위 200개)
        List<RecipeData> nutrientCandidatesPool = new ArrayList<>(topScoredNutrientRecipes.subList(0, nutrientCandidatePoolSize));

        while (finalRecipeCandidates.size() < targetRecipeCount && !nutrientCandidatesPool.isEmpty()) {
          int randomIndex = random.nextInt(nutrientCandidatesPool.size());
          RecipeData recipeToAdd = nutrientCandidatesPool.remove(randomIndex);
          // 최종 후보에 이미 없는 경우에만 추가 (혹시 모를 중복 방지)
          if (!finalRecipeCandidates.contains(recipeToAdd)) {
            finalRecipeCandidates.add(recipeToAdd);
          }
        }
        log.info("✅ 영양소 기반으로 {}개의 레시피를 추가했습니다. 최종 후보: {}개.", recipesToAdd, finalRecipeCandidates.size());
      }
    } else if (finalRecipeCandidates.size() > targetRecipeCount) {
      // 재료 기반 레시피가 targetRecipeCount를 초과할 경우, 랜덤 샘플링
      log.info("ℹ️ 재료 기반 레시피가 목표 개수({})를 초과합니다. 랜덤 샘플링을 수행합니다. 현재 {}개.", targetRecipeCount, finalRecipeCandidates.size());
      List<RecipeData> tempCandidates = new ArrayList<>();
      Random random = new Random();
      while (tempCandidates.size() < targetRecipeCount && !finalRecipeCandidates.isEmpty()) {
        int randomIndex = random.nextInt(finalRecipeCandidates.size());
        tempCandidates.add(finalRecipeCandidates.remove(randomIndex));
      }
      finalRecipeCandidates = tempCandidates;
      log.info("✅ 재료 기반 레시피에서 {}개로 랜덤 샘플링 완료. 최종 후보: {}개.", targetRecipeCount, finalRecipeCandidates.size());
    }


    // 레시피 이름 추출 (LLM 활용 위해서)
    List<String> recipeNames = finalRecipeCandidates.stream()
        .map(RecipeData::getRecipeName)
        .collect(Collectors.toList());
    log.info("✅ LLM에 전달할 레시피 후보 크기: {}", recipeNames.size());

    // ... (이하 기존 LLM 호출 및 RecommendedRecipe 저장 로직 동일) ...
    String recommendRecipes = openAiApiService.getRecommendedRecipes(recipeNames);
    String[] weeklyRecipes = recommendRecipes.split("-"); // 요일 구분

    User user = userRepository.findByUserId(userId)
        .orElseThrow(() -> new NoSuchElementException("User not found with userId: " + userId));

    for (int i = 0; i < 7; i++) {
      DayOfWeek currentDay = DayOfWeek.values()[i];

      if (i >= weeklyRecipes.length) {
        log.warn("❌ LLM 응답이 7일치를 모두 포함하지 않습니다. ({}일치만 있음)", weeklyRecipes.length);
        break;
      }

      String[] meals = weeklyRecipes[i].split("\\|");
      for (int j = 0; j < meals.length; j++) {
        DietType currentType = switch (j) {
          case 0 -> DietType.breakfast;
          case 1 -> DietType.lunch;
          case 2 -> DietType.dinner;
          default -> throw new IllegalStateException("Unexpected meal type index: " + j);
        };

        List<RecommendedRecipe> existingRecipes = recommendedRecipeRepository.findByUserAndDateAndType(user, currentDay, currentType);
        if (!existingRecipes.isEmpty()) {
          recommendedRecipeRepository.deleteAll(existingRecipes);
        }

        String[] recipeNamesForMeal = meals[j].split(",");
        for (String recipeName : recipeNamesForMeal) {
          RecipeData recipeData = recipeDataRepository.findByRecipeName(recipeName.trim());

          if (recipeData == null) {
            log.warn("❗ DB에 없는 레시피 (LLM이 생성): {} (userId: {}, Date: {}, Type: {})", recipeName.trim(), userId, currentDay, currentType);
            continue;
          }

          RecommendedRecipe recipe = new RecommendedRecipe();
          recipe.setUser(user);
          recipe.setDate(currentDay);
          recipe.setType(currentType);
          recipe.setRecipeData(recipeData);

          recommendedRecipeRepository.save(recipe);
        }
      }
    }
  }

  // 업로드된 CSV 파일을 처리하여 DB에 저장
  public void changeCsvToRecipeData(MultipartFile file) throws IOException {
    System.out.println("파일 이름: " + file.getOriginalFilename());
    System.out.println("파일 크기: " + file.getSize());
    // MultipartFile을 읽기 위한 InputStreamReader로 변환
    InputStreamReader reader = new InputStreamReader(file.getInputStream(), StandardCharsets.UTF_8);

    List<RecipeData> recipeDataList = new CsvToBeanBuilder<RecipeData>(reader)
        .withType(RecipeData.class)
        .withSeparator(',') // 필수: csv 파일이 쉼표 구분자일 경우
        .withIgnoreLeadingWhiteSpace(true)
        .build()
        .parse();

    // DB에 저장
    recipeDataRepository.saveAll(recipeDataList);
  }

  // 자카드 유사도 계산
  private double calculateJaccardSimilarity(RecipeData recipe1, RecipeData recipe2) {
    if (recipe1.getIngredients() == null || recipe2.getIngredients() == null) {
      return 0.0;
    }

    Set<String> ingredients1 = new HashSet<>(Arrays.asList(recipe1.getCleanedIngredients().split(",")));
    Set<String> ingredients2 = new HashSet<>(Arrays.asList(recipe2.getCleanedIngredients().split(",")));

    Set<String> intersection = new HashSet<>(ingredients1);
    intersection.retainAll(ingredients2);

    Set<String> union = new HashSet<>(ingredients1);
    union.addAll(ingredients2);

    if (union.isEmpty()) {
      return 0.0;
    }

    return (double) intersection.size() / union.size();
  }
  
  // 푸드데이터로부터 가져오기
  public void nutritionFromFoodData(){
    List<RecipeData> recipeDataList = loadRecipeData();
    List<RecipeData> mok = new ArrayList<>();
    List<FoodData> foods = new ArrayList<>();

    for (RecipeData recipeData : recipeDataList) {
      List<FoodData> foodDataList = foodDataRepository.findByFoodName(recipeData.getRecipeName());

      if (!foodDataList.isEmpty()) {
        FoodData foodData = foodDataList.get(0);

        recipeData.setCalories(foodData.getCalories());
        recipeData.setProtein(foodData.getProtein());
        recipeData.setFat(foodData.getFat());
        recipeData.setCarbohydrate(foodData.getCarbohydrate());

        recipeDataRepository.save(recipeData);

      } else{
        FoodData foodData = openAiApiService.getNutrientInfo(recipeData.getRecipeName());
        foodData.setFoodImage(recipeData.getRecipeImage());
        foodDataRepository.save(foodData);

        recipeData.setCalories(foodData.getCalories());
        recipeData.setProtein(foodData.getProtein());
        recipeData.setFat(foodData.getFat());
        recipeData.setCarbohydrate(foodData.getCarbohydrate());
        recipeDataRepository.save(recipeData);
      }

    }
  }

  // 연관 레시피 찾기
  private Map<RecipeData, List<RecipeData>> findRelatedRecipes(List<RecipeData> allRecipes, int topN) {
    Map<RecipeData, List<RecipeData>> relatedRecipesMap = new HashMap<>();

    for (RecipeData recipe1 : allRecipes) {
      Map<RecipeData, Double> similarityScores = new HashMap<>();
      for (RecipeData recipe2 : allRecipes) {
        if (!recipe1.equals(recipe2)) {
          double similarity = calculateJaccardSimilarity(recipe1, recipe2);
          similarityScores.put(recipe2, similarity);
        }
      }

      List<RecipeData> topNRelated = similarityScores.entrySet().stream()
          .sorted(Map.Entry.<RecipeData, Double>comparingByValue().reversed())
          .limit(topN)
          .map(Map.Entry::getKey)
          .collect(Collectors.toList());

      relatedRecipesMap.put(recipe1, topNRelated);
    }

    return relatedRecipesMap;
  }

  private static class RecipeScore {
    private RecipeData recipe;
    private double score;

    public RecipeScore(RecipeData recipe, double score) {
      this.recipe = recipe;
      this.score = score;
    }

    public RecipeData getRecipe() {
      return recipe;
    }

    public double getScore() {
      return score;
    }
  }
}
