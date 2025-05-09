//package com.Hansung.Capston.service;
//
//import com.Hansung.Capston.dto.MealLog.AverageNutritionDTO;
//import com.Hansung.Capston.dto.MealLog.SelectDateNutritionDTO;
//import com.Hansung.Capston.dto.Nutrition.NutritionStatusDTO;
//import com.Hansung.Capston.entity.DataSet.FoodData;
//import com.Hansung.Capston.entity.MealLog.MealLog;
//import com.Hansung.Capston.entity.NutritionLog;
//import com.Hansung.Capston.entity.NutritionStatus;
//import com.Hansung.Capston.entity.NutritionType;
//import com.Hansung.Capston.entity.RecommendedNutrient;
//import com.Hansung.Capston.entity.UserInfo.BMI;
//import com.Hansung.Capston.repository.FoodDataRepository;
//import com.Hansung.Capston.repository.MealLogRepository;
//import com.Hansung.Capston.repository.NutritionLogRepository;
//import com.Hansung.Capston.repository.RecommendedNutrientRepository;
//import com.Hansung.Capston.repository.UserInfo.UserRepository;
//import com.Hansung.Capston.service.UserInfo.BMIService;
//import java.time.LocalDate;
//import java.time.LocalDateTime;
//import java.util.ArrayList;
//import java.util.HashMap;
//import java.util.List;
//import java.util.Map;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Service;
//
//@Service
//public class NutrientService {
//  private final FoodDataRepository foodDataRepository;
//  private final NutritionLogRepository nutritionLogRepository;
//  private final RecommendedNutrientRepository recommendedNutrientRepository;
//  private final MealLogRepository mealLogRepository;
//  private final UserRepository userRepository;
//
//  private final BMIService bmiService;
//
//  @Autowired
//  public NutrientService(FoodDataRepository foodDataRepository,
//      NutritionLogRepository nutritionLogRepository,
//      RecommendedNutrientRepository recommendedNutrientRepository, MealLogRepository mealLogRepository,
//      UserRepository userRepository, BMIService bmiService) {
//    this.foodDataRepository = foodDataRepository;
//    this.nutritionLogRepository = nutritionLogRepository;
//    this.recommendedNutrientRepository = recommendedNutrientRepository;
//    this.mealLogRepository = mealLogRepository;
//    this.userRepository = userRepository;
//    this.bmiService = bmiService;
//  }
//
//
//  public List<FoodData> checkNutrientData(List<String> foods, OpenAiApiService openAiApiService) {
//    List<FoodData> foodDataDTOS = new ArrayList<>();
//
//    for (String food : foods) {
//     List<FoodData> foodDataList = foodDataRepository.findByFoodName(food);
//
//     if(foodDataList.isEmpty()) {
//       FoodData nutrientInfo = openAiApiService.getNutrientInfo(food);
//       nutrientInfo.setFoodImage("");
//       foodDataRepository.save(nutrientInfo);
//       foodDataDTOS.add(nutrientInfo);
//     } else{
//       foodDataDTOS.add(foodDataDTOS.getFirst());
//     }
//   }
//
//    return foodDataDTOS;
//  }
//
//  public void setNutrientLog(String userId, LocalDateTime date, boolean addOrDel) {
//    // 해당 날짜에 존재하는 NutritionLog 찾기
//    LocalDate onlyDate = date.toLocalDate();
//    List<NutritionLog> logs = nutritionLogRepository.findByDateOnly(userId, onlyDate);
//    NutritionLog nutrientLog;
//
//    if (logs.isEmpty()) {
//      // NutritionLog가 없으면 새로 생성
//      nutrientLog = new NutritionLog();
//      nutrientLog.setUser(userRepository.findById(userId).get());
//      nutrientLog.setDate(date); // 날짜 필드가 있다면 꼭 지정
//      // 기본값 0.0은 이미 필드에 설정되어 있으므로 건드릴 필요 없음
//      nutritionLogRepository.save(nutrientLog);
//    } else {
//      nutrientLog = logs.get(0);
//    }
//
//    // 마지막 MealLog 불러오기
//    MealLog mealLog = mealLogRepository.findByUserIdAndDateOnly(userId, date.toLocalDate()).getLast();
//
//    if (addOrDel) {
//      nutrientLog.setCalories(nutrientLog.getCalories() + mealLog.getCalories());
//      nutrientLog.setProtein(nutrientLog.getProtein() + mealLog.getProtein());
//      nutrientLog.setCarbohydrate(nutrientLog.getCarbohydrate() + mealLog.getCarbohydrate());
//      nutrientLog.setFat(nutrientLog.getFat() + mealLog.getFat());
//      nutrientLog.setSugar(nutrientLog.getSugar() + mealLog.getSugar());
//      nutrientLog.setSodium(nutrientLog.getSodium() + mealLog.getSodium());
//      nutrientLog.setDietaryFiber(nutrientLog.getDietaryFiber() + mealLog.getDietaryFiber());
//      nutrientLog.setCalcium(nutrientLog.getCalcium() + mealLog.getCalcium());
//      nutrientLog.setSaturatedFat(nutrientLog.getSaturatedFat() + mealLog.getSaturatedFat());
//      nutrientLog.setTransFat(nutrientLog.getTransFat() + mealLog.getTransFat());
//      nutrientLog.setCholesterol(nutrientLog.getCholesterol() + mealLog.getCholesterol());
//      nutrientLog.setVitaminA(nutrientLog.getVitaminA() + mealLog.getVitaminA());
//      nutrientLog.setVitaminB1(nutrientLog.getVitaminB1() + mealLog.getVitaminB1());
//      nutrientLog.setVitaminC(nutrientLog.getVitaminC() + mealLog.getVitaminC());
//      nutrientLog.setVitaminD(nutrientLog.getVitaminD() + mealLog.getVitaminD());
//      nutrientLog.setVitaminE(nutrientLog.getVitaminE() + mealLog.getVitaminE());
//      nutrientLog.setMagnesium(nutrientLog.getMagnesium() + mealLog.getMagnesium());
//      nutrientLog.setZinc(nutrientLog.getZinc() + mealLog.getZinc());
//      nutrientLog.setLactium(nutrientLog.getLactium() + mealLog.getLactium());
//      nutrientLog.setPotassium(nutrientLog.getPotassium() + mealLog.getPotassium());
//      nutrientLog.setLArginine(nutrientLog.getLArginine() + mealLog.getLArginine());
//      nutrientLog.setOmega3(nutrientLog.getOmega3() + mealLog.getOmega3());
//    } else {
//      nutrientLog.setCalories(nutrientLog.getCalories() - mealLog.getCalories());
//      nutrientLog.setProtein(nutrientLog.getProtein() - mealLog.getProtein());
//      nutrientLog.setCarbohydrate(nutrientLog.getCarbohydrate() - mealLog.getCarbohydrate());
//      nutrientLog.setFat(nutrientLog.getFat() - mealLog.getFat());
//      nutrientLog.setSugar(nutrientLog.getSugar() - mealLog.getSugar());
//      nutrientLog.setSodium(nutrientLog.getSodium() - mealLog.getSodium());
//      nutrientLog.setDietaryFiber(nutrientLog.getDietaryFiber() - mealLog.getDietaryFiber());
//      nutrientLog.setCalcium(nutrientLog.getCalcium() - mealLog.getCalcium());
//      nutrientLog.setSaturatedFat(nutrientLog.getSaturatedFat() - mealLog.getSaturatedFat());
//      nutrientLog.setTransFat(nutrientLog.getTransFat() - mealLog.getTransFat());
//      nutrientLog.setCholesterol(nutrientLog.getCholesterol() - mealLog.getCholesterol());
//      nutrientLog.setVitaminA(nutrientLog.getVitaminA() - mealLog.getVitaminA());
//      nutrientLog.setVitaminB1(nutrientLog.getVitaminB1() - mealLog.getVitaminB1());
//      nutrientLog.setVitaminC(nutrientLog.getVitaminC() - mealLog.getVitaminC());
//      nutrientLog.setVitaminD(nutrientLog.getVitaminD() - mealLog.getVitaminD());
//      nutrientLog.setVitaminE(nutrientLog.getVitaminE() - mealLog.getVitaminE());
//      nutrientLog.setMagnesium(nutrientLog.getMagnesium() - mealLog.getMagnesium());
//      nutrientLog.setZinc(nutrientLog.getZinc() - mealLog.getZinc());
//      nutrientLog.setLactium(nutrientLog.getLactium() - mealLog.getLactium());
//      nutrientLog.setPotassium(nutrientLog.getPotassium() - mealLog.getPotassium());
//      nutrientLog.setLArginine(nutrientLog.getLArginine() - mealLog.getLArginine());
//      nutrientLog.setOmega3(nutrientLog.getOmega3() - mealLog.getOmega3());
//    }
//
//    nutritionLogRepository.save(nutrientLog);
//  }
//
//  public SelectDateNutritionDTO getSelectDateNutrition(String userId, LocalDateTime date) {
//    SelectDateNutritionDTO selectDateNutritionDTO = new SelectDateNutritionDTO();
//    List<NutritionLog> logs = nutritionLogRepository.findByDateOnly(userId, date.toLocalDate());
//    NutritionLog nutritionLog;
//
//    if (logs.isEmpty()) {
//      nutritionLog = new NutritionLog();
//      nutritionLog.setUser(userRepository.findById(userId).orElseThrow(() -> new RuntimeException("User not found")));
//      nutritionLog.setDate(date);
//      nutritionLogRepository.save(nutritionLog);
//    } else {
//      nutritionLog = logs.get(0);
//    }
//
//    // NutritionLog
//    selectDateNutritionDTO.setCalories(nutritionLog.getCalories());
//    selectDateNutritionDTO.setProtein(nutritionLog.getProtein());
//    selectDateNutritionDTO.setCarbohydrate(nutritionLog.getCarbohydrate());
//    selectDateNutritionDTO.setFat(nutritionLog.getFat());
//    selectDateNutritionDTO.setSugar(nutritionLog.getSugar());
//    selectDateNutritionDTO.setSodium(nutritionLog.getSodium());
//    selectDateNutritionDTO.setDietaryFiber(nutritionLog.getDietaryFiber());
//    selectDateNutritionDTO.setCalcium(nutritionLog.getCalcium());
//    selectDateNutritionDTO.setSaturatedFat(nutritionLog.getSaturatedFat());
//    selectDateNutritionDTO.setTransFat(nutritionLog.getTransFat());
//    selectDateNutritionDTO.setCholesterol(nutritionLog.getCholesterol());
//    selectDateNutritionDTO.setVitaminA(nutritionLog.getVitaminA());
//    selectDateNutritionDTO.setVitaminB1(nutritionLog.getVitaminB1());
//    selectDateNutritionDTO.setVitaminC(nutritionLog.getVitaminC());
//    selectDateNutritionDTO.setVitaminD(nutritionLog.getVitaminD());
//    selectDateNutritionDTO.setVitaminE(nutritionLog.getVitaminE());
//    selectDateNutritionDTO.setMagnesium(nutritionLog.getMagnesium());
//    selectDateNutritionDTO.setZinc(nutritionLog.getZinc());
//    selectDateNutritionDTO.setLactium(nutritionLog.getLactium());
//    selectDateNutritionDTO.setPotassium(nutritionLog.getPotassium());
//    selectDateNutritionDTO.setLArginine(nutritionLog.getLArginine());
//    selectDateNutritionDTO.setOmega3(nutritionLog.getOmega3());
//
//    return selectDateNutritionDTO;
//  }
//
//  public AverageNutritionDTO getAverageNutrition(String userId, LocalDateTime date){
//    LocalDateTime startDate = LocalDateTime.now().minusDays(30);
//    AverageNutritionDTO dto = nutritionLogRepository.findAverageNutritionForLast30Days(userId, startDate);
//
//
//    return dto;
//  }
//
//  public void setRecommendedNutrition(String userId, NutritionType type) {
//    RecommendedNutrient recommendedNutrient = recommendedNutrientRepository
//        .findByUserIdAndType(userId, type)
//        .orElse(new RecommendedNutrient());
//    recommendedNutrient.setUser(userRepository.findById(userId).orElseThrow());
//    recommendedNutrient.setType(type);
//
//    BMI bmi = bmiService.getBMI(userId);
//    double bmr = bmiService.calculateBMR(userId);
//    double calories = 0.0; // kcal
//    double carbohydrate = 0.0; // g
//    double protein = 0.0; // g
//    double fat = 0.0;  // g
//    double sugar = 0.0; // g
//    double saturatedFat = 0.0; // g
//    double transFat = 0.0; // g
//
//    if (type == NutritionType.MAX) {
//      calories = bmr * 1.75;
//      carbohydrate = calories * 0.65 / 4;
//      protein = calories * 0.20 / 4;
//      fat = calories * 0.30 / 9;
//      sugar = calories * 0.20 / 4;
//      saturatedFat = calories * 0.07 / 9;
//      transFat = calories * 0.01 / 9;
//    } else if (type == NutritionType.MIN) {
//      calories = bmr * 1.2;
//      carbohydrate = calories * 0.55 / 4;
//      protein = calories * 0.07 / 4;
//      fat = calories * 0.15 / 9;
//      sugar = 0.0;
//      saturatedFat = 0.0;
//      transFat = 0.0;
//    }
//
//      double sodium = (type == NutritionType.MIN) ? 0 : 2000; // mg
//      double dietaryFiber = (type == NutritionType.MIN) ? ((bmi.getGender().equals("male") ) ? 23 : 13) : ((bmi.getGender().equals("male") ) ? 40 : 30); // g
//      double calcium = (type == NutritionType.MIN) ? 700 : 2500; // mg
//      double cholesterol = (type == NutritionType.MIN) ? 0 : 300.0; // mg
//      double vitaminA = (type == NutritionType.MIN) ? 600 : 3000; // ug
//      double vitaminB1 = (type == NutritionType.MIN) ? 1.1 : 5.0; // mg
//      double vitaminC = (type == NutritionType.MIN) ? 75 : 2000; // mg
//      double vitaminD = (type == NutritionType.MIN) ? 10 : 100; // ug
//      double vitaminE = (type == NutritionType.MIN) ? 7 : 540; // mg
//      double magnesium = (type == NutritionType.MIN) ? ((bmi.getGender().equals("male") ) ? 300 : 250) : 350; // mg
//      double zinc = (type == NutritionType.MIN) ? ((bmi.getGender().equals("male") ) ? 10 : 8) : 35; // mg
//      double potassium = (type == NutritionType.MIN) ? 2600 : 3600; // mg
//      double lArginine = (type == NutritionType.MIN) ? 0 : 6000; // mg
//      double omega3 = (type == NutritionType.MIN) ? 500 : 2500; // mg
//      double lactium = (type == NutritionType.MIN) ? 150 : 300; // mg
//
//    recommendedNutrient.setCalories(calories);
//    recommendedNutrient.setCarbohydrate(carbohydrate);
//    recommendedNutrient.setProtein(protein);
//    recommendedNutrient.setFat(fat);
//    recommendedNutrient.setSugar(sugar);
//    recommendedNutrient.setSaturatedFat(saturatedFat);
//    recommendedNutrient.setTransFat(transFat);
//    recommendedNutrient.setSodium(sodium);
//    recommendedNutrient.setDietaryFiber(dietaryFiber);
//    recommendedNutrient.setCalcium(calcium);
//    recommendedNutrient.setCholesterol(cholesterol);
//    recommendedNutrient.setVitaminA(vitaminA);
//    recommendedNutrient.setVitaminB1(vitaminB1);
//    recommendedNutrient.setVitaminC(vitaminC);
//    recommendedNutrient.setVitaminD(vitaminD);
//    recommendedNutrient.setVitaminE(vitaminE);
//    recommendedNutrient.setMagnesium(magnesium);
//    recommendedNutrient.setZinc(zinc);
//    recommendedNutrient.setPotassium(potassium);
//    recommendedNutrient.setLArginine(lArginine);
//    recommendedNutrient.setOmega3(omega3);
//    recommendedNutrient.setLactium(lactium);
//
//    recommendedNutrientRepository.save(recommendedNutrient);
//  }
//
//  public NutritionStatusDTO getNutritionStatusForDate(String userId, LocalDateTime date) {
//    NutritionLog log = nutritionLogRepository.findByDateOnly(userId, date.toLocalDate()).get(0);
//    RecommendedNutrient min = recommendedNutrientRepository.findByUserIdAndType(userId, NutritionType.MIN).get();
//    RecommendedNutrient max = recommendedNutrientRepository.findByUserIdAndType(userId, NutritionType.MAX).get();
//
//    Map<String, NutritionStatus> statusMap = new HashMap<>();
//
//    // 각 항목별 상태 계산
//    statusMap.put("calories", compare(log.getCalories(), min.getCalories(), max.getCalories()));
//    statusMap.put("carbohydrate", compare(log.getCarbohydrate(), min.getCarbohydrate(), max.getCarbohydrate()));
//    statusMap.put("protein", compare(log.getProtein(), min.getProtein(), max.getProtein()));
//    statusMap.put("fat", compare(log.getFat(), min.getFat(), max.getFat()));
//    statusMap.put("sugar", compare(log.getSugar(), min.getSugar(), max.getSugar()));
//    statusMap.put("sodium", compare(log.getSodium(), min.getSodium(), max.getSodium()));
//    statusMap.put("dietaryFiber", compare(log.getDietaryFiber(), min.getDietaryFiber(), max.getDietaryFiber()));
//    statusMap.put("calcium", compare(log.getCalcium(), min.getCalcium(), max.getCalcium()));
//    statusMap.put("saturatedFat", compare(log.getSaturatedFat(), min.getSaturatedFat(), max.getSaturatedFat()));
//    statusMap.put("transFat", compare(log.getTransFat(), min.getTransFat(), max.getTransFat()));
//    statusMap.put("cholesterol", compare(log.getCholesterol(), min.getCholesterol(), max.getCholesterol()));
//    statusMap.put("vitaminA", compare(log.getVitaminA(), min.getVitaminA(), max.getVitaminA()));
//    statusMap.put("vitaminB1", compare(log.getVitaminB1(), min.getVitaminB1(), max.getVitaminB1()));
//    statusMap.put("vitaminC", compare(log.getVitaminC(), min.getVitaminC(), max.getVitaminC()));
//    statusMap.put("vitaminD", compare(log.getVitaminD(), min.getVitaminD(), max.getVitaminD()));
//    statusMap.put("vitaminE", compare(log.getVitaminE(), min.getVitaminE(), max.getVitaminE()));
//    statusMap.put("magnesium", compare(log.getMagnesium(), min.getMagnesium(), max.getMagnesium()));
//    statusMap.put("zinc", compare(log.getZinc(), min.getZinc(), max.getZinc()));
//    statusMap.put("potassium", compare(log.getPotassium(), min.getPotassium(), max.getPotassium()));
//    statusMap.put("lArginine", compare(log.getLArginine(), min.getLArginine(), max.getLArginine()));
//    statusMap.put("omega3", compare(log.getOmega3(), min.getOmega3(), max.getOmega3()));
//    statusMap.put("lactium", compare(log.getLactium(), min.getLactium(), max.getLactium()));
//
//    NutritionStatusDTO result = new NutritionStatusDTO();
//    result.setNutrientStatusMap(statusMap);
//    return result;
//  }
//
//  public RecommendedNutrient getRecommendedNutrientByType(String userId, NutritionType nutritionType) {
//    return recommendedNutrientRepository.findByUserIdAndType(userId, nutritionType).get();
//  }
//
//  // 상태 비교 로직
//  private NutritionStatus compare(Double value, Double min, Double max) {
//    if (value == null) return NutritionStatus.DEFICIENT;
//    if (value < min) return NutritionStatus.DEFICIENT;
//    if (value > max) return NutritionStatus.EXCESS;
//    return NutritionStatus.NORMAL;
//  }
//
//}
