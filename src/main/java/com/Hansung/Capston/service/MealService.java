package com.Hansung.Capston.service;

import com.Hansung.Capston.dto.MealLog.MealLogCreateResponse;
import com.Hansung.Capston.dto.MealLog.ImageMealLogCreateRequest;
import com.Hansung.Capston.dto.MealLog.SearchMealLogCreateRequest;
import com.Hansung.Capston.entity.*;
import com.Hansung.Capston.repository.FoodDataRepository;
import com.Hansung.Capston.repository.ImageMealLogRepository;
import com.Hansung.Capston.repository.MealLogRepository;
import com.Hansung.Capston.repository.NutritionLogRepository;
import com.Hansung.Capston.repository.PreferredFoodRepository;
import com.Hansung.Capston.repository.PreferredSupplementRepository;
import com.Hansung.Capston.repository.SearchMealLogRepository;
import com.Hansung.Capston.repository.SupplementDataRepository;
import com.Hansung.Capston.repository.UserRepository;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class MealService {

  private final MealLogRepository mealLogRepository;
  private final ImageMealLogRepository imageMealLogRepository;
  private final SearchMealLogRepository searchMealLogRepository;
  private final UserRepository userRepository;
  private final FoodDataRepository foodDataRepository;
  private final SupplementDataRepository supplementDataRepository;
  private final NutritionLogRepository nutritionLogRepository;
  private final PreferredFoodRepository preferredFoodRepository;
  private final PreferredSupplementRepository preferredSupplementRepository;

  @Autowired
  public MealService(MealLogRepository mealLogRepository,
      ImageMealLogRepository imageMealLogRepository,
      SearchMealLogRepository searchMealLogRepository,
      UserRepository userRepository,
      FoodDataRepository foodDataRepository, SupplementDataRepository supplementDataRepository,
      NutritionLogRepository nutritionLogRepository,
      PreferredFoodRepository preferredFoodRepository,
      PreferredSupplementRepository preferredSupplementRepository) {
    this.mealLogRepository = mealLogRepository;
    this.imageMealLogRepository = imageMealLogRepository;
    this.searchMealLogRepository = searchMealLogRepository;
    this.userRepository = userRepository;
    this.foodDataRepository = foodDataRepository;
    this.supplementDataRepository = supplementDataRepository;
    this.nutritionLogRepository = nutritionLogRepository;
    this.preferredFoodRepository = preferredFoodRepository;
    this.preferredSupplementRepository = preferredSupplementRepository;
  }

  // 이미지 MealLog 저장
  @Transactional
  public MealLog imageSave(ImageMealLogCreateRequest imageMealLogCreateRequest) {
    User user = userRepository.findById(imageMealLogCreateRequest.getUserId()) // user_id 외래키 참조용
        .orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없음"));

    // MealLog 저장
    MealLog mealLog = MealLog.builder()
        .user(user)
        .type(imageMealLogCreateRequest.getType())
        .calories(imageMealLogCreateRequest.getCalories())
        .protein(imageMealLogCreateRequest.getProtein())
        .carbohydrate(imageMealLogCreateRequest.getCarbohydrate())
        .fat(imageMealLogCreateRequest.getFat())
        .sugar(imageMealLogCreateRequest.getSugar())
        .sodium(imageMealLogCreateRequest.getSodium())
        .dietaryFiber(imageMealLogCreateRequest.getDietaryFiber())
        .calcium(imageMealLogCreateRequest.getCalcium())
        .saturatedFat(imageMealLogCreateRequest.getSaturatedFat())
        .transFat(imageMealLogCreateRequest.getTransFat())
        .cholesterol(imageMealLogCreateRequest.getCholesterol())
        .vitaminA(imageMealLogCreateRequest.getVitaminA())
        .vitaminB1(imageMealLogCreateRequest.getVitaminB1())
        .vitaminC(imageMealLogCreateRequest.getVitaminC())
        .vitaminD(imageMealLogCreateRequest.getVitaminD())
        .vitaminE(imageMealLogCreateRequest.getVitaminE())
        .magnesium(imageMealLogCreateRequest.getMagnesium())
        .zinc(imageMealLogCreateRequest.getZinc())
        .lactium(imageMealLogCreateRequest.getLactium())
        .potassium(imageMealLogCreateRequest.getPotassium())
        .lArginine(imageMealLogCreateRequest.getLArginine())
        .omega3(imageMealLogCreateRequest.getOmega3())
        .date(imageMealLogCreateRequest.getDate())
        .build();

    mealLog = mealLogRepository.save(mealLog);

      ImageMealLog imageMealLog = new ImageMealLog();
      imageMealLog.setMealLog(mealLog);
      imageMealLog.setUser(user);
      imageMealLog.setMealName(imageMealLogCreateRequest.getMealName());
      imageMealLog.setMealImage(imageMealLogCreateRequest.getMealImage());
      imageMealLogRepository.save(imageMealLog);



    return mealLog;
  }

  // 검색 Meallog 저장

  @Transactional
  public MealLog searchSave(String userId, SearchMealLogCreateRequest searchMealLogCreateRequest) {
    User user = userRepository.findById(userId) // user_id 외래키 참조용
        .orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없음"));

    // MealLog 저장
    MealLog mealLog = MealLog.builder()
        .user(user)
        .type(searchMealLogCreateRequest.getType())
        .calories(searchMealLogCreateRequest.getCalories())
        .protein(searchMealLogCreateRequest.getProtein())
        .carbohydrate(searchMealLogCreateRequest.getCarbohydrate())
        .fat(searchMealLogCreateRequest.getFat())
        .sugar(searchMealLogCreateRequest.getSugar())
        .sodium(searchMealLogCreateRequest.getSodium())
        .dietaryFiber(searchMealLogCreateRequest.getDietaryFiber())
        .calcium(searchMealLogCreateRequest.getCalcium())
        .saturatedFat(searchMealLogCreateRequest.getSaturatedFat())
        .transFat(searchMealLogCreateRequest.getTransFat())
        .cholesterol(searchMealLogCreateRequest.getCholesterol())
        .vitaminA(searchMealLogCreateRequest.getVitaminA())
        .vitaminB1(searchMealLogCreateRequest.getVitaminB1())
        .vitaminC(searchMealLogCreateRequest.getVitaminC())
        .vitaminD(searchMealLogCreateRequest.getVitaminD())
        .vitaminE(searchMealLogCreateRequest.getVitaminE())
        .magnesium(searchMealLogCreateRequest.getMagnesium())
        .zinc(searchMealLogCreateRequest.getZinc())
        .lactium(searchMealLogCreateRequest.getLactium())
        .potassium(searchMealLogCreateRequest.getPotassium())
        .lArginine(searchMealLogCreateRequest.getLArginine())
        .omega3(searchMealLogCreateRequest.getOmega3())
        .date(searchMealLogCreateRequest.getDate())
        .build();

    FoodData food = foodDataRepository.findById(searchMealLogCreateRequest.getFoodId())
        .orElseThrow(() -> new IllegalArgumentException("음식을 찾을 수 없음"));

    SearchMealLog searchMealLog = new SearchMealLog();
    searchMealLog.setMealLog(mealLog);
    searchMealLog.setFoodData(food);
    searchMealLog.setUser(user);
    searchMealLogRepository.save(searchMealLog);



    return mealLog;
  }
//  식사 기록 삭제
  @Transactional
  public void delete(MealLog mealLog) {
    if(mealLog.getType() == MealType.image) {
      imageMealLogRepository.delete(imageMealLogRepository.findByMealId(mealLog.getMealId()));
    } else if(mealLog.getType() == MealType.search) {
      searchMealLogRepository.delete(searchMealLogRepository.findByMealid(mealLog.getMealId()));
    }

    mealLogRepository.delete(mealLog);
  }

  // DietCreate 페이지에 필요한 데이터들 GET
  public MealLogCreateResponse dietCreatePage(String user, LocalDateTime date) {
    MealLogCreateResponse mealLogCreateResponse = new MealLogCreateResponse();
    List<NutritionLog> nutritionLogs = nutritionLogRepository.findByDateAndUserId(date,user);
    if(nutritionLogs.isEmpty()){
      return MealLogCreateResponse.empty(user,date);
    }


    NutritionLog nutritionLog = new NutritionLog();

    for (NutritionLog log : nutritionLogs) {
      nutritionLog.setCalories(nutritionLog.getCalories() + log.getCalories());
      nutritionLog.setProtein(nutritionLog.getProtein() + log.getProtein());
      nutritionLog.setCarbohydrate(nutritionLog.getCarbohydrate() + log.getCarbohydrate());
      nutritionLog.setFat(nutritionLog.getFat() + log.getFat());
      nutritionLog.setSugar(nutritionLog.getSugar() + log.getSugar());
      nutritionLog.setSodium(nutritionLog.getSodium() + log.getSodium());
      nutritionLog.setDietaryFiber(nutritionLog.getDietaryFiber() + log.getDietaryFiber());
      nutritionLog.setCalcium(nutritionLog.getCalcium() + log.getCalcium());
      nutritionLog.setSaturatedFat(nutritionLog.getSaturatedFat() + log.getSaturatedFat());
      nutritionLog.setTransFat(nutritionLog.getTransFat() + log.getTransFat());
      nutritionLog.setCholesterol(nutritionLog.getCholesterol() + log.getCholesterol());
      nutritionLog.setVitaminA(nutritionLog.getVitaminA() + log.getVitaminA());
      nutritionLog.setVitaminB1(nutritionLog.getVitaminB1() + log.getVitaminB1());
      nutritionLog.setVitaminC(nutritionLog.getVitaminC() + log.getVitaminC());
      nutritionLog.setVitaminD(nutritionLog.getVitaminD() + log.getVitaminD());
      nutritionLog.setVitaminE(nutritionLog.getVitaminE() + log.getVitaminE());
      nutritionLog.setMagnesium(nutritionLog.getMagnesium() + log.getMagnesium());
      nutritionLog.setZinc(nutritionLog.getZinc() + log.getZinc());
      nutritionLog.setLactium(nutritionLog.getLactium() + log.getLactium());
      nutritionLog.setPotassium(nutritionLog.getPotassium() + log.getPotassium());
      nutritionLog.setLArginine(nutritionLog.getLArginine() + log.getLArginine());
      nutritionLog.setOmega3(nutritionLog.getOmega3() + log.getOmega3());
    }


    // NutritionLog
    mealLogCreateResponse.setCalories(nutritionLog.getCalories());
    mealLogCreateResponse.setProtein(nutritionLog.getProtein());
    mealLogCreateResponse.setCarbohydrate(nutritionLog.getCarbohydrate());
    mealLogCreateResponse.setFat(nutritionLog.getFat());
    mealLogCreateResponse.setSugar(nutritionLog.getSugar());
    mealLogCreateResponse.setSodium(nutritionLog.getSodium());
    mealLogCreateResponse.setDietaryFiber(nutritionLog.getDietaryFiber());
    mealLogCreateResponse.setCalcium(nutritionLog.getCalcium());
    mealLogCreateResponse.setSaturatedFat(nutritionLog.getSaturatedFat());
    mealLogCreateResponse.setTransFat(nutritionLog.getTransFat());
    mealLogCreateResponse.setCholesterol(nutritionLog.getCholesterol());
    mealLogCreateResponse.setVitaminA(nutritionLog.getVitaminA());
    mealLogCreateResponse.setVitaminB1(nutritionLog.getVitaminB1());
    mealLogCreateResponse.setVitaminC(nutritionLog.getVitaminC());
    mealLogCreateResponse.setVitaminD(nutritionLog.getVitaminD());
    mealLogCreateResponse.setVitaminE(nutritionLog.getVitaminE());
    mealLogCreateResponse.setMagnesium(nutritionLog.getMagnesium());
    mealLogCreateResponse.setZinc(nutritionLog.getZinc());
    mealLogCreateResponse.setLactium(nutritionLog.getLactium());
    mealLogCreateResponse.setPotassium(nutritionLog.getPotassium());
    mealLogCreateResponse.setLArginine(nutritionLog.getLArginine());
    mealLogCreateResponse.setOmega3(nutritionLog.getOmega3());

    mealLogCreateResponse.setSelectDate(date);

    // 식사 내역
    List<MealLog> mealLogs = mealLogRepository.findMealLogsByUserIdAndDate(user, date);
    mealLogs.sort(Comparator.comparing(MealLog::getDate));

    List<Long> mealIds = new ArrayList<>();
    List<String> mealLogImage = new ArrayList<>();
    List<String> foodLogImage = new ArrayList<>();

    for (MealLog mealLog : mealLogs) {
      if(mealLog.getType() == MealType.image){
        mealLogImage.add(imageMealLogRepository.
            findByMealId(mealLog.getMealId()).getMealImage());
      }
      else if(mealLog.getType() == MealType.search) {
        foodLogImage.add(searchMealLogRepository.findByMealid(mealLog.
                        getMealId()).getFoodData().getFoodImage());
      }
      mealIds.add(mealLog.getMealId());
    }

    // 상세정보로 넘어가기위한 초석작업??
    mealLogCreateResponse.setMealIds(mealIds);
    mealLogCreateResponse.setMealLogImage(mealLogImage);
    mealLogCreateResponse.setFoodLogImage(foodLogImage);

    // 선호 음식에 대한 사진
    List<String> preferredFoodNames = preferredFoodRepository.findByUserUserId(user)
            .stream()
            .map(preferredFood -> preferredFood.getFoodData().getFoodImage())
            .collect(Collectors.toList());

    mealLogCreateResponse.setPreferredFoodImage(preferredFoodNames);

    // 선호 영양제에 대한 사진
    List<String> preferredSupplementNames = preferredSupplementRepository.findByUserUserId(user)
            .stream()
            .map(preferredSupplement -> preferredSupplement.getSupplementData().getSupplementImage())
            .collect(Collectors.toList());

    mealLogCreateResponse.setPreferredSupplementImage(preferredSupplementNames);

    return mealLogCreateResponse;
  }

}
