package com.Hansung.Capston.service;

import com.Hansung.Capston.dto.MealLog.FoodDataDTO;
import com.Hansung.Capston.dto.SupplmentApi.SupplementDataFromOpenApi;
import com.Hansung.Capston.entity.DataSet.FoodCSV;
import com.Hansung.Capston.entity.DataSet.FoodData;
import com.Hansung.Capston.entity.Exercise.ExerciseData;
import com.Hansung.Capston.entity.SupplementData;
import com.Hansung.Capston.repository.Exercise.ExerciseDataRepository;
import com.Hansung.Capston.repository.FoodDataRepository;
import com.Hansung.Capston.repository.SupplementDataRepository;
import com.opencsv.bean.CsvToBeanBuilder;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;
import java.util.Optional;
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


  public List<FoodDataDTO> sendAllFoodData(){
    List<FoodData> foodData = foodDataRepository.findAll();
    return foodData.stream().map(FoodDataDTO::fromEntity).
        collect(Collectors.toList());
  }

  @Transactional
  public void saveSupplementAll(List<SupplementDataFromOpenApi> requests) {
    List<SupplementData> entities = requests.stream().map(SupplementDataFromOpenApi::toEntity).collect(Collectors.toList());
    supplementDataRepository.saveAll(entities);
  }



  // 업로드된 CSV 파일을 처리하여 DB에 저장
  public void processFoodCsvFile(MultipartFile file) throws IOException {
    System.out.println("파일 이름: " + file.getOriginalFilename());
    System.out.println("파일 크기: " + file.getSize());
    // MultipartFile을 읽기 위한 InputStreamReader로 변환
    InputStreamReader reader = new InputStreamReader(file.getInputStream());

    // OpenCSV 라이브러리로 CSV를 객체 리스트로 변환
    List<FoodCSV> foodCSVList = new CsvToBeanBuilder<FoodCSV>(reader)
        .withType(FoodCSV.class)
        .build()
        .parse();

    List<FoodData> foodDataList = foodCSVList.stream()
        .filter(csv -> csv.getFoodName() != null && !csv.getFoodName().isBlank()) // 필수값 체크
        .map(csv -> {
          return FoodData.builder()
              .foodName(csv.getFoodName())
              .foodCompany(csv.getFoodCompany())
              .foodImage(csv.getFoodImage())

              .calories(Optional.ofNullable(csv.getCalories()).orElse(0.0))
              .protein(Optional.ofNullable(csv.getProtein()).orElse(0.0))
              .fat(Optional.ofNullable(csv.getFat()).orElse(0.0))
              .carbohydrate(Optional.ofNullable(csv.getCarbohydrate()).orElse(0.0))
              .sugar(Optional.ofNullable(csv.getSugar()).orElse(0.0))
              .dietaryFiber(Optional.ofNullable(csv.getDietaryFiber()).orElse(0.0))
              .calcium(Optional.ofNullable(csv.getCalcium()).orElse(0.0))
              .potassium(Optional.ofNullable(csv.getPotassium()).orElse(0.0))
              .sodium(Optional.ofNullable(csv.getSodium()).orElse(0.0))

              .vitaminA(Optional.ofNullable(csv.getVitaminA()).orElse(0.0))
              .vitaminB1(Optional.ofNullable(csv.getVitaminB1()).orElse(0.0))
              .vitaminC(Optional.ofNullable(csv.getVitaminC()).orElse(0.0))
              .vitaminD(Optional.ofNullable(csv.getVitaminD()).orElse(0.0))
              .cholesterol(Optional.ofNullable(csv.getCholesterol()).orElse(0.0))
              .saturatedFat(Optional.ofNullable(csv.getSaturatedFat()).orElse(0.0))
              .transFat(Optional.ofNullable(csv.getTransFat()).orElse(0.0))

              // CSV에는 없지만 엔티티에 존재하는 필드
              .vitaminE(0.0)
              .magnesium(0.0)
              .zinc(0.0)
              .lactium(0.0)
              .lArginine(0.0)
              .omega3(0.0)

              .build();
        })
        .toList();


    // DB에 저장
    foodDataRepository.saveAll(foodDataList);
  }

}
