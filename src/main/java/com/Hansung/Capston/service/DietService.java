package com.Hansung.Capston.service;

import com.Hansung.Capston.dto.DietCreateDTO;
import com.Hansung.Capston.dto.DietCreateWindowDTO;
import com.Hansung.Capston.entity.FoodData;
import com.Hansung.Capston.entity.ImageMealLog;
import com.Hansung.Capston.entity.MealLog;
import com.Hansung.Capston.entity.MealType;
import com.Hansung.Capston.entity.NutritionLog;
import com.Hansung.Capston.entity.SearchMealLog;
import com.Hansung.Capston.entity.User;
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

  public DietCreateWindowDTO dietCreatePage(User user, LocalDateTime date) {
    DietCreateWindowDTO dietCreateWindowDTO = new DietCreateWindowDTO();
    NutritionLog nutritionLog = nutritionLogRepository.getReferenceById();

    dietCreateWindowDTO.setUserId(user.getUserId());
    dietCreateWindowDTO.setCalories(nutritionLogRepository.findById(user));


    return dietCreateWindowDTO;
  }


}
