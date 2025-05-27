package com.Hansung.Capston.service.Diet;

import com.Hansung.Capston.common.DayOfWeek;
import com.Hansung.Capston.common.DietType;
import com.Hansung.Capston.dto.Diet.Ingredient.PreferredIngredientResponse;
import com.Hansung.Capston.dto.Diet.Ingredient.RecommendedIngredientResponse;
import com.Hansung.Capston.dto.Diet.Nutrition.RecommendedNutrientResponse;
import com.Hansung.Capston.dto.Diet.Recipe.RecommendRecipeResponse;
import com.Hansung.Capston.entity.Diet.Food.FoodData;
import com.Hansung.Capston.entity.Diet.Recipe.RecipeData;
import com.Hansung.Capston.entity.Diet.Recipe.RecommendedRecipe;
import com.Hansung.Capston.entity.Diet.Recipe.RecommendedRecipeCandidate;
import com.Hansung.Capston.entity.UserInfo.User;
import com.Hansung.Capston.repository.Diet.Food.FoodDataRepository;
import com.Hansung.Capston.repository.Diet.Recipe.RecipeDataRepository;
import com.Hansung.Capston.repository.Diet.Recipe.RecommendedRecipeCandidateRepository;
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
  private final RecommendedRecipeCandidateRepository recommendedRecipeCandidateRepository;
  private final UserRepository userRepository;

  private final IngredientService ingredientService;
  private final OpenAiApiService openAiApiService;
  private final NutrientService nutrientService;

  public RecipeService(RecipeDataRepository recipeDataRepository,
      FoodDataRepository foodDataRepository,
      RecommendedRecipeRepository recommendedRecipeRepository,
      RecommendedRecipeCandidateRepository recommendedRecipeCandidateRepository,
      IngredientService ingredientService,
      OpenAiApiService openAiApiService,
      UserRepository userRepository,
      NutrientService nutrientService) {
    this.recipeDataRepository = recipeDataRepository;
    this.foodDataRepository = foodDataRepository;
    this.recommendedRecipeRepository = recommendedRecipeRepository;
    this.recommendedRecipeCandidateRepository = recommendedRecipeCandidateRepository;
    this.ingredientService = ingredientService;
    this.openAiApiService = openAiApiService;
    this.userRepository = userRepository;
    this.nutrientService = nutrientService;
  }

  public List<RecipeData> loadRecipeData() {
    return recipeDataRepository.findAll();
  }

  public void saveRelatedRecipeData() {
    List<RecipeData> allRecipes = recipeDataRepository.findAll();
    int topN = 3;

    Map<RecipeData, List<RecipeData>> relatedRecipesMap = findRelatedRecipes(allRecipes, topN);

    for (Map.Entry<RecipeData, List<RecipeData>> entry : relatedRecipesMap.entrySet()) {
      RecipeData baseRecipe = entry.getKey();
      List<RecipeData> relatedRecipes = entry.getValue();

      if (!relatedRecipes.isEmpty()) {
        baseRecipe.setRelatedRecipe1(String.valueOf(relatedRecipes.getFirst().getRecipeId()));
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

  public List<RecommendRecipeResponse> loadRecommendRecipe(String userId) {
    List<RecommendedRecipe> recommendedRecipes = recommendedRecipeRepository.findByUser_UserId(userId);
    List<RecommendRecipeResponse> recommendedRecipeResponses = new ArrayList<>();
    for (RecommendedRecipe recommendedRecipe : recommendedRecipes) {
      recommendedRecipeResponses.add(RecommendRecipeResponse.toDto(recommendedRecipe));
    }
    log.info("✅ userId: {} 에 대한 추천 식단 {}개를 불러왔습니다.", userId, recommendedRecipeResponses.size());
    return recommendedRecipeResponses;
  }

  public void setRecommendRecipeCandidate(String userId) {
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
    int targetRecipeCount = 100;

    if (!matchingRecipesSet.isEmpty()) {
      finalRecipeCandidates.addAll(matchingRecipesSet);
      log.info("✅ 재료 기반으로 {}개의 레시피가 매칭되었습니다.", finalRecipeCandidates.size());
    } else {
      log.info("ℹ️ 재료 기반으로 매칭되는 레시피가 없습니다.");
    }

    if (finalRecipeCandidates.size() < targetRecipeCount) {
      log.info("ℹ️ 레시피 후보가 부족하여 영양소 기반 레시피를 추가합니다. 현재 {}개, 목표 {}개.", finalRecipeCandidates.size(), targetRecipeCount);

      List<RecommendedNutrientResponse> nutritionLog = nutrientService.loadRecommendedNutrients(userId);

      if (nutritionLog.isEmpty()) {
        log.warn("🚨 영양소 로그가 비어있어 추가 레시피 추천이 불가능합니다. userId: {}", userId);
      } else {
        double recCalories = (nutritionLog.getFirst().getCalories()+nutritionLog.getLast().getCalories())/2;
        double recCarbohydrates = (nutritionLog.getFirst().getCarbohydrate()+nutritionLog.getLast().getCarbohydrate())/2;
        double recProtein = (nutritionLog.getFirst().getProtein()+nutritionLog.getLast().getProtein())/2;
        double recFat = (nutritionLog.getFirst().getFat()+nutritionLog.getLast().getFat())/2;

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
          int recipeServing;
          if(recipe.getServing().isEmpty()){
            recipeServing = 1;
          } else{
            recipeServing = Integer.parseInt(recipe.getServing().replaceAll("[^0-9]", ""));
          }

          double score = 0.0;
          score += Math.abs(((recipeCalories/recipeServing) - recCalories) / calorieRange);
          score += Math.abs(((recipeCarbohydrate/recipeServing) - recCarbohydrates) / carbohydrateRange);
          score += Math.abs(((recipeProtein/recipeServing) - recProtein) / proteinRange);
          score += Math.abs(((recipeFat/recipeServing) - recFat) / fatRange);

          scoredRecipes.add(new RecipeScore(recipe, score));
        }

        scoredRecipes.sort(Comparator.comparingDouble(RecipeScore::score));

        Random random = new Random();
        int nutrientCandidatePoolSize = Math.min(scoredRecipes.size(), 200);
        List<RecipeData> nutrientCandidatesPool = scoredRecipes.stream()
            .map(RecipeScore::recipe)
            .limit(nutrientCandidatePoolSize)
            .collect(Collectors.toList());

        while (finalRecipeCandidates.size() < targetRecipeCount && !nutrientCandidatesPool.isEmpty()) {
          int randomIndex = random.nextInt(nutrientCandidatesPool.size());
          RecipeData recipeToAdd = nutrientCandidatesPool.remove(randomIndex);
          if (!finalRecipeCandidates.contains(recipeToAdd)) {
            finalRecipeCandidates.add(recipeToAdd);
          }
        }
        log.info("✅ 영양소 기반으로 레시피를 추가했습니다. 최종 후보: {}개.", finalRecipeCandidates.size());
      }
    } else if (finalRecipeCandidates.size() > targetRecipeCount) {
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

    List<String> allRecipeNamesForLLM = finalRecipeCandidates.stream()
        .map(RecipeData::getRecipeName)
        .collect(Collectors.toList());
    log.info("✅ LLM에 전달할 최종 레시피 후보 크기: {}", allRecipeNamesForLLM.size());

    Map<DietType, List<String>> mealTypeCandidatesFromLLM = openAiApiService.getMealTypeRecipeCandidates(allRecipeNamesForLLM);
    log.info("✅ LLM으로부터 식사 시간별 레시피 후보군을 성공적으로 받았습니다.");

    User user = userRepository.findByUserId(userId)
        .orElseThrow(() -> new NoSuchElementException("User not found with userId: " + userId));

    recommendedRecipeCandidateRepository.deleteByUser(user);
    log.info("ℹ️ 기존 RecommendedRecipeCandidate 데이터 삭제 완료 (userId: {})", userId);

    for (Map.Entry<DietType, List<String>> entry : mealTypeCandidatesFromLLM.entrySet()) {
      DietType dietType = entry.getKey();
      List<String> recipesForMealType = entry.getValue();

      for (String recipeNameWithSide : recipesForMealType) {
        String mainRecipeName = recipeNameWithSide.split(",")[0].trim();
        RecipeData recipeData = recipeDataRepository.findByRecipeName(mainRecipeName);

        if (recipeData == null) {
          log.warn("❗ DB에 없는 레시피 (LLM이 생성): {} (식사 타입: {})", mainRecipeName, dietType);
          continue;
        }

        RecommendedRecipeCandidate candidate = new RecommendedRecipeCandidate();
        candidate.setUser(user);
        candidate.setDietType(dietType);
        candidate.setRecipeData(recipeData);

        recommendedRecipeCandidateRepository.save(candidate);
      }
    }
    log.info("✅ 식사 시간별 레시피 후보군 RecommendedRecipeCandidate DB 저장 완료.");
  }

  public void setWeekRecommendRecipe(String userId) {
    User user = userRepository.findByUserId(userId)
        .orElseThrow(() -> new NoSuchElementException("User not found with userId: " + userId));

    for (int i = 0; i < 7; i++) {
      DayOfWeek currentDay = DayOfWeek.values()[i];

      for (DietType type : DietType.values()) {
        List<RecommendedRecipeCandidate> candidatesForMealType = recommendedRecipeCandidateRepository.findByUserAndDietType(user, type);

        if (candidatesForMealType.isEmpty()) {
          log.warn("❌ userId: {}, DietType: {} 에 대한 레시피 후보가 부족하여 7일 식단 배정 불가.", userId, type);
          continue;
        }

        List<RecommendedRecipe> existingRecipes = recommendedRecipeRepository.findByUserAndDateAndType(user, currentDay, type);
        if (!existingRecipes.isEmpty()) {
          recommendedRecipeRepository.deleteAll(existingRecipes);
        }

        Random random = new Random();
        RecommendedRecipeCandidate selectedCandidate = candidatesForMealType.get(random.nextInt(candidatesForMealType.size()));

        RecommendedRecipe recipe = new RecommendedRecipe();
        recipe.setUser(user);
        recipe.setDate(currentDay);
        recipe.setType(type);
        recipe.setRecipeData(selectedCandidate.getRecipeData());

        recommendedRecipeRepository.save(recipe);
        log.debug("➡️ {} {}에 {} ({} userId) 레시피 배정: {}", currentDay, type, selectedCandidate.getRecipeData().getRecipeName(), userId, recipe.getRecommendedRecipeId());
      }
    }
    log.info("✅ 7일 식단 배정 완료 (RecommendedRecipeCandidate 풀에서 랜덤 선택).");
  }

  public String updateRecommendRecipe(String userId, DayOfWeek dayOfWeek, DietType dietType) {
    User user = userRepository.findByUserId(userId)
        .orElseThrow(() -> new NoSuchElementException("User not found with userId: " + userId));

    List<RecommendedRecipe> existingRecipes = recommendedRecipeRepository.findByUserAndDateAndType(user, dayOfWeek, dietType);
    if (!existingRecipes.isEmpty()) {
      recommendedRecipeRepository.deleteAll(existingRecipes);
      log.info("기존 추천 레시피 삭제 완료 (userId: {}, Date: {}, Type: {})", userId, dayOfWeek, dietType);
    }

    List<RecommendedRecipeCandidate> candidatesForMealType = recommendedRecipeCandidateRepository.findByUserAndDietType(user, dietType);

    if (candidatesForMealType.isEmpty()) {
      log.warn("❌ userId: {}, DietType: {} 에 대한 레시피 후보가 없어 업데이트 불가.", userId, dietType);
      return "실패: 해당 식사 시간에 대한 레시피 후보가 없습니다.";
    }

    Random random = new Random();
    RecommendedRecipeCandidate selectedCandidate = candidatesForMealType.get(random.nextInt(candidatesForMealType.size()));

    RecommendedRecipe newRecipe = new RecommendedRecipe();
    newRecipe.setUser(user);
    newRecipe.setDate(dayOfWeek);
    newRecipe.setType(dietType);
    newRecipe.setRecipeData(selectedCandidate.getRecipeData());

    recommendedRecipeRepository.save(newRecipe);

    log.info("✅ {} {} 레시피 업데이트 완료: {} (userId: {})", dayOfWeek, dietType, selectedCandidate.getRecipeData().getRecipeName(), userId);
    return "성공: " + dayOfWeek + " " + dietType + " 레시피 업데이트: " + selectedCandidate.getRecipeData().getRecipeName();
  }

  public void changeCsvToRecipeData(MultipartFile file) throws IOException {
    System.out.println("파일 이름: " + file.getOriginalFilename());
    System.out.println("파일 크기: " + file.getSize());
    InputStreamReader reader = new InputStreamReader(file.getInputStream(), StandardCharsets.UTF_8);

    List<RecipeData> recipeDataList = new CsvToBeanBuilder<RecipeData>(reader)
        .withType(RecipeData.class)
        .withSeparator(',')
        .withIgnoreLeadingWhiteSpace(true)
        .build()
        .parse();

    recipeDataRepository.saveAll(recipeDataList);
  }

  private double calculateJacquardSimilarity(RecipeData recipe1, RecipeData recipe2) {
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

  public void nutritionFromFoodData(){
    List<RecipeData> recipeDataList = loadRecipeData();
    List<RecipeData> mok = new ArrayList<>();
    List<FoodData> foods = new ArrayList<>();

    for (RecipeData recipeData : recipeDataList) {
      List<FoodData> foodDataList = foodDataRepository.findByFoodName(recipeData.getRecipeName());

      FoodData foodData;
      if (!foodDataList.isEmpty()) {
        foodData = foodDataList.getFirst();

      } else{
        foodData = openAiApiService.getNutrientInfo(recipeData.getRecipeName());
        foodData.setFoodImage(recipeData.getRecipeImage());
        foodDataRepository.save(foodData);

      }
      recipeData.setCalories(foodData.getCalories());
      recipeData.setProtein(foodData.getProtein());
      recipeData.setFat(foodData.getFat());
      recipeData.setCarbohydrate(foodData.getCarbohydrate());
      recipeDataRepository.save(recipeData);

    }
  }

  private Map<RecipeData, List<RecipeData>> findRelatedRecipes(List<RecipeData> allRecipes, int topN) {
    Map<RecipeData, List<RecipeData>> relatedRecipesMap = new HashMap<>();

    for (RecipeData recipe1 : allRecipes) {
      Map<RecipeData, Double> similarityScores = new HashMap<>();
      for (RecipeData recipe2 : allRecipes) {
        if (!recipe1.equals(recipe2)) {
          double similarity = calculateJacquardSimilarity(recipe1, recipe2);
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

  private record RecipeScore(RecipeData recipe, double score) {

  }
}