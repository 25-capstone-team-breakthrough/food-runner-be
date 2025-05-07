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

    List<FoodData> foodDataList = foodCSVList.stream().map(
        csv -> {
          FoodData data = new FoodData();
          data.setVitaminE(0.0);
          data.setMagnesium(0.0);
          data.setZinc(0.0);
          data.setLactium(0.0);
          data.setLArginine(0.0);
          data.setOmega3(0.0);

          return data;
        }
    ).collect(Collectors.toList());

    // DB에 저장
    foodDataRepository.saveAll(foodDataList);
  }

}
