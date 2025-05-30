package com.Hansung.Capston.service.Diet;

import com.Hansung.Capston.dto.Diet.Meal.MealLogRequest;
import com.Hansung.Capston.dto.Diet.Meal.MealLogResponse;
import com.Hansung.Capston.entity.Diet.Food.FoodData;
import com.Hansung.Capston.common.MealType;
import com.Hansung.Capston.entity.Diet.Meal.ImageMealLog;
import com.Hansung.Capston.entity.Diet.Meal.MealLog;
import com.Hansung.Capston.entity.Diet.Meal.SearchMealLog;
import com.Hansung.Capston.entity.UserInfo.User;
import com.Hansung.Capston.repository.Diet.Food.FoodDataRepository;
import com.Hansung.Capston.repository.Diet.Meal.ImageMealLogRepository;
import com.Hansung.Capston.repository.Diet.Meal.MealLogRepository;
import com.Hansung.Capston.repository.Diet.Meal.SearchMealLogRepository;
import com.Hansung.Capston.repository.UserInfo.UserRepository;
import com.Hansung.Capston.service.ApiService.AwsS3Service;
import com.Hansung.Capston.service.ApiService.OpenAiApiService;
import jakarta.persistence.EntityNotFoundException;
import java.util.ArrayList;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Slf4j
public class MealService {
  private final MealLogRepository mealLogRepository;
  private final ImageMealLogRepository imageMealLogRepository;
  private final SearchMealLogRepository searchMealLogRepository;
  private final UserRepository userRepository;
  private final FoodDataRepository foodDataRepository;

  private final OpenAiApiService openAiApiService;
  private final NutrientService nutrientService;
  private final AwsS3Service awsS3Service;
  private final IngredientService ingredientService;

  @Autowired
  public MealService(MealLogRepository mealLogRepository,
      ImageMealLogRepository imageMealLogRepository,
      SearchMealLogRepository searchMealLogRepository, UserRepository userRepository,
      FoodDataRepository foodDataRepository, OpenAiApiService openAiApiService,
      NutrientService nutrientService,
      AwsS3Service awsS3Service, IngredientService ingredientService) {
    this.mealLogRepository = mealLogRepository;
    this.imageMealLogRepository = imageMealLogRepository;
    this.searchMealLogRepository = searchMealLogRepository;
    this.userRepository = userRepository;
    this.foodDataRepository = foodDataRepository;
    this.openAiApiService = openAiApiService;
    this.nutrientService = nutrientService;
    this.awsS3Service = awsS3Service;
    this.ingredientService = ingredientService;
  }

  // 식사 기록 불러오기
  @Transactional
  public MealLogResponse loadMealLogs(String userId) {
    MealLogResponse res;

    List<MealLog> mealLogs = mealLogRepository.findByUserUserId(userId);
    List<ImageMealLog> imageMealLogs = new ArrayList<>();
    List<SearchMealLog> searchMealLogs = new ArrayList<>();

    for(MealLog mealLog : mealLogs) {
      if(mealLog.getType().equals(MealType.image)) {
        ImageMealLog log = imageMealLogRepository.findByMealId(mealLog.getMealId());
        imageMealLogs.add(log);
      } else if(mealLog.getType().equals(MealType.search)) {
        SearchMealLog log = searchMealLogRepository.findByMealid(mealLog.getMealId());
        searchMealLogs.add(log);
      }
    }

    res = MealLogResponse.toDto(imageMealLogs, searchMealLogs);

    return res;
  }

