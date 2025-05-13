package com.Hansung.Capston.service.Diet;

import com.Hansung.Capston.dto.Diet.Food.PreferredFoodResponse;
import com.Hansung.Capston.entity.Diet.Food.FoodData;
import com.Hansung.Capston.entity.Diet.Food.PreferredFood;
import com.Hansung.Capston.repository.Diet.Food.FoodDataRepository;
import com.Hansung.Capston.repository.Diet.Food.PreferredFoodRepository;
import com.Hansung.Capston.repository.UserInfo.UserRepository;
import com.opencsv.bean.CsvToBeanBuilder;
import jakarta.transaction.Transactional;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class FoodService {
  private final FoodDataRepository foodDataRepository;
  private final PreferredFoodRepository preferredFoodRepository;
  private final UserRepository userRepository;

  @Autowired
  public FoodService(FoodDataRepository foodDataRepository,
      PreferredFoodRepository preferredFoodRepository, UserRepository userRepository) {
    this.foodDataRepository = foodDataRepository;
    this.preferredFoodRepository = preferredFoodRepository;
    this.userRepository = userRepository;
  }
  
  // 음식 데이터 불러오기
  public List<FoodData> loadFoodData() {
    return foodDataRepository.findAll();
  }
  
  // 음식 즐겨찾기 등록하기
  public String savePreferredFood(String userId, Long foodId) {
    PreferredFood preferredFood;

    if((preferredFood = preferredFoodRepository.findByUserUserIdAndFoodFoodId(userId,foodId)) != null){
      return "실패 : 이미 추가되어 있습니다";
    } else{
      preferredFood = new PreferredFood();

      preferredFood.setUser(userRepository.findById(userId).get());
      preferredFood.setFood(foodDataRepository.findById(foodId).get());

      preferredFoodRepository.save(preferredFood);

      return ("성공 : 즐겨찾기 추가");
    }
  }

  // 음식 즐겨찾기 불러오기
  public List<PreferredFoodResponse> loadPreferredFood(String userId) {
    List<PreferredFood> preferredFoods = preferredFoodRepository.findByUserUserId(userId);
    List<PreferredFoodResponse> res = new ArrayList<>();

    for (PreferredFood preferredFood : preferredFoods) {
      PreferredFoodResponse response = PreferredFoodResponse.toDTO(preferredFood);
      res.add(response);
    }

    return res;
  }
  
  // 음식 즐겨찾기 삭제하기
  public String deletePreferredFood(Long preferredFoodId) {
    preferredFoodRepository.deleteById(preferredFoodId);
    
    return "성공 : 즐겨찾기 삭제";
  }
  
  // csv 파일 to 음식 데이터
  @Transactional
  public String changeCsvToFoodData(MultipartFile file) throws IOException {
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
