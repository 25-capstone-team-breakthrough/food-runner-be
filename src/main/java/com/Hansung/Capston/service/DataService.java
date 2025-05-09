package com.Hansung.Capston.service;

import com.Hansung.Capston.entity.Diet.Food.FoodData;
import com.Hansung.Capston.entity.Diet.Ingredient.IngredientData;
import com.Hansung.Capston.entity.Diet.Supplement.SupplementData;
import com.Hansung.Capston.repository.Diet.Food.FoodDataRepository;
import com.Hansung.Capston.repository.Diet.Ingredient.IngredientDataRepository;
import com.Hansung.Capston.repository.Diet.Supplement.SupplementDataRepository;
import com.opencsv.bean.CsvToBeanBuilder;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;
import java.util.stream.Collectors;

import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class DataService {
  private final FoodDataRepository foodDataRepository;
  private final SupplementDataRepository supplementDataRepository;
  private final IngredientDataRepository ingredientDataRepository;

  @Autowired
  public DataService(FoodDataRepository foodDataRepository,
      SupplementDataRepository supplementDataRepository,
      IngredientDataRepository ingredientDataRepository) {
    this.foodDataRepository = foodDataRepository;
    this.supplementDataRepository = supplementDataRepository;
    this.ingredientDataRepository = ingredientDataRepository;
  }


  public List<FoodData> sendAllFoodData(){
    return foodDataRepository.findAll();
  }


  @Transactional
  public String processIngredientCsvFile(MultipartFile file) throws IOException {
    System.out.println("파일 이름: " + file.getOriginalFilename());
    System.out.println("파일 크기: " + file.getSize());

    // MultipartFile을 읽기 위한 InputStreamReader로 변환
    InputStreamReader reader = new InputStreamReader(file.getInputStream());

    // OpenCSV 라이브러리로 CSV를 객체 리스트로 변환
    List<IngredientData> ingredientDataList = new CsvToBeanBuilder<IngredientData>(reader)
        .withType(IngredientData.class)
        .build()
        .parse();

    for (IngredientData data : ingredientDataList) {
      if (data.getCalories() == null) data.setCalories(0.0);
      if (data.getProtein() == null) data.setProtein(0.0);
      if (data.getCarbohydrate() == null) data.setCarbohydrate(0.0);
      if (data.getFat() == null) data.setFat(0.0);
      if (data.getSugar() == null) data.setSugar(0.0);
      if (data.getDietaryFiber() == null) data.setDietaryFiber(0.0);
      if (data.getCalcium() == null) data.setCalcium(0.0);
      if (data.getPotassium() == null) data.setPotassium(0.0);
      if (data.getSodium() == null) data.setSodium(0.0);
      if (data.getVitaminA() == null) data.setVitaminA(0.0);
      if (data.getVitaminB1() == null) data.setVitaminB1(0.0);
      if (data.getVitaminC() == null) data.setVitaminC(0.0);
      if (data.getVitaminD() == null) data.setVitaminD(0.0);
      if (data.getCholesterol() == null) data.setCholesterol(0.0);
      if (data.getSaturatedFat() == null) data.setSaturatedFat(0.0);
      if (data.getTransFat() == null) data.setTransFat(0.0);
      if (data.getMagnesium() == null) data.setMagnesium(0.0);
      if (data.getZinc() == null) data.setZinc(0.0);
      if (data.getLactium() == null) data.setLactium(0.0);
      if (data.getLArginine() == null) data.setLArginine(0.0);
      if (data.getOmega3() == null) data.setOmega3(0.0);
    }

    // DB에 저장
    ingredientDataRepository.saveAll(ingredientDataList);

    String firstFoodName = ingredientDataList.stream()
        .findFirst()
        .map(IngredientData::getIngredientName)
        .orElse("없음");

    return "데이터 갯수 : " + ingredientDataList.size() + "\n첫 번째 데이터 음식 명: " + firstFoodName;

  }

}
