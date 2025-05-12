package com.Hansung.Capston.service.Diet;

import com.Hansung.Capston.entity.Diet.Recipe.RecipeData;
import com.Hansung.Capston.entity.Exercise.ExerciseData;
import com.Hansung.Capston.repository.Diet.Recipe.RecipeDataRepository;
import com.Hansung.Capston.repository.Diet.Recipe.RecommendedRecipeRepository;
import com.opencsv.bean.CsvToBeanBuilder;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class RecipeService {
  private final RecipeDataRepository recipeDataRepository;
  private final RecommendedRecipeRepository recommendedRecipeRepository;

  public RecipeService(RecipeDataRepository recipeDataRepository,
      RecommendedRecipeRepository recommendedRecipeRepository) {
    this.recipeDataRepository = recipeDataRepository;
    this.recommendedRecipeRepository = recommendedRecipeRepository;
  }

  // 업로드된 CSV 파일을 처리하여 DB에 저장
  public void changeCsvToRecipeData(MultipartFile file) throws IOException {
    System.out.println("파일 이름: " + file.getOriginalFilename());
    System.out.println("파일 크기: " + file.getSize());
    // MultipartFile을 읽기 위한 InputStreamReader로 변환
    InputStreamReader reader = new InputStreamReader(file.getInputStream());

    // OpenCSV 라이브러리로 CSV를 객체 리스트로 변환
    List<RecipeData> recipeDataList = new CsvToBeanBuilder<RecipeData>(reader)
        .withType(RecipeData.class)
        .build()
        .parse();

    // DB에 저장
    recipeDataRepository.saveAll(recipeDataList);
  }
}
