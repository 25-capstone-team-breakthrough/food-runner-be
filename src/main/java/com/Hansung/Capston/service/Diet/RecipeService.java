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
import org.springframework.transaction.annotation.Propagation;
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

  @Transactional
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
    } else {
      log.info("ℹ️ 재료 기반으로 매칭되는 레시피가 없습니다.");
    }

    if (finalRecipeCandidates.size() < targetRecipeCount) {
      List<RecommendedNutrientResponse> nutritionLog = nutrientService.loadRecommendedNutrients(userId);
      if (!nutritionLog.isEmpty()) {
        double recCalories = (nutritionLog.getFirst().getCalories() + nutritionLog.getLast().getCalories()) / 2;
        double recCarbohydrates = (nutritionLog.getFirst().getCarbohydrate() + nutritionLog.getLast().getCarbohydrate()) / 2;
        double recProtein = (nutritionLog.getFirst().getProtein() + nutritionLog.getLast().getProtein()) / 2;
        double recFat = (nutritionLog.getFirst().getFat() + nutritionLog.getLast().getFat()) / 2;

        List<RecipeData> allRecipes = recipeDataRepository.findAll();
        List<RecipeScore> scoredRecipes = new ArrayList<>();

        for (RecipeData recipe : allRecipes) {
          if (finalRecipeCandidates.contains(recipe)) continue;

          double score = 0.0;
          score += Math.abs(((recipe.getCalories() != null ? recipe.getCalories() : 0) - recCalories) / recCalories);
          score += Math.abs(((recipe.getCarbohydrate() != null ? recipe.getCarbohydrate() : 0) - recCarbohydrates) / recCarbohydrates);
          score += Math.abs(((recipe.getProtein() != null ? recipe.getProtein() : 0) - recProtein) / recProtein);
          score += Math.abs(((recipe.getFat() != null ? recipe.getFat() : 0) - recFat) / recFat);

          scoredRecipes.add(new RecipeScore(recipe, score));
        }


        scoredRecipes.sort(Comparator.comparingDouble(RecipeScore::score));
        List<RecipeData> nutrientCandidates = scoredRecipes.stream()
            .map(RecipeScore::recipe)
            .limit(200)
            .collect(Collectors.toList());

        Random random = new Random();
        while (finalRecipeCandidates.size() < targetRecipeCount && !nutrientCandidates.isEmpty()) {
          finalRecipeCandidates.add(nutrientCandidates.remove(random.nextInt(nutrientCandidates.size())));
        }
      }
    } else if (finalRecipeCandidates.size() > targetRecipeCount) {
      Collections.shuffle(finalRecipeCandidates);
      finalRecipeCandidates = finalRecipeCandidates.subList(0, targetRecipeCount);
    }

    List<String> recipeNames = finalRecipeCandidates.stream()
        .map(RecipeData::getRecipeName)
        .collect(Collectors.toList());

    Map<DietType, List<String>> mealMap = openAiApiService.getMealTypeRecipeCandidates(recipeNames);
    User user = userRepository.findByUserId(userId)
        .orElseThrow(() -> new NoSuchElementException("User not found with userId: " + userId));

    deleteOldCandidates(user);

    for (Map.Entry<DietType, List<String>> entry : mealMap.entrySet()) {
      DietType dietType = entry.getKey();
      for (String recipeNameWithSide : entry.getValue()) {
        String mainRecipeName = recipeNameWithSide.split(",")[0].trim();
        RecipeData recipeData = recipeDataRepository.findByRecipeName(mainRecipeName);
        if (recipeData == null) continue;

        RecommendedRecipeCandidate candidate = new RecommendedRecipeCandidate();
        candidate.setUser(user);
        candidate.setDietType(dietType);
        candidate.setRecipeData(recipeData);
        recommendedRecipeCandidateRepository.save(candidate);
      }
    }
  }

  @Transactional
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

  @Transactional
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

  @Transactional(propagation = Propagation.REQUIRES_NEW)
  public void deleteOldCandidates(User user) {
    recommendedRecipeCandidateRepository.deleteByUser(user);
  }

}