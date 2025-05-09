package com.Hansung.Capston.service.Diet;

import com.Hansung.Capston.entity.MealLog.MealLog;
import com.Hansung.Capston.entity.NutritionLog;
import com.Hansung.Capston.entity.NutritionType;
import com.Hansung.Capston.entity.RecommendedNutrient;
import com.Hansung.Capston.entity.UserInfo.BMI;
import com.Hansung.Capston.repository.MealLogRepository;
import com.Hansung.Capston.repository.NutritionLogRepository;
import com.Hansung.Capston.repository.RecommendedNutrientRepository;
import com.Hansung.Capston.repository.UserInfo.UserRepository;
import com.Hansung.Capston.service.UserInfo.BMIService;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
  public List<NutritionLog> GetNutritionByUserId(String userId) {
    List<NutritionLog> res = new ArrayList<>();
    List<NutritionLog> logs = nutritionLogRepository.findByUserUserId(userId);
    for (NutritionLog log : logs) {
      log.setUser(null);
      res.add(log);
    }
    return res;
  }

  // 섭취 영양소 정보 업데이트 및 생성(일 단위) -- MealController에서 사용
  public void setNutrientLog(String userId, boolean addOrDel, Long mealLogId) {
    // 해당 날짜에 존재하는 NutritionLog 찾기
    MealLog mealLog = mealLogRepository.findById(mealLogId).get();
    LocalDate onlyDate = mealLog.getDate().toLocalDate();
    List<NutritionLog> logs = nutritionLogRepository.findByDateOnly(userId, onlyDate);
    NutritionLog nutrientLog;

    if (logs.isEmpty()) {
      // NutritionLog가 없으면 새로 생성
      nutrientLog = new NutritionLog();
      nutrientLog.setUser(userRepository.findById(userId).get());
      nutrientLog.setDate(mealLog.getDate()); // 날짜 필드가 있다면 꼭 지정
      // 기본값 0.0은 이미 필드에 설정되어 있으므로 건드릴 필요 없음
      nutritionLogRepository.save(nutrientLog);
    } else {
      nutrientLog = logs.get(0);
    }
    
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

  public List<RecommendedNutrient> GetRecommendedNutrients(String userId) {
    List<RecommendedNutrient> res = new ArrayList<>();
    List<RecommendedNutrient> nutrients = recommendedNutrientRepository.findByUserUserId(userId);

    for(RecommendedNutrient nutrient : nutrients) {
      nutrient.setUser(null);
      res.add(nutrient);
    }
    return res;
  }


}
