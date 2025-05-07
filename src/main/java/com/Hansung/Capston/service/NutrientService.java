package com.Hansung.Capston.service;

import com.Hansung.Capston.dto.MealLog.FoodDataDTO;
import com.Hansung.Capston.dto.MealLog.AverageNutritionDTO;
import com.Hansung.Capston.dto.MealLog.SelectDateNutritionDTO;
import com.Hansung.Capston.dto.Nutrition.NutritionStatusDTO;
import com.Hansung.Capston.entity.DataSet.FoodData;
import com.Hansung.Capston.entity.MealLog.MealLog;
import com.Hansung.Capston.entity.NutritionLog;
import com.Hansung.Capston.entity.NutritionStatus;
import com.Hansung.Capston.entity.NutritionType;
import com.Hansung.Capston.entity.RecommendedNutrient;
import com.Hansung.Capston.repository.FoodDataRepository;
import com.Hansung.Capston.repository.MealLogRepository;
import com.Hansung.Capston.repository.NutritionLogRepository;
import com.Hansung.Capston.repository.RecommendedNutrientRepository;
import com.Hansung.Capston.repository.UserInfo.UserRepository;
import com.Hansung.Capston.service.UserInfo.BMIService;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class NutrientService {
  private final FoodDataRepository foodDataRepository;
  private final NutritionLogRepository nutritionLogRepository;
  private final RecommendedNutrientRepository recommendedNutrientRepository;
  private final MealLogRepository mealLogRepository;
  private final UserRepository userRepository;

  private final BMIService bmiService;

  @Autowired
  public NutrientService(FoodDataRepository foodDataRepository,
      NutritionLogRepository nutritionLogRepository,
      RecommendedNutrientRepository recommendedNutrientRepository, MealLogRepository mealLogRepository,
      UserRepository userRepository, BMIService bmiService) {
    this.foodDataRepository = foodDataRepository;
    this.nutritionLogRepository = nutritionLogRepository;
    this.recommendedNutrientRepository = recommendedNutrientRepository;
    this.mealLogRepository = mealLogRepository;
    this.userRepository = userRepository;
    this.bmiService = bmiService;
  }


  public List<FoodDataDTO> checkNutrientData(List<String> foods, OpenAiApiService openAiApiService) {
    List<FoodDataDTO> foodDataDTOS = new ArrayList<>();

    for (String food : foods) {
     List<FoodData> foodDataList = foodDataRepository.findByFoodName(food);

     if(foodDataList.isEmpty()) {
       FoodDataDTO nutrientInfo = openAiApiService.getNutrientInfo(food);
       nutrientInfo.setFoodImage("");
       foodDataRepository.save(nutrientInfo.toEntity());
       foodDataDTOS.add(nutrientInfo);
     } else{
       foodDataDTOS.add(FoodDataDTO.fromEntity(foodDataList.get(0)));
     }

   }

    return foodDataDTOS;
  }

  public void setNutrientLog(String userId, LocalDateTime date, boolean addOrDel) {

    NutritionLog nutrientLog = nutritionLogRepository.findByDateAndUserId(date,userId).get(0);

    // 마지막 meal 로그를 추가 혹은 삭제
    MealLog mealLog = mealLogRepository.findMealLogsByUserIdAndDate
        (userId,date).getLast();
    if (addOrDel) {
      nutrientLog.setCalories(nutrientLog.getCalories() + mealLog.getCalories());
      nutrientLog.setProtein(nutrientLog.getProtein() + mealLog.getProtein());
      nutrientLog.setCarbohydrate(nutrientLog.getCarbohydrate() + mealLog.getCarbohydrate());
      nutrientLog.setFat(nutrientLog.getFat() + mealLog.getFat());
      nutrientLog.setSugar(nutrientLog.getSugar() + mealLog.getSugar());
      nutrientLog.setSodium(nutrientLog.getSodium() + mealLog.getSodium());
      nutrientLog.setDietaryFiber(nutrientLog.getDietaryFiber() + mealLog.getDietaryFiber());
      nutrientLog.setCalcium(nutrientLog.getCalcium() + mealLog.getCalcium());
      nutrientLog.setSaturatedFat(nutrientLog.getSaturatedFat() + mealLog.getSaturatedFat());
      nutrientLog.setTransFat(nutrientLog.getTransFat() + mealLog.getTransFat());
      nutrientLog.setCholesterol(nutrientLog.getCholesterol() + mealLog.getCholesterol());
      nutrientLog.setVitaminA(nutrientLog.getVitaminA() + mealLog.getVitaminA());
      nutrientLog.setVitaminB1(nutrientLog.getVitaminB1() + mealLog.getVitaminB1());
      nutrientLog.setVitaminC(nutrientLog.getVitaminC() + mealLog.getVitaminC());
      nutrientLog.setVitaminD(nutrientLog.getVitaminD() + mealLog.getVitaminD());
      nutrientLog.setVitaminE(nutrientLog.getVitaminE() + mealLog.getVitaminE());
      nutrientLog.setMagnesium(nutrientLog.getMagnesium() + mealLog.getMagnesium());
      nutrientLog.setZinc(nutrientLog.getZinc() + mealLog.getZinc());
      nutrientLog.setLactium(nutrientLog.getLactium() + mealLog.getLactium());
      nutrientLog.setPotassium(nutrientLog.getPotassium() + mealLog.getPotassium());
      nutrientLog.setLArginine(nutrientLog.getLArginine() + mealLog.getLArginine());
      nutrientLog.setOmega3(nutrientLog.getOmega3() + mealLog.getOmega3());
    } else {
      nutrientLog.setCalories(nutrientLog.getCalories() - mealLog.getCalories());
      nutrientLog.setProtein(nutrientLog.getProtein() - mealLog.getProtein());
      nutrientLog.setCarbohydrate(nutrientLog.getCarbohydrate() - mealLog.getCarbohydrate());
      nutrientLog.setFat(nutrientLog.getFat() - mealLog.getFat());
      nutrientLog.setSugar(nutrientLog.getSugar() - mealLog.getSugar());
      nutrientLog.setSodium(nutrientLog.getSodium() - mealLog.getSodium());
      nutrientLog.setDietaryFiber(nutrientLog.getDietaryFiber() - mealLog.getDietaryFiber());
      nutrientLog.setCalcium(nutrientLog.getCalcium() - mealLog.getCalcium());
      nutrientLog.setSaturatedFat(nutrientLog.getSaturatedFat() - mealLog.getSaturatedFat());
      nutrientLog.setTransFat(nutrientLog.getTransFat() - mealLog.getTransFat());
      nutrientLog.setCholesterol(nutrientLog.getCholesterol() - mealLog.getCholesterol());
      nutrientLog.setVitaminA(nutrientLog.getVitaminA() - mealLog.getVitaminA());
      nutrientLog.setVitaminB1(nutrientLog.getVitaminB1() - mealLog.getVitaminB1());
      nutrientLog.setVitaminC(nutrientLog.getVitaminC() - mealLog.getVitaminC());
      nutrientLog.setVitaminD(nutrientLog.getVitaminD() - mealLog.getVitaminD());
      nutrientLog.setVitaminE(nutrientLog.getVitaminE() - mealLog.getVitaminE());
      nutrientLog.setMagnesium(nutrientLog.getMagnesium() - mealLog.getMagnesium());
      nutrientLog.setZinc(nutrientLog.getZinc() - mealLog.getZinc());
      nutrientLog.setLactium(nutrientLog.getLactium() - mealLog.getLactium());
      nutrientLog.setPotassium(nutrientLog.getPotassium() - mealLog.getPotassium());
      nutrientLog.setLArginine(nutrientLog.getLArginine() - mealLog.getLArginine());
      nutrientLog.setOmega3(nutrientLog.getOmega3() - mealLog.getOmega3());
    }

    nutritionLogRepository.save(nutrientLog);
  }

  public SelectDateNutritionDTO getSelectDateNutrition(String userId, LocalDateTime date) {
    SelectDateNutritionDTO selectDateNutritionDTO = new SelectDateNutritionDTO();
    NutritionLog nutritionLog = nutritionLogRepository.findByDateAndUserId(date,userId).get(0);
    selectDateNutritionDTO.setSelectDate(date);

    // NutritionLog
    selectDateNutritionDTO.setCalories(nutritionLog.getCalories());
    selectDateNutritionDTO.setProtein(nutritionLog.getProtein());
    selectDateNutritionDTO.setCarbohydrate(nutritionLog.getCarbohydrate());
    selectDateNutritionDTO.setFat(nutritionLog.getFat());
    selectDateNutritionDTO.setSugar(nutritionLog.getSugar());
    selectDateNutritionDTO.setSodium(nutritionLog.getSodium());
    selectDateNutritionDTO.setDietaryFiber(nutritionLog.getDietaryFiber());
    selectDateNutritionDTO.setCalcium(nutritionLog.getCalcium());
    selectDateNutritionDTO.setSaturatedFat(nutritionLog.getSaturatedFat());
    selectDateNutritionDTO.setTransFat(nutritionLog.getTransFat());
    selectDateNutritionDTO.setCholesterol(nutritionLog.getCholesterol());
    selectDateNutritionDTO.setVitaminA(nutritionLog.getVitaminA());
    selectDateNutritionDTO.setVitaminB1(nutritionLog.getVitaminB1());
    selectDateNutritionDTO.setVitaminC(nutritionLog.getVitaminC());
    selectDateNutritionDTO.setVitaminD(nutritionLog.getVitaminD());
    selectDateNutritionDTO.setVitaminE(nutritionLog.getVitaminE());
    selectDateNutritionDTO.setMagnesium(nutritionLog.getMagnesium());
    selectDateNutritionDTO.setZinc(nutritionLog.getZinc());
    selectDateNutritionDTO.setLactium(nutritionLog.getLactium());
    selectDateNutritionDTO.setPotassium(nutritionLog.getPotassium());
    selectDateNutritionDTO.setLArginine(nutritionLog.getLArginine());
    selectDateNutritionDTO.setOmega3(nutritionLog.getOmega3());

    return selectDateNutritionDTO;
  }

  public AverageNutritionDTO getAverageNutrition(String userId, LocalDateTime date){
    LocalDateTime startDate = LocalDateTime.now().minusDays(30);
    AverageNutritionDTO dto = nutritionLogRepository.findAverageNutritionForLast30Days(userId, startDate);


    return dto;
  }

  public void setRecommendedNutrition(String userId, NutritionType type) {
    RecommendedNutrient recommendedNutrient = new RecommendedNutrient();
    recommendedNutrient.setUser(userRepository.findById(userId).orElseThrow());
    recommendedNutrient.setType(type);

    double bmr = bmiService.calculateBMR(userId);
    double calories = 0.0;
    double carbohydrate = 0.0;
    double protein = 0.0;
    double fat = 0.0;
    double sugar = 0.0;
    double saturatedFat = 0.0;
    double transFat = 0.0;

    if (type == NutritionType.MAX) {
      calories = bmr * 1.75;
      carbohydrate = calories * 0.65 / 4;
      protein = calories * 0.20 / 4;
      fat = calories * 0.30 / 9;
      sugar = calories * 0.20 / 4;
      saturatedFat = calories * 0.07 / 9;
      transFat = calories * 0.01 / 9;
    } else if (type == NutritionType.MIN) {
      calories = bmr * 1.3;
      carbohydrate = calories * 0.55 / 4;
      protein = calories * 0.07 / 4;
      fat = calories * 0.15 / 9;
      sugar = 0.0;
      saturatedFat = 0.0;
      transFat = 0.0;
    }

      // 고정값 (비타민 및 무기질은 MIN/MAX 기준으로 분기)
      double sodium = 2000; // 일단 고정
      double dietaryFiber = (type == NutritionType.MIN) ? 25 : 30;
      double calcium = (type == NutritionType.MIN) ? 700 : 2500;
      double cholesterol = 300.0; // 고정된 상한 기준
      double vitaminA = (type == NutritionType.MIN) ? 600 : 3000;
      double vitaminB1 = (type == NutritionType.MIN) ? 1.1 : 5.0;
      double vitaminC = (type == NutritionType.MIN) ? 75 : 2000;
      double vitaminD = (type == NutritionType.MIN) ? 5 : 100;
      double vitaminE = (type == NutritionType.MIN) ? 7 : 1000;
      double magnesium = (type == NutritionType.MIN) ? 300 : 700;
      double zinc = (type == NutritionType.MIN) ? 8 : 35;
      double potassium = (type == NutritionType.MIN) ? 2600 : 4700;
      double lArginine = 2.5;
      double omega3 = (type == NutritionType.MIN) ? 1.0 : 3.0;
      double lactium = 0.0;

    recommendedNutrient.setCalories(calories);
    recommendedNutrient.setCarbohydrate(carbohydrate);
    recommendedNutrient.setProtein(protein);
    recommendedNutrient.setFat(fat);
    recommendedNutrient.setSugar(sugar);
    recommendedNutrient.setSaturatedFat(saturatedFat);
    recommendedNutrient.setTransFat(transFat);
    recommendedNutrient.setSodium(sodium);
    recommendedNutrient.setDietaryFiber(dietaryFiber);
    recommendedNutrient.setCalcium(calcium);
    recommendedNutrient.setCholesterol(cholesterol);
    recommendedNutrient.setVitaminA(vitaminA);
    recommendedNutrient.setVitaminB1(vitaminB1);
    recommendedNutrient.setVitaminC(vitaminC);
    recommendedNutrient.setVitaminD(vitaminD);
    recommendedNutrient.setVitaminE(vitaminE);
    recommendedNutrient.setMagnesium(magnesium);
    recommendedNutrient.setZinc(zinc);
    recommendedNutrient.setPotassium(potassium);
    recommendedNutrient.setLArginine(lArginine);
    recommendedNutrient.setOmega3(omega3);
    recommendedNutrient.setLactium(lactium);

    recommendedNutrientRepository.save(recommendedNutrient);
  }

  public NutritionStatusDTO getNutritionStatusForDate(String userId, LocalDateTime date) {
    NutritionLog log = nutritionLogRepository.findByDateAndUserId(date, userId).get(0);
    RecommendedNutrient min = recommendedNutrientRepository.findByUserUserIdAndType(userId, NutritionType.MIN);
    RecommendedNutrient max = recommendedNutrientRepository.findByUserUserIdAndType(userId, NutritionType.MAX);

    Map<String, NutritionStatus> statusMap = new HashMap<>();

    // 각 항목별 상태 계산
    statusMap.put("calories", compare(log.getCalories(), min.getCalories(), max.getCalories()));
    statusMap.put("carbohydrate", compare(log.getCarbohydrate(), min.getCarbohydrate(), max.getCarbohydrate()));
    statusMap.put("protein", compare(log.getProtein(), min.getProtein(), max.getProtein()));
    statusMap.put("fat", compare(log.getFat(), min.getFat(), max.getFat()));
    statusMap.put("sugar", compare(log.getSugar(), min.getSugar(), max.getSugar()));
    statusMap.put("sodium", compare(log.getSodium(), min.getSodium(), max.getSodium()));
    statusMap.put("dietaryFiber", compare(log.getDietaryFiber(), min.getDietaryFiber(), max.getDietaryFiber()));
    statusMap.put("calcium", compare(log.getCalcium(), min.getCalcium(), max.getCalcium()));
    statusMap.put("saturatedFat", compare(log.getSaturatedFat(), min.getSaturatedFat(), max.getSaturatedFat()));
    statusMap.put("transFat", compare(log.getTransFat(), min.getTransFat(), max.getTransFat()));
    statusMap.put("cholesterol", compare(log.getCholesterol(), min.getCholesterol(), max.getCholesterol()));
    statusMap.put("vitaminA", compare(log.getVitaminA(), min.getVitaminA(), max.getVitaminA()));
    statusMap.put("vitaminB1", compare(log.getVitaminB1(), min.getVitaminB1(), max.getVitaminB1()));
    statusMap.put("vitaminC", compare(log.getVitaminC(), min.getVitaminC(), max.getVitaminC()));
    statusMap.put("vitaminD", compare(log.getVitaminD(), min.getVitaminD(), max.getVitaminD()));
    statusMap.put("vitaminE", compare(log.getVitaminE(), min.getVitaminE(), max.getVitaminE()));
    statusMap.put("magnesium", compare(log.getMagnesium(), min.getMagnesium(), max.getMagnesium()));
    statusMap.put("zinc", compare(log.getZinc(), min.getZinc(), max.getZinc()));
    statusMap.put("potassium", compare(log.getPotassium(), min.getPotassium(), max.getPotassium()));
    statusMap.put("lArginine", compare(log.getLArginine(), min.getLArginine(), max.getLArginine()));
    statusMap.put("omega3", compare(log.getOmega3(), min.getOmega3(), max.getOmega3()));
    statusMap.put("lactium", compare(log.getLactium(), min.getLactium(), max.getLactium()));

    NutritionStatusDTO result = new NutritionStatusDTO();
    result.setUserId(userId);
    result.setDate(date);
    result.setNutrientStatusMap(statusMap);
    return result;
  }

  public RecommendedNutrient getRecommendedNutrientByType(String userId, NutritionType nutritionType) {
    return recommendedNutrientRepository.findByUserUserIdAndType(userId, nutritionType);
  }

  // 상태 비교 로직
  private NutritionStatus compare(Double value, Double min, Double max) {
    if (value == null) return NutritionStatus.DEFICIENT;
    if (value < min) return NutritionStatus.DEFICIENT;
    if (value > max) return NutritionStatus.EXCESS;
    return NutritionStatus.NORMAL;
  }

}
