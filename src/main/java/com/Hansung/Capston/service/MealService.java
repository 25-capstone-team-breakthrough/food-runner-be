package com.Hansung.Capston.service;

import com.Hansung.Capston.dto.MealLog.ImageMealLogCreateRequest;
import com.Hansung.Capston.dto.MealLog.MealLogCreateResponse;
import com.Hansung.Capston.dto.MealLog.PreferredMealAndSupDTO;
import com.Hansung.Capston.dto.MealLog.SearchMealLogCreateRequest;
import com.Hansung.Capston.dto.MealLog.SelectDateMealLogDTO;
import com.Hansung.Capston.dto.Nutrition.RecommendedNutrientDTO;
import com.Hansung.Capston.dto.Nutrition.SimpleRecNutDTO;
import com.Hansung.Capston.entity.*;
import com.Hansung.Capston.entity.DataSet.FoodData;
import com.Hansung.Capston.entity.DataSet.MealType;
import com.Hansung.Capston.entity.MealLog.ImageMealLog;
import com.Hansung.Capston.entity.MealLog.MealLog;
import com.Hansung.Capston.entity.MealLog.SearchMealLog;
import com.Hansung.Capston.entity.UserInfo.User;
import com.Hansung.Capston.repository.*;
import com.Hansung.Capston.repository.UserInfo.UserRepository;
import com.Hansung.Capston.service.UserInfo.BMIService;
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

  private final BMIService bmiService;
  private final NutrientService nutrientService;

  @Autowired
  public MealService(MealLogRepository mealLogRepository,
      ImageMealLogRepository imageMealLogRepository,
      SearchMealLogRepository searchMealLogRepository,
      UserRepository userRepository,
      FoodDataRepository foodDataRepository, SupplementDataRepository supplementDataRepository,
      NutritionLogRepository nutritionLogRepository,
      PreferredFoodRepository preferredFoodRepository,
      PreferredSupplementRepository preferredSupplementRepository, BMIService bmiService,
      NutrientService nutrientService) {
    this.mealLogRepository = mealLogRepository;
    this.imageMealLogRepository = imageMealLogRepository;
    this.searchMealLogRepository = searchMealLogRepository;
    this.userRepository = userRepository;
    this.foodDataRepository = foodDataRepository;
    this.supplementDataRepository = supplementDataRepository;
    this.nutritionLogRepository = nutritionLogRepository;
    this.preferredFoodRepository = preferredFoodRepository;
    this.preferredSupplementRepository = preferredSupplementRepository;
    this.bmiService = bmiService;
    this.nutrientService = nutrientService;
  }

  public Long getLastMealLogId() {
    return mealLogRepository.count();
  }

  // 이미지 MealLog 저장
  @Transactional
  public MealLog imageSave(ImageMealLogCreateRequest imageMealLogCreateRequest, FoodData foodData) {
    // 사용자 정보 조회
    User user = userRepository.findById(imageMealLogCreateRequest.getUserId())
        .orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없음"));

    // MealLog 저장 (영양소 데이터 추가)
    MealLog mealLog = MealLog.builder()
        .user(user)
        .type(imageMealLogCreateRequest.getType())
        .date(imageMealLogCreateRequest.getDate())
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

    // mealLog 객체를 저장
    mealLog = mealLogRepository.save(mealLog);

    // ImageMealLog 객체 설정
    ImageMealLog imageMealLog = new ImageMealLog();
    imageMealLog.setMealLog(mealLog);
    imageMealLog.setUser(user);
    imageMealLog.setMealImage(imageMealLogCreateRequest.getMealImage());
    imageMealLog.setMealName(foodData.getFoodName());
    imageMealLogRepository.save(imageMealLog);

    return mealLog;
  }


  // 검색 Meallog 저장

  @Transactional
  public MealLog searchSave(String userId, SearchMealLogCreateRequest searchMealLogCreateRequest) {
    User user = userRepository.findById(userId) // user_id 외래키 참조용
        .orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없음"));

    FoodData food = foodDataRepository.findById(searchMealLogCreateRequest.getFoodId())
        .orElseThrow(() -> new IllegalArgumentException("음식을 찾을 수 없음"));

    // MealLog 저장
    MealLog mealLog = MealLog.builder()
        .user(user)
        .type(searchMealLogCreateRequest.getType())
        .calories(food.getCalories())
        .protein(food.getProtein())
        .carbohydrate(food.getCarbohydrate())
        .fat(food.getFat())
        .sugar(food.getSugar())
        .sodium(food.getSodium())
        .dietaryFiber(food.getDietaryFiber())
        .calcium(food.getCalcium())
        .saturatedFat(food.getSaturatedFat())
        .transFat(food.getTransFat())
        .cholesterol(food.getCholesterol())
        .vitaminA(food.getVitaminA())
        .vitaminB1(food.getVitaminB1())
        .vitaminC(food.getVitaminC())
        .vitaminD(food.getVitaminD())
        .vitaminE(food.getVitaminE())
        .magnesium(food.getMagnesium())
        .zinc(food.getZinc())
        .lactium(food.getLactium())
        .potassium(food.getPotassium())
        .lArginine(food.getLArginine())
        .omega3(food.getOmega3())
        .date(searchMealLogCreateRequest.getDate())
        .build();


    SearchMealLog searchMealLog = new SearchMealLog();
    searchMealLog.setMealLog(mealLog);
    searchMealLog.setFoodData(food);
    searchMealLog.setUser(user);
    searchMealLogRepository.save(searchMealLog);



    return mealLog;
  }
  //  식사 기록 삭제
  @Transactional
  public void delete(Long mealLogId) {

    MealLog mealLog = mealLogRepository.findById(mealLogId)
        .orElseThrow(() -> new IllegalArgumentException("해당하는 식사 기록이 없습니다: " + mealLogId));

    if (mealLog.getType() == MealType.image) {
      imageMealLogRepository.delete(imageMealLogRepository.findByMealId(mealLog.getMealId()));
    } else if (mealLog.getType() == MealType.search) {
      searchMealLogRepository.delete(searchMealLogRepository.findByMealid(mealLog.getMealId()));
    }

    mealLogRepository.delete(mealLog);
  }

  private SelectDateMealLogDTO getSelectDateMealLog(String userId, LocalDateTime date) {
    List<MealLog> mealLogs = mealLogRepository.findByUserIdAndDateOnly(userId, date.toLocalDate());
    mealLogs.sort(Comparator.comparing(MealLog::getDate));

    List<Long> mealIds = new ArrayList<>();
    List<String> mealLogImage = new ArrayList<>();
    List<String> foodLogImage = new ArrayList<>();

    for (MealLog mealLog : mealLogs) {
      if (mealLog.getType() == MealType.image) {
        mealLogImage.add(imageMealLogRepository.findByMealId(mealLog.getMealId()).getMealImage());
      } else if (mealLog.getType() == MealType.search) {
        foodLogImage.add(searchMealLogRepository.findByMealid(mealLog.getMealId()).getFoodData().getFoodImage());
      }
      mealIds.add(mealLog.getMealId());
    }

    SelectDateMealLogDTO dto = new SelectDateMealLogDTO();
    dto.setMealIds(mealIds);
    dto.setMealLogImage(mealLogImage);
    dto.setFoodLogImage(foodLogImage);
    return dto;
  }


  // DietCreate 페이지에 필요한 데이터들 GET
  public MealLogCreateResponse dietCreatePage(String user, LocalDateTime date) {
    MealLogCreateResponse mealLogCreateResponse = new MealLogCreateResponse();
    SelectDateMealLogDTO selectDateMealLogDTO = getSelectDateMealLog(user, date);

    SimpleRecNutDTO max = SimpleRecNutDTO.fromEntity(nutrientService.getRecommendedNutrientByType(user,NutritionType.MAX));
    SimpleRecNutDTO min = SimpleRecNutDTO.fromEntity(nutrientService.getRecommendedNutrientByType(user,NutritionType.MIN));

    RecommendedNutrientDTO recommendedNutrientDTO = new RecommendedNutrientDTO(max, min);


    mealLogCreateResponse.setSelectDateMealLog(selectDateMealLogDTO);
    mealLogCreateResponse.setSelectDateNutrition(nutrientService.getSelectDateNutrition(user,date));
    mealLogCreateResponse.setUserId(user);
    mealLogCreateResponse.setSelectDate(date);
    mealLogCreateResponse.setRecommendationCalories(bmiService.calculateBMR(user));
    mealLogCreateResponse.setNutritionStatus(nutrientService.getNutritionStatusForDate(user,date));
    mealLogCreateResponse.setRecommendedNutrientDTO(recommendedNutrientDTO);

    return mealLogCreateResponse;
  }

  public PreferredMealAndSupDTO getPreferredMealAndSupDTO(String user) {
    PreferredMealAndSupDTO mealAndSupDTO = new PreferredMealAndSupDTO();
    // 선호 음식에 대한 사진
    List<String> preferredFoodNames = preferredFoodRepository.findByUserUserId(user)
        .stream()
        .map(preferredFood -> preferredFood.getFoodData().getFoodImage())
        .collect(Collectors.toList());

    mealAndSupDTO.setPreferredFoodImage(preferredFoodNames);

    // 선호 영양제에 대한 사진
    List<String> preferredSupplementNames = preferredSupplementRepository.findByUserUserId(user)
        .stream()
        .map(preferredSupplement -> preferredSupplement.getSupplementData().getSupplementImage())
        .collect(Collectors.toList());

    mealAndSupDTO.setPreferredSupplementImage(preferredSupplementNames);

    return mealAndSupDTO;
  }

  public MealLog getMealLog(Long mealLogId) {
    return mealLogRepository.findById(mealLogId).get();
  }

  public ImageMealLog getImageMealLog(Long mealLogId) {
    return imageMealLogRepository.findByMealId(mealLogId);
  }
}
