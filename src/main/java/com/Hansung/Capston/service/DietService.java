package com.Hansung.Capston.service;

import com.Hansung.Capston.dto.DietCreateDTO;
import com.Hansung.Capston.dto.DietCreateWindowDTO;
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
public class DietService {

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
  public DietService(MealLogRepository mealLogRepository,
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

  // MealLog 저장
  @Transactional
  public MealLog save(DietCreateDTO dietCreateDTO) {
    User user = userRepository.findById(dietCreateDTO.getUserId()) // user_id 외래키 참조용
        .orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없음"));

    // MealLog 저장
    MealLog mealLog = MealLog.builder()
        .user(user)
        .type(dietCreateDTO.getType())
        .calories(dietCreateDTO.getCalories())
        .protein(dietCreateDTO.getProtein())
        .carbohydrate(dietCreateDTO.getCarbohydrate())
        .fat(dietCreateDTO.getFat())
        .sugar(dietCreateDTO.getSugar())
        .sodium(dietCreateDTO.getSodium())
        .dietaryFiber(dietCreateDTO.getDietaryFiber())
        .calcium(dietCreateDTO.getCalcium())
        .saturatedFat(dietCreateDTO.getSaturatedFat())
        .transFat(dietCreateDTO.getTransFat())
        .cholesterol(dietCreateDTO.getCholesterol())
        .vitaminA(dietCreateDTO.getVitaminA())
        .vitaminB1(dietCreateDTO.getVitaminB1())
        .vitaminC(dietCreateDTO.getVitaminC())
        .vitaminD(dietCreateDTO.getVitaminD())
        .vitaminE(dietCreateDTO.getVitaminE())
        .magnesium(dietCreateDTO.getMagnesium())
        .zinc(dietCreateDTO.getZinc())
        .lactium(dietCreateDTO.getLactium())
        .potassium(dietCreateDTO.getPotassium())
        .lArginine(dietCreateDTO.getLArginine())
        .omega3(dietCreateDTO.getOmega3())
        .date(dietCreateDTO.getDate())
        .build();

    mealLog = mealLogRepository.save(mealLog);

    // type이 'image'이면 ImageMealLog 저장
    if (dietCreateDTO.getType() == MealType.IMAGE) {
      ImageMealLog imageMealLog = new ImageMealLog();
      imageMealLog.setMealLog(mealLog);
      imageMealLog.setUser(user);
      imageMealLog.setMealName(dietCreateDTO.getMealName());
      imageMealLog.setMealImage(dietCreateDTO.getMealImage());
      imageMealLogRepository.save(imageMealLog);
    }

//     type이 'search'이면 SearchMealLog 저장
    else if (dietCreateDTO.getType() == MealType.SEARCH) {
      FoodData food = foodDataRepository.findById(dietCreateDTO.getFoodId())
          .orElseThrow(() -> new IllegalArgumentException("음식을 찾을 수 없음"));

      SearchMealLog searchMealLog = new SearchMealLog();
      searchMealLog.setMealLog(mealLog);
      searchMealLog.setFoodData(food);
      searchMealLogRepository.save(searchMealLog);
    }

    return mealLog;
  }

  // DietCreate 페이지에 필요한 데이터들 POST
  public DietCreateWindowDTO dietCreatePage(User user, LocalDateTime date) {
    DietCreateWindowDTO dietCreateWindowDTO = new DietCreateWindowDTO();
    NutritionLog nutritionLog = nutritionLogRepository.findByDateAndUserId(date,user.getUserId());

    // NutritionLog
    dietCreateWindowDTO.setUserId(user.getUserId());
    dietCreateWindowDTO.setCalories(nutritionLog.getCalories());
    dietCreateWindowDTO.setProtein(nutritionLog.getProtein());
    dietCreateWindowDTO.setCarbohydrate(nutritionLog.getCarbohydrate());
    dietCreateWindowDTO.setFat(nutritionLog.getFat());
    dietCreateWindowDTO.setSugar(nutritionLog.getSugar());
    dietCreateWindowDTO.setSodium(nutritionLog.getSodium());
    dietCreateWindowDTO.setDietaryFiber(nutritionLog.getDietaryFiber());
    dietCreateWindowDTO.setCalcium(nutritionLog.getCalcium());
    dietCreateWindowDTO.setSaturatedFat(nutritionLog.getSaturatedFat());
    dietCreateWindowDTO.setTransFat(nutritionLog.getTransFat());
    dietCreateWindowDTO.setCholesterol(nutritionLog.getCholesterol());
    dietCreateWindowDTO.setVitaminA(nutritionLog.getVitaminA());
    dietCreateWindowDTO.setVitaminB1(nutritionLog.getVitaminB1());
    dietCreateWindowDTO.setVitaminC(nutritionLog.getVitaminC());
    dietCreateWindowDTO.setVitaminD(nutritionLog.getVitaminD());
    dietCreateWindowDTO.setVitaminE(nutritionLog.getVitaminE());
    dietCreateWindowDTO.setMagnesium(nutritionLog.getMagnesium());
    dietCreateWindowDTO.setZinc(nutritionLog.getZinc());
    dietCreateWindowDTO.setLactium(nutritionLog.getLactium());
    dietCreateWindowDTO.setPotassium(nutritionLog.getPotassium());
    dietCreateWindowDTO.setLArginine(nutritionLog.getLArginine());
    dietCreateWindowDTO.setOmega3(nutritionLog.getOmega3());
    dietCreateWindowDTO.setSelectDate(date);

    // 식사 내역
    List<MealLog> mealLogs = mealLogRepository.findMealLogsByUserIdAndDate(user.getUserId(), date);
    mealLogs.sort(Comparator.comparing(MealLog::getDate));

    List<Long> mealIds = new ArrayList<>();
    List<String> mealLogImage = new ArrayList<>();
    List<String> foodLogImage = new ArrayList<>();

    for (MealLog mealLog : mealLogs) {
      if(mealLog.getType() == MealType.IMAGE){
        mealLogImage.add(imageMealLogRepository.
                findByMealid(mealLog.getMealId()).getMealImage());
      }
      else if(mealLog.getType() == MealType.SEARCH) {
        foodLogImage.add(searchMealLogRepository.findByMealid(mealLog.
                        getMealId()).getFoodData().getFoodImage());
      }
      mealIds.add(mealLog.getMealId());
    }

    // 상세정보로 넘어가기위한 초석작업??
    dietCreateWindowDTO.setMealIds(mealIds);
    dietCreateWindowDTO.setMealLogImage(mealLogImage);
    dietCreateWindowDTO.setFoodLogImage(foodLogImage);

    // 선호 음식에 대한 사진
    List<String> preferredFoodNames = preferredFoodRepository.findByUserId(user.getUserId())
            .stream()
            .map(preferredFood -> preferredFood.getFoodData().getFoodImage())
            .collect(Collectors.toList());

    dietCreateWindowDTO.setPreferredFoodImage(preferredFoodNames);

    // 선호 영양제에 대한 사진
    List<String> preferredSupplementNames = preferredSupplementRepository.findbyuserId(user.getUserId())
            .stream()
            .map(preferredSupplement -> preferredSupplement.getSupplementData().getSupplementImage())
            .collect(Collectors.toList());

    dietCreateWindowDTO.setPreferredSupplementImage(preferredSupplementNames);

    // 서버에 저장된 음식 정보들(이거 대기)
    List<FoodData> foodDataList = foodDataRepository.findAll();




    return dietCreateWindowDTO;
  }


}
