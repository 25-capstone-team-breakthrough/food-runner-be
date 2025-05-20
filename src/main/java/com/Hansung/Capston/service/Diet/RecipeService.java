package com.Hansung.Capston.service.Diet;

import com.Hansung.Capston.common.DayOfWeek;
import com.Hansung.Capston.common.DietType;
import com.Hansung.Capston.dto.Diet.Ingredient.PreferredIngredientResponse;
import com.Hansung.Capston.dto.Diet.Ingredient.RecommendedIngredientResponse;
import com.Hansung.Capston.dto.Diet.Recipe.RecommendRecipeResponse;
import com.Hansung.Capston.entity.Diet.Food.FoodData;
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
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Service
@Transactional
public class RecipeService {
  private final RecipeDataRepository recipeDataRepository;
  private final FoodDataRepository foodDataRepository;
  private final RecommendedRecipeRepository recommendedRecipeRepository;
  private final UserRepository userRepository;

  private final IngredientService ingredientService;
  private final OpenAiApiService openAiApiService;

  public RecipeService(RecipeDataRepository recipeDataRepository,
      FoodDataRepository foodDataRepository,
                       RecommendedRecipeRepository recommendedRecipeRepository, IngredientService ingredientService, OpenAiApiService openAiApiService, UserRepository userRepository) {
    this.recipeDataRepository = recipeDataRepository;
    this.foodDataRepository = foodDataRepository;
    this.recommendedRecipeRepository = recommendedRecipeRepository;
      this.ingredientService = ingredientService;
      this.openAiApiService = openAiApiService;
    this.userRepository = userRepository;
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

  // 데이터 불러오기
  public List<RecommendRecipeResponse> loadRecommendRecipe(String userId) {
    List<RecommendedRecipe> recommendedRecipes = recommendedRecipeRepository.findByUser_UserId(userId);
    List<RecommendRecipeResponse> recommendedRecipeResponses = new ArrayList<>();
    for (RecommendedRecipe recommendedRecipe : recommendedRecipes) {
      recommendedRecipeResponses.add(RecommendRecipeResponse.toDto(recommendedRecipe));
    }

    return recommendedRecipeResponses;
  }

  // 레시피 추천
  public void setRecommendRecipe(String userId) {
    List<RecommendedIngredientResponse> recList = ingredientService.loadRecommendedIngredient(userId);
    List<PreferredIngredientResponse> prefList = ingredientService.loadPreferredIngredients(userId);

    List<String> allIngredients = new ArrayList<>();
    recList.forEach(r -> allIngredients.add(r.getIngredient().getIngredientName()));
    prefList.forEach(p -> allIngredients.add(p.getIngredient().getIngredientName()));

    // 연관 레시피 조회
    List<RecipeData> matchingRecipes = recipeDataRepository.findByIngredientsContainingAny(allIngredients);
    List<String> recipeNames = matchingRecipes.stream()
            .map(RecipeData::getRecipeName)
            .collect(Collectors.toList());

    // OpenAI로부터 요일별 식단 추천 받기
    String recommendRecipes = openAiApiService.getRecommendedRecipes(recipeNames);
    String[] weeklyRecipes = recommendRecipes.split("-");

    User user = userRepository.findById(userId).orElseThrow();

    // 요일별 식단 저장 또는 업데이트
    for (int i = 0; i < weeklyRecipes.length; i++) {
      DayOfWeek currentDay;
      switch (i) {
        case 0 -> currentDay = DayOfWeek.mon;
        case 1 -> currentDay = DayOfWeek.tue;
        case 2 -> currentDay = DayOfWeek.wed;
        case 3 -> currentDay = DayOfWeek.thu;
        case 4 -> currentDay = DayOfWeek.fri;
        case 5 -> currentDay = DayOfWeek.sat;
        case 6 -> currentDay = DayOfWeek.sun;
        default -> throw new IllegalStateException("Unexpected day index: " + i);
      }

      String[] meals = weeklyRecipes[i].split("\\|"); // 아침|점심|저녁
      for (int j = 0; j < meals.length; j++) {
        DietType currentType;
        switch (j) {
          case 0 -> currentType = DietType.BREAKFAST;
          case 1 -> currentType = DietType.LUNCH;
          case 2 -> currentType = DietType.DINNER;
          default -> throw new IllegalStateException("Unexpected meal type index: " + j);
        }

        // 기존 추천 레시피 삭제
        recommendedRecipeRepository.findByUserAndDateAndType(user, currentDay, currentType)
                .ifPresent(recommendedRecipeRepository::delete);

        String[] recipeNamesForMeal = meals[j].split(",");
        for (String recipeName : recipeNamesForMeal) {
          RecipeData recipeData = recipeDataRepository.findByRecipeName(recipeName.trim());

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

        mok.add(recipeData);
      } else{
        FoodData foodData = openAiApiService.getNutrientInfo(recipeData.getRecipeName());
        foodDataList.add(foodData);

        recipeData.setCalories(foodData.getCalories());
        recipeData.setProtein(foodData.getProtein());
        recipeData.setFat(foodData.getFat());
        recipeData.setCarbohydrate(foodData.getCarbohydrate());

        mok.add(recipeData);
      }

      foodDataRepository.saveAll(foodDataList);
      recipeDataRepository.saveAll(mok);
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
}