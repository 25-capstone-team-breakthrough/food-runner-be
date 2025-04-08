package com.Hansung.Capston.service;

import com.Hansung.Capston.dto.DietCreateWindowDTO;
import com.Hansung.Capston.dto.ImageDietCreateDTO;
import com.Hansung.Capston.dto.SearchDietCreateDTO;
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
  public MealLog imageSave(ImageDietCreateDTO imageDietCreateDTO) {
    User user = userRepository.findById(imageDietCreateDTO.getUserId()) // user_id 외래키 참조용
        .orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없음"));

    // MealLog 저장
    MealLog mealLog = MealLog.builder()
        .user(user)
        .type(imageDietCreateDTO.getType())
        .calories(imageDietCreateDTO.getCalories())
        .protein(imageDietCreateDTO.getProtein())
        .carbohydrate(imageDietCreateDTO.getCarbohydrate())
        .fat(imageDietCreateDTO.getFat())
        .sugar(imageDietCreateDTO.getSugar())
        .sodium(imageDietCreateDTO.getSodium())
        .dietaryFiber(imageDietCreateDTO.getDietaryFiber())
        .calcium(imageDietCreateDTO.getCalcium())
        .saturatedFat(imageDietCreateDTO.getSaturatedFat())
        .transFat(imageDietCreateDTO.getTransFat())
        .cholesterol(imageDietCreateDTO.getCholesterol())
        .vitaminA(imageDietCreateDTO.getVitaminA())
        .vitaminB1(imageDietCreateDTO.getVitaminB1())
        .vitaminC(imageDietCreateDTO.getVitaminC())
        .vitaminD(imageDietCreateDTO.getVitaminD())
        .vitaminE(imageDietCreateDTO.getVitaminE())
        .magnesium(imageDietCreateDTO.getMagnesium())
        .zinc(imageDietCreateDTO.getZinc())
        .lactium(imageDietCreateDTO.getLactium())
        .potassium(imageDietCreateDTO.getPotassium())
        .lArginine(imageDietCreateDTO.getLArginine())
        .omega3(imageDietCreateDTO.getOmega3())
        .date(imageDietCreateDTO.getDate())
        .build();

    mealLog = mealLogRepository.save(mealLog);

      ImageMealLog imageMealLog = new ImageMealLog();
      imageMealLog.setMealLog(mealLog);
      imageMealLog.setUser(user);
      imageMealLog.setMealName(imageDietCreateDTO.getMealName());
      imageMealLog.setMealImage(imageDietCreateDTO.getMealImage());
      imageMealLogRepository.save(imageMealLog);



    return mealLog;
  }

  // 검색 Meallog 저장

  @Transactional
  public MealLog searchSave(String userId,SearchDietCreateDTO searchDietCreateDTO) {
    User user = userRepository.findById(userId) // user_id 외래키 참조용
        .orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없음"));

    // MealLog 저장
    MealLog mealLog = MealLog.builder()
        .user(user)
        .type(searchDietCreateDTO.getType())
        .calories(searchDietCreateDTO.getCalories())
        .protein(searchDietCreateDTO.getProtein())
        .carbohydrate(searchDietCreateDTO.getCarbohydrate())
        .fat(searchDietCreateDTO.getFat())
        .sugar(searchDietCreateDTO.getSugar())
        .sodium(searchDietCreateDTO.getSodium())
        .dietaryFiber(searchDietCreateDTO.getDietaryFiber())
        .calcium(searchDietCreateDTO.getCalcium())
        .saturatedFat(searchDietCreateDTO.getSaturatedFat())
        .transFat(searchDietCreateDTO.getTransFat())
        .cholesterol(searchDietCreateDTO.getCholesterol())
        .vitaminA(searchDietCreateDTO.getVitaminA())
        .vitaminB1(searchDietCreateDTO.getVitaminB1())
        .vitaminC(searchDietCreateDTO.getVitaminC())
        .vitaminD(searchDietCreateDTO.getVitaminD())
        .vitaminE(searchDietCreateDTO.getVitaminE())
        .magnesium(searchDietCreateDTO.getMagnesium())
        .zinc(searchDietCreateDTO.getZinc())
        .lactium(searchDietCreateDTO.getLactium())
        .potassium(searchDietCreateDTO.getPotassium())
        .lArginine(searchDietCreateDTO.getLArginine())
        .omega3(searchDietCreateDTO.getOmega3())
        .date(searchDietCreateDTO.getDate())
        .build();

    FoodData food = foodDataRepository.findById(searchDietCreateDTO.getFoodId())
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
  public DietCreateWindowDTO dietCreatePage(String user, LocalDateTime date) {
    DietCreateWindowDTO dietCreateWindowDTO = new DietCreateWindowDTO();
    List<NutritionLog> nutritionLogs = nutritionLogRepository.findByDateAndUserId(date,user);
    if(nutritionLogs.isEmpty()){
      return DietCreateWindowDTO.empty(user,date);
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
    List<MealLog> mealLogs = mealLogRepository.findMealLogsByUserIdAndDate(user, date);
    mealLogs.sort(Comparator.comparing(MealLog::getDate));

    List<Long> mealIds = new ArrayList<>();
    List<byte[]> mealLogImage = new ArrayList<>();
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
    dietCreateWindowDTO.setMealIds(mealIds);
    dietCreateWindowDTO.setMealLogImage(mealLogImage);
    dietCreateWindowDTO.setFoodLogImage(foodLogImage);

    // 선호 음식에 대한 사진
    List<String> preferredFoodNames = preferredFoodRepository.findByUserUserId(user)
            .stream()
            .map(preferredFood -> preferredFood.getFoodData().getFoodImage())
            .collect(Collectors.toList());

    dietCreateWindowDTO.setPreferredFoodImage(preferredFoodNames);

    // 선호 영양제에 대한 사진
    List<String> preferredSupplementNames = preferredSupplementRepository.findByUserUserId(user)
            .stream()
            .map(preferredSupplement -> preferredSupplement.getSupplementData().getSupplementImage())
            .collect(Collectors.toList());

    dietCreateWindowDTO.setPreferredSupplementImage(preferredSupplementNames);

    return dietCreateWindowDTO;
  }

}
