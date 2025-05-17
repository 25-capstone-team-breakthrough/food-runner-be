package com.Hansung.Capston.service.Diet;

import com.Hansung.Capston.entity.Diet.Recipe.RecipeData;
import com.Hansung.Capston.entity.Diet.Recipe.RecommendedRecipe;
import com.Hansung.Capston.repository.Diet.Recipe.RecipeDataRepository;
import com.Hansung.Capston.repository.Diet.Recipe.RecommendedRecipeRepository;
import com.opencsv.bean.CsvToBeanBuilder;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Service
@Transactional
public class RecipeService {
  private final RecipeDataRepository recipeDataRepository;
  private final RecommendedRecipeRepository recommendedRecipeRepository;

  public RecipeService(RecipeDataRepository recipeDataRepository,
      RecommendedRecipeRepository recommendedRecipeRepository) {
    this.recipeDataRepository = recipeDataRepository;
    this.recommendedRecipeRepository = recommendedRecipeRepository;
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

  // 레시피 추천
  public void recommendRecipe(String userId) {
    // 사용자 기반 레시피 추천 로직 (현재는 비어 있음)
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

    Set<String> ingredients1 = new HashSet<>(Arrays.asList(recipe1.getIngredients().split(",")));
    Set<String> ingredients2 = new HashSet<>(Arrays.asList(recipe2.getIngredients().split(",")));

    Set<String> intersection = new HashSet<>(ingredients1);
    intersection.retainAll(ingredients2);

    Set<String> union = new HashSet<>(ingredients1);
    union.addAll(ingredients2);

    if (union.isEmpty()) {
      return 0.0;
    }

    return (double) intersection.size() / union.size();
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