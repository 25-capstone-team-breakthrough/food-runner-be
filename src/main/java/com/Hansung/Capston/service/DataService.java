package com.Hansung.Capston.service;

import com.Hansung.Capston.dto.SupplmentApi.SupplementDataFromOpenApi;
import com.Hansung.Capston.entity.DataSet.FoodData;
import com.Hansung.Capston.entity.SupplementData;
import com.Hansung.Capston.repository.FoodDataRepository;
import com.Hansung.Capston.repository.SupplementDataRepository;
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

  @Autowired
  public DataService(FoodDataRepository foodDataRepository,
      SupplementDataRepository supplementDataRepository) {
    this.foodDataRepository = foodDataRepository;
    this.supplementDataRepository = supplementDataRepository;
  }


  public List<FoodData> sendAllFoodData(){
    return foodDataRepository.findAll();
  }

  @Transactional
  public void saveSupplementAll(List<SupplementDataFromOpenApi> requests) {
    List<SupplementData> entities = requests.stream().map(SupplementDataFromOpenApi::toEntity).collect(Collectors.toList());
    supplementDataRepository.saveAll(entities);
  }



  // 업로드된 CSV 파일을 처리하여 DB에 저장
  @Transactional
  public String processFoodCsvFile(MultipartFile file) throws IOException {
    System.out.println("파일 이름: " + file.getOriginalFilename());
    System.out.println("파일 크기: " + file.getSize());

    // MultipartFile을 읽기 위한 InputStreamReader로 변환
    InputStreamReader reader = new InputStreamReader(file.getInputStream());

    // OpenCSV 라이브러리로 CSV를 객체 리스트로 변환
    List<FoodData> foodDatasetList = new CsvToBeanBuilder<FoodData>(reader)
        .withType(FoodData.class)
        .build()
        .parse();

    for (FoodData data : foodDatasetList) {
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
      if (data.getVitaminE() == null) data.setVitaminE(0.0);
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
    foodDataRepository.saveAll(foodDatasetList);

    String firstFoodName = foodDatasetList.stream()
        .findFirst()
        .map(FoodData::getFoodName)
        .orElse("없음");

    return "데이터 갯수 : " + foodDatasetList.size() + "\n첫 번째 데이터 음식 명: " + firstFoodName;

  }

}