  // 식사 기록 등록하기
  @Transactional
  public MealLog saveMealLog(MealLogRequest request, String userId) {
    MealLog log = new MealLog();
    User user = userRepository.findById(userId)
        .orElseThrow(() -> new EntityNotFoundException("User not found with id: " + userId));

    log.setType(request.getType());
    log.setDate(request.getDateTime());

    if (request.getType().equals(MealType.image)) {
      ImageMealLogRecord logs = saveImageMealLog(request);
      if (logs == null) {
        return null;
      }

      copyMealLogDetails(logs.mealLog(), log);
      log.setUser(user);

      MealLog savedMealLog = mealLogRepository.save(log);
      logs.imageMealLog().setMealLog(savedMealLog);
      imageMealLogRepository.save(logs.imageMealLog());
      nutrientService.saveNutrientLogFromMealLog(userId, true, savedMealLog.getMealId());
      ingredientService.saveRecommendedIngredient(userId);
      return savedMealLog;

    } else if (request.getType().equals(MealType.search)) {
      SearchMealLogRecord logs = saveSearchMealLog(request);
      copyMealLogDetails(logs.mealLog(), log);
      log.setUser(user);

      MealLog savedMealLog = mealLogRepository.save(log);
      logs.searchMealLog().setMealLog(savedMealLog);
      searchMealLogRepository.save(logs.searchMealLog());
      nutrientService.saveNutrientLogFromMealLog(userId, true, savedMealLog.getMealId());
      ingredientService.saveRecommendedIngredient(userId);
      return savedMealLog;
    }
    return log;
  }



