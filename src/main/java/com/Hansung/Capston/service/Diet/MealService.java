package com.Hansung.Capston.service.Diet;

import com.Hansung.Capston.dto.Diet.Meal.MealLogRequest;
import com.Hansung.Capston.dto.Diet.Meal.MealLogResponse;
import com.Hansung.Capston.entity.Diet.Food.FoodData;
import com.Hansung.Capston.common.MealType;
import com.Hansung.Capston.entity.Diet.Meal.ImageMealLog;
import com.Hansung.Capston.entity.Diet.Meal.MealLog;
import com.Hansung.Capston.entity.Diet.Meal.SearchMealLog;
import com.Hansung.Capston.repository.Diet.Food.FoodDataRepository;
import com.Hansung.Capston.repository.Diet.Meal.ImageMealLogRepository;
import com.Hansung.Capston.repository.Diet.Meal.MealLogRepository;
import com.Hansung.Capston.repository.Diet.Meal.SearchMealLogRepository;
import com.Hansung.Capston.repository.UserInfo.UserRepository;
import com.Hansung.Capston.service.ApiService.OpenAiApiService;
import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MealService {
  private final MealLogRepository mealLogRepository;
  private final ImageMealLogRepository imageMealLogRepository;
  private final SearchMealLogRepository searchMealLogRepository;
  private final UserRepository userRepository;
  private final FoodDataRepository foodDataRepository;

  private final OpenAiApiService openAiApiService;

  @Autowired
  public MealService(MealLogRepository mealLogRepository,
      ImageMealLogRepository imageMealLogRepository,
      SearchMealLogRepository searchMealLogRepository, UserRepository userRepository,
      FoodDataRepository foodDataRepository, OpenAiApiService openAiApiService) {
    this.mealLogRepository = mealLogRepository;
    this.imageMealLogRepository = imageMealLogRepository;
    this.searchMealLogRepository = searchMealLogRepository;
    this.userRepository = userRepository;
    this.foodDataRepository = foodDataRepository;
    this.openAiApiService = openAiApiService;
  }

  // 식사 기록 불러오기
  public MealLogResponse loadMealLogs(String userId) {
    MealLogResponse res = new MealLogResponse();

    List<MealLog> mealLogs = mealLogRepository.findByUserUserId(userId);
    List<ImageMealLog> imageMealLogs = new ArrayList<>();
    List<SearchMealLog> searchMealLogs = new ArrayList<>();

    for(MealLog mealLog : mealLogs) {
      if(mealLog.getType().equals(MealType.image)) {
        ImageMealLog log = imageMealLogRepository.findByMealId(mealLog.getMealId());
        log.getMealLog().setUser(null);
        imageMealLogs.add(log);
      } else if(mealLog.getType().equals(MealType.search)) {
        SearchMealLog log = searchMealLogRepository.findByMealid(mealLog.getMealId());
        log.getMealLog().setUser(null);
        searchMealLogs.add(log);
      }
    }

    res = MealLogResponse.toDto(imageMealLogs, searchMealLogs);

    return res;
  }

  // 식사 기록 등록하기
  public MealLog saveMealLog(MealLogRequest request, String userId) {
    MealLog log = new MealLog();

    if(request.getType().equals(MealType.image)) {
      ImageMealLogRecord logs = saveImageMealLog(request);
      if(logs == null) {
        return null;
      }

      log = MealLog.builder()
          .user(userRepository.findById(userId).get())
          .type(request.getType())
          .date(request.getDateTime())
          .fat(logs.mealLog.getFat())
          .calories(logs.mealLog.getCalories())
          .protein(logs.mealLog.getProtein())
          .carbohydrate(logs.mealLog.getCarbohydrate())
          .sugar(logs.mealLog.getSugar())
          .sodium(logs.mealLog.getSodium())
          .dietaryFiber(logs.mealLog.getDietaryFiber())
          .calcium(logs.mealLog.getCalcium())
          .saturatedFat(logs.mealLog.getSaturatedFat())
          .transFat(logs.mealLog.getTransFat())
          .cholesterol(logs.mealLog.getCholesterol())
          .vitaminA(logs.mealLog.getVitaminA())
          .vitaminB1(logs.mealLog.getVitaminB1())
          .vitaminC(logs.mealLog.getVitaminC())
          .vitaminD(logs.mealLog.getVitaminD())
          .vitaminE(logs.mealLog.getVitaminE())
          .magnesium(logs.mealLog.getMagnesium())
          .zinc(logs.mealLog.getZinc())
          .lactium(logs.mealLog.getLactium())
          .potassium(logs.mealLog.getPotassium())
          .lArginine(logs.mealLog.getLArginine())
          .omega3(logs.mealLog.getOmega3())
          .build();

      MealLog savedMealLog = mealLogRepository.save(log);
      logs.imageMealLog.setMealLog(savedMealLog);
      imageMealLogRepository.save(logs.imageMealLog);
    }
    else if(request.getType().equals(MealType.search)) {
      SearchMealLogRecord logs = saveSearchMealLog(request);

      log = MealLog.builder()
          .user(userRepository.findById(userId).get())
          .type(request.getType())
          .date(request.getDateTime())
          .fat(logs.mealLog.getFat())
          .calories(logs.mealLog.getCalories())
          .protein(logs.mealLog.getProtein())
          .carbohydrate(logs.mealLog.getCarbohydrate())
          .sugar(logs.mealLog.getSugar())
          .sodium(logs.mealLog.getSodium())
          .dietaryFiber(logs.mealLog.getDietaryFiber())
          .calcium(logs.mealLog.getCalcium())
          .saturatedFat(logs.mealLog.getSaturatedFat())
          .transFat(logs.mealLog.getTransFat())
          .cholesterol(logs.mealLog.getCholesterol())
          .vitaminA(logs.mealLog.getVitaminA())
          .vitaminB1(logs.mealLog.getVitaminB1())
          .vitaminC(logs.mealLog.getVitaminC())
          .vitaminD(logs.mealLog.getVitaminD())
          .vitaminE(logs.mealLog.getVitaminE())
          .magnesium(logs.mealLog.getMagnesium())
          .zinc(logs.mealLog.getZinc())
          .lactium(logs.mealLog.getLactium())
          .potassium(logs.mealLog.getPotassium())
          .lArginine(logs.mealLog.getLArginine())
          .omega3(logs.mealLog.getOmega3())
          .build();

      MealLog savedMealLog = mealLogRepository.save(log);
      logs.searchMealLog.setMealLog(savedMealLog);
      searchMealLogRepository.save(logs.searchMealLog);
    }
    return log;
  }
  
  private ImageMealLogRecord saveImageMealLog(MealLogRequest request) {
    ImageMealLog imageMealLog = new ImageMealLog();
    MealLog mealLog;

    imageMealLog.setMealImage(request.getMealImage());
    List<String> foodNames =  openAiApiService.mealImageAnalysis(request.getMealImage());
    StringBuilder name = new StringBuilder();

    double fat = 0, calories = 0, protein = 0, carbohydrate = 0, sugar = 0, sodium = 0;
    double dietaryFiber = 0, calcium = 0, saturatedFat = 0, transFat = 0, cholesterol = 0;
    double vitaminA = 0, vitaminB1 = 0, vitaminC = 0, vitaminD = 0, vitaminE = 0;
    double magnesium = 0, zinc = 0, lactium = 0, potassium = 0, lArginine = 0, omega3 = 0;

    for(String foodName : foodNames){
      List<FoodData> foundFoods = foodDataRepository.findByFoodName(foodName);
      FoodData food;

      if (foundFoods.isEmpty()) {
        // OpenAI 호출 후 저장
        food = openAiApiService.getNutrientInfo(foodName);
        if(food == null){
          return null;
        }
        foodDataRepository.save(food);
      } else {
        food = foundFoods.get(0); // 첫 번째 항목 사용
      }

      fat += food.getFat();
      calories += food.getCalories();
      protein += food.getProtein();
      carbohydrate += food.getCarbohydrate();
      sugar += food.getSugar();
      sodium += food.getSodium();
      dietaryFiber += food.getDietaryFiber();
      calcium += food.getCalcium();
      saturatedFat += food.getSaturatedFat();
      transFat += food.getTransFat();
      cholesterol += food.getCholesterol();
      vitaminA += food.getVitaminA();
      vitaminB1 += food.getVitaminB1();
      vitaminC += food.getVitaminC();
      vitaminD += food.getVitaminD();
      vitaminE += food.getVitaminE();
      magnesium += food.getMagnesium();
      zinc += food.getZinc();
      lactium += food.getLactium();
      potassium += food.getPotassium();
      lArginine += food.getLArginine();
      omega3 += food.getOmega3();

      name.append("|").append(foodName).append("|");
    }

    imageMealLog.setMealName(name.toString());

    mealLog = MealLog.builder()
      .fat(fat)
      .calories(calories)
      .protein(protein)
      .carbohydrate(carbohydrate)
      .sugar(sugar)
      .sodium(sodium)
      .dietaryFiber(dietaryFiber)
      .calcium(calcium)
      .saturatedFat(saturatedFat)
      .transFat(transFat)
      .cholesterol(cholesterol)
      .vitaminA(vitaminA)
      .vitaminB1(vitaminB1)
      .vitaminC(vitaminC)
      .vitaminD(vitaminD)
      .vitaminE(vitaminE)
      .magnesium(magnesium)
      .zinc(zinc)
      .lactium(lactium)
      .potassium(potassium)
      .lArginine(lArginine)
      .omega3(omega3)
      .build();

    return new ImageMealLogRecord(mealLog, imageMealLog);
  }

  private SearchMealLogRecord saveSearchMealLog(MealLogRequest request) {
    SearchMealLog searchMealLog = new SearchMealLog();
    FoodData foodData = foodDataRepository.getReferenceById(request.getFoodId());
    searchMealLog.setFoodId(request.getFoodId());

    MealLog mealLog;

    mealLog = MealLog.builder()
        .fat(foodData.getFat())
        .calories(foodData.getCalories())
        .protein(foodData.getProtein())
        .carbohydrate(foodData.getCarbohydrate())
        .sugar(foodData.getSugar())
        .sodium(foodData.getSodium())
        .dietaryFiber(foodData.getDietaryFiber())
        .calcium(foodData.getCalcium())
        .saturatedFat(foodData.getSaturatedFat())
        .transFat(foodData.getTransFat())
        .cholesterol(foodData.getCholesterol())
        .vitaminA(foodData.getVitaminA())
        .vitaminB1(foodData.getVitaminB1())
        .vitaminC(foodData.getVitaminC())
        .vitaminD(foodData.getVitaminD())
        .vitaminE(foodData.getVitaminE())
        .magnesium(foodData.getMagnesium())
        .zinc(foodData.getZinc())
        .lactium(foodData.getLactium())
        .potassium(foodData.getPotassium())
        .lArginine(foodData.getLArginine())
        .omega3(foodData.getOmega3())
        .build();

    searchMealLog.setFoodImage(foodData.getFoodImage());
    searchMealLog.setFoodName(foodData.getFoodName());

    return new SearchMealLogRecord(mealLog, searchMealLog);
  }
  
  // 식사 기록 삭제하기
  public void deleteMealLog(Long mealLogId) {
    MealLog log = mealLogRepository.findById(mealLogId).get();

    if(log.getType().equals(MealType.image)){
      imageMealLogRepository.delete(imageMealLogRepository.findByMealId(log.getMealId()));
      mealLogRepository.delete(log);
    } else if(log.getType().equals(MealType.search)){
      searchMealLogRepository.delete(searchMealLogRepository.findByMealid(log.getMealId()));
      mealLogRepository.delete(log);
    }
  }

  private record ImageMealLogRecord(MealLog mealLog, ImageMealLog imageMealLog) {}
  private record SearchMealLogRecord(MealLog mealLog, SearchMealLog searchMealLog) {}

}
