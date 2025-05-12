package com.Hansung.Capston.service.Diet;

import com.Hansung.Capston.entity.Diet.Meal.MealLog;
import com.Hansung.Capston.entity.Diet.Nutrient.NutritionLog;
import com.Hansung.Capston.common.NutritionType;
import com.Hansung.Capston.entity.Diet.Nutrient.RecommendedNutrient;
import com.Hansung.Capston.entity.UserInfo.BMI;
import com.Hansung.Capston.entity.UserInfo.User;
import com.Hansung.Capston.repository.Diet.Meal.MealLogRepository;
import com.Hansung.Capston.repository.Diet.Nutrition.NutritionLogRepository;
import com.Hansung.Capston.repository.Diet.Nutrition.RecommendedNutrientRepository;
import com.Hansung.Capston.repository.UserInfo.UserRepository;
import com.Hansung.Capston.service.UserInfo.BMIService;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class NutrientService {
  private final NutritionLogRepository nutritionLogRepository;
  private final RecommendedNutrientRepository recommendedNutrientRepository;
  private final UserRepository userRepository;
  private final MealLogRepository mealLogRepository;

  private final BMIService bmiService;

  @Autowired
  public NutrientService(NutritionLogRepository nutritionLogRepository,
      RecommendedNutrientRepository recommendedNutrientRepository,
      UserRepository userRepository, MealLogRepository mealLogRepository, BMIService bmiService) {
    this.nutritionLogRepository = nutritionLogRepository;
    this.recommendedNutrientRepository = recommendedNutrientRepository;
    this.userRepository = userRepository;
    this.mealLogRepository = mealLogRepository;
    this.bmiService = bmiService;
  }

  // 섭취 영양소 정보 불러오기
  @Transactional
  public List<NutritionLog> loadNutritionByUserId(String userId) {
    List<NutritionLog> logs = nutritionLogRepository.findByUserUserId(userId);
    List<NutritionLog> res = new ArrayList<>();

    if (logs.isEmpty()) {
      User user = userRepository.findById(userId)
          .orElseThrow(() -> new RuntimeException("해당 ID의 사용자가 존재하지 않습니다: " + userId));

      NutritionLog newLog = new NutritionLog();
      newLog.setUser(user);
      newLog.setDate(LocalDate.now());
      nutritionLogRepository.save(newLog);

      logs = List.of(newLog);
    }

//    for (NutritionLog log : logs) {
//      log.setUser(null); // 민감 정보 제거
//      res.add(log);
//    }

    return logs;
  }


  // 섭취 영양소 정보 업데이트 및 생성(일 단위) -- MealController에서 사용
  @Transactional
  public void saveNutrientLog(String userId, boolean addOrDel, Long mealLogId) {
    // 해당 날짜에 존재하는 NutritionLog 찾기
    MealLog mealLog = mealLogRepository.findById(mealLogId).orElseThrow(() -> new RuntimeException("MealLog not found"));
    LocalDate onlyDate = mealLog.getDate().toLocalDate();

    // NutritionLog가 존재하는지 확인
    NutritionLog existingLog = nutritionLogRepository.findByUserIdAndDate(userId, onlyDate);

    NutritionLog nutrientLog;

    if (existingLog != null) {
      nutrientLog = existingLog;  // 존재하는 로그 사용
      nutritionLogRepository.delete(existingLog);
    } else {
      User user = userRepository.findById(userId)
          .orElseThrow(() -> new RuntimeException("해당 ID의 사용자가 존재하지 않습니다: " + userId));

      nutrientLog = new NutritionLog();
      nutrientLog.setUser(user);
      nutrientLog.setDate(onlyDate);  // mealLog의 날짜를 그대로 사용
      nutritionLogRepository.save(nutrientLog);  // 새로운 NutritionLog 저장
    }

    // 영양소 정보 업데이트
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

    nutritionLogRepository.save(nutrientLog);  // 최종적으로 업데이트 및 저장
  }



  // BMI 정보를 이용해서 추천 영양소 상한 하한 구하기 -- BMIController에서 사용
  public void setRecommendedNutrition(String userId, NutritionType type, BMI bmi) {
    RecommendedNutrient recommendedNutrient = recommendedNutrientRepository
        .findByUserIdAndType(userId, type)
        .orElse(new RecommendedNutrient());
    recommendedNutrient.setUser(userRepository.findById(userId).orElseThrow());
    recommendedNutrient.setType(type);

    double bmr = bmiService.calculateBMR(userId);
    double calories = 0.0; // kcal
    double carbohydrate = 0.0; // g
    double protein = 0.0; // g
    double fat = 0.0;  // g
    double sugar = 0.0; // g
    double saturatedFat = 0.0; // g
    double transFat = 0.0; // g

    if (type == NutritionType.MAX) {
      calories = bmr * 1.75;
      carbohydrate = calories * 0.65 / 4;
      protein = calories * 0.20 / 4;
      fat = calories * 0.30 / 9;
      sugar = calories * 0.20 / 4;
      saturatedFat = calories * 0.07 / 9;
      transFat = calories * 0.01 / 9;
    } else if (type == NutritionType.MIN) {
      calories = bmr * 1.2;
      carbohydrate = calories * 0.55 / 4;
      protein = calories * 0.07 / 4;
      fat = calories * 0.15 / 9;
      sugar = 0.0;
      saturatedFat = 0.0;
      transFat = 0.0;
    }

    double sodium = (type == NutritionType.MIN) ? 0 : 2000; // mg
    double dietaryFiber = (type == NutritionType.MIN) ? ((bmi.getGender().equals("male") ) ? 23 : 13) : ((bmi.getGender().equals("male") ) ? 40 : 30); // g
    double calcium = (type == NutritionType.MIN) ? 700 : 2500; // mg
    double cholesterol = (type == NutritionType.MIN) ? 0 : 300.0; // mg
    double vitaminA = (type == NutritionType.MIN) ? 600 : 3000; // ug
    double vitaminB1 = (type == NutritionType.MIN) ? 1.1 : 5.0; // mg
    double vitaminC = (type == NutritionType.MIN) ? 75 : 2000; // mg
    double vitaminD = (type == NutritionType.MIN) ? 10 : 100; // ug
    double vitaminE = (type == NutritionType.MIN) ? 7 : 540; // mg
    double magnesium = (type == NutritionType.MIN) ? ((bmi.getGender().equals("male") ) ? 300 : 250) : 350; // mg
    double zinc = (type == NutritionType.MIN) ? ((bmi.getGender().equals("male") ) ? 10 : 8) : 35; // mg
    double potassium = (type == NutritionType.MIN) ? 2600 : 3600; // mg
    double lArginine = (type == NutritionType.MIN) ? 0 : 6000; // mg
    double omega3 = (type == NutritionType.MIN) ? 500 : 2500; // mg
    double lactium = (type == NutritionType.MIN) ? 150 : 300; // mg

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

  public List<RecommendedNutrient> loadRecommendedNutrients(String userId) {
    List<RecommendedNutrient> res = new ArrayList<>();
    List<RecommendedNutrient> nutrients = recommendedNutrientRepository.findByUserUserId(userId);

    for(RecommendedNutrient nutrient : nutrients) {
      nutrient.setUser(null);
      res.add(nutrient);
    }
    return res;
  }

  // 권장 영양소 평균 구하는 메서드
  protected RecommendedNutrient getAverageRecommendedNutrient(String userId) {
    RecommendedNutrient max = recommendedNutrientRepository.findByUserUserId(userId).get(0);
    RecommendedNutrient min = recommendedNutrientRepository.findByUserUserId(userId).get(1);

    Double averageProtein = (max.getProtein() - min.getProtein()) / 2;
    Double averageCarbohydrate = (max.getCarbohydrate() - min.getCarbohydrate()) / 2;
    Double averageFat = (max.getFat() - min.getFat()) / 2;
    Double averageDietaryFiber = (max.getDietaryFiber() - min.getDietaryFiber()) / 2;
    Double averageVitaminB1 = (max.getVitaminB1() - min.getVitaminB1()) / 2;
    Double averagePotassium = (max.getPotassium() - min.getPotassium()) / 2;
    Double averageVitaminC = (max.getVitaminC() - min.getVitaminC()) / 2;
    Double averageCalcium = (max.getCalcium() - min.getCalcium()) / 2;
    Double averageVitaminD = (max.getVitaminD() - min.getVitaminD()) / 2;
    Double averageVitaminA = (max.getVitaminA() - min.getVitaminA()) / 2;

    RecommendedNutrient averageNutrient = new RecommendedNutrient();
    averageNutrient.setProtein(averageProtein);
    averageNutrient.setCarbohydrate(averageCarbohydrate);
    averageNutrient.setFat(averageFat);
    averageNutrient.setDietaryFiber(averageDietaryFiber);
    averageNutrient.setVitaminB1(averageVitaminB1);
    averageNutrient.setPotassium(averagePotassium);
    averageNutrient.setVitaminC(averageVitaminC);
    averageNutrient.setCalcium(averageCalcium);
    averageNutrient.setVitaminD(averageVitaminD);
    averageNutrient.setVitaminA(averageVitaminA);

    // ✅ 사용자 설정 누락되면 오류 발생
    averageNutrient.setUser(userRepository.findById(userId)
        .orElseThrow(() -> new RuntimeException("해당 ID의 사용자가 존재하지 않습니다: " + userId)));

    return averageNutrient;
  }

}