  @Transactional
  protected ImageMealLogRecord saveImageMealLog(MealLogRequest request) {
    ImageMealLog imageMealLog = new ImageMealLog();
    MealLog mealLog;

    imageMealLog.setMealImage(request.getMealImage());
    List<String> foods =  openAiApiService.mealImageAnalysis(request.getMealImage());
    if (foods == null) {
      return null;
    }

    StringBuilder name = new StringBuilder();

    double fat = 0, calories = 0, protein = 0, carbohydrate = 0, sugar = 0, sodium = 0;
    double dietaryFiber = 0, calcium = 0, saturatedFat = 0, transFat = 0, cholesterol = 0;
    double vitaminA = 0, vitaminB1 = 0, vitaminC = 0, vitaminD = 0, vitaminE = 0;
    double magnesium = 0, zinc = 0, lactium = 0, potassium = 0, lArginine = 0, omega3 = 0;

    for(String food : foods){

      List<FoodData> foundFoods = foodDataRepository.findByFoodName(food.split(":")[0]);
      String cleaned = food.split(":")[1].replace("g", "");
      Double gram = Double.parseDouble(cleaned)/100;
      FoodData foodData;

      if (foundFoods.isEmpty()) {
        // OpenAI 호출 후 저장
        log.info("food data 없음");
        foodData = openAiApiService.getNutrientInfo(food);
        foodData.setOneServing(openAiApiService.estimateServingGram(food));
        foodData.setFoodImage(request.getMealImage());
        foodDataRepository.save(foodData);
      } else {
        foodData = foundFoods.getFirst(); // 첫 번째 항목 사용
        log.info("food data 있음 : {}", foodData.getFoodName());
      }

      fat += foodData.getFat() * gram;
      calories += foodData.getCalories() * gram;
      protein += foodData.getProtein() * gram;
      carbohydrate += foodData.getCarbohydrate() * gram;
      sugar += foodData.getSugar() * gram;
      sodium += foodData.getSodium() * gram;
      dietaryFiber += foodData.getDietaryFiber() * gram;
      calcium += foodData.getCalcium() * gram;
      saturatedFat += foodData.getSaturatedFat() * gram;
      transFat += foodData.getTransFat() * gram;
      cholesterol += foodData.getCholesterol() * gram;
      vitaminA += foodData.getVitaminA() * gram;
      vitaminB1 += foodData.getVitaminB1() * gram;
      vitaminC += foodData.getVitaminC() * gram;
      vitaminD += foodData.getVitaminD() * gram;
      vitaminE += foodData.getVitaminE() * gram;
      magnesium += foodData.getMagnesium() * gram;
      zinc += foodData.getZinc() * gram;
      lactium += foodData.getLactium() * gram;
      potassium += foodData.getPotassium() * gram;
      lArginine += foodData.getLArginine() * gram;
      omega3 += foodData.getOmega3() * gram;

      if (!name.isEmpty()) { // 첫 번째 요소가 아닐 때만 파이프 추가
        name.append("|");
      }
      name.append(food.split(":")[0]);
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

  @Transactional
  protected SearchMealLogRecord saveSearchMealLog(MealLogRequest request) {
    SearchMealLog searchMealLog = new SearchMealLog();
    FoodData foodData = foodDataRepository.getReferenceById(request.getFoodId());
    searchMealLog.setFoodId(request.getFoodId());

    MealLog mealLog;

    double ratio = foodData.getOneServing() / 100.0;

    mealLog = MealLog.builder()
        .fat(foodData.getFat() * ratio)
        .calories(foodData.getCalories() * ratio)
        .protein(foodData.getProtein() * ratio)
        .carbohydrate(foodData.getCarbohydrate() * ratio)
        .sugar(foodData.getSugar() * ratio)
        .sodium(foodData.getSodium() * ratio)
        .dietaryFiber(foodData.getDietaryFiber() * ratio)
        .calcium(foodData.getCalcium() * ratio)
        .saturatedFat(foodData.getSaturatedFat() * ratio)
        .transFat(foodData.getTransFat() * ratio)
        .cholesterol(foodData.getCholesterol() * ratio)
        .vitaminA(foodData.getVitaminA() * ratio)
        .vitaminB1(foodData.getVitaminB1() * ratio)
        .vitaminC(foodData.getVitaminC() * ratio)
        .vitaminD(foodData.getVitaminD() * ratio)
        .vitaminE(foodData.getVitaminE() * ratio)
        .magnesium(foodData.getMagnesium() * ratio)
        .zinc(foodData.getZinc() * ratio)
        .lactium(foodData.getLactium() * ratio)
        .potassium(foodData.getPotassium() * ratio)
        .lArginine(foodData.getLArginine() * ratio)
        .omega3(foodData.getOmega3() * ratio)
        .build();

    searchMealLog.setFoodImage(foodData.getFoodImage());
    searchMealLog.setFoodName(foodData.getFoodName());

    return new SearchMealLogRecord(mealLog, searchMealLog);
  }
  
  // 식사 기록 삭제하기
  @Transactional
  public void deleteMealLog(Long mealLogId) {
    MealLog log = mealLogRepository.findById(mealLogId).get();
    nutrientService.saveNutrientLogFromMealLog(log.getUser().getUserId(), false, log.getMealId());

    if(log.getType().equals(MealType.image)){
      ImageMealLog imageMealLog = imageMealLogRepository.findByMealId(log.getMealId());
      imageMealLogRepository.delete(imageMealLog);
      awsS3Service.deleteImageFromS3(imageMealLog.getMealImage());
      mealLogRepository.delete(log);
    } else if(log.getType().equals(MealType.search)){
      searchMealLogRepository.delete(searchMealLogRepository.findByMealid(log.getMealId()));
      mealLogRepository.delete(log);
    }
  }

  private void copyMealLogDetails(MealLog source, MealLog target) {
    target.setFat(source.getFat());
    target.setCalories(source.getCalories());
    target.setProtein(source.getProtein());
    target.setCarbohydrate(source.getCarbohydrate());
    target.setSugar(source.getSugar());
    target.setSodium(source.getSodium());
    target.setDietaryFiber(source.getDietaryFiber());
    target.setCalcium(source.getCalcium());
    target.setSaturatedFat(source.getSaturatedFat());
    target.setTransFat(source.getTransFat());
    target.setCholesterol(source.getCholesterol());
    target.setVitaminA(source.getVitaminA());
    target.setVitaminB1(source.getVitaminB1());
    target.setVitaminC(source.getVitaminC());
    target.setVitaminD(source.getVitaminD());
    target.setVitaminE(source.getVitaminE());
    target.setMagnesium(source.getMagnesium());
    target.setZinc(source.getZinc());
    target.setLactium(source.getLactium());
    target.setPotassium(source.getPotassium());
    target.setLArginine(source.getLArginine());
    target.setOmega3(source.getOmega3());
  }

  private record ImageMealLogRecord(MealLog mealLog, ImageMealLog imageMealLog) {}
  private record SearchMealLogRecord(MealLog mealLog, SearchMealLog searchMealLog) {}

}
