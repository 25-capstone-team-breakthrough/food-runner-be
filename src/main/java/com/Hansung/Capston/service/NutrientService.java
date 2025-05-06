package com.Hansung.Capston.service;

import com.Hansung.Capston.dto.FoodDataDTO;
import com.Hansung.Capston.dto.MealLog.AverageNutritionDTO;
import com.Hansung.Capston.dto.MealLog.SelectDateMealLogDTO;
import com.Hansung.Capston.dto.MealLog.SelectDateNutritionDTO;
import com.Hansung.Capston.entity.FoodData;
import com.Hansung.Capston.entity.MealLog;
import com.Hansung.Capston.entity.MealType;
import com.Hansung.Capston.entity.NutritionLog;
import com.Hansung.Capston.repository.FoodDataRepository;
import com.Hansung.Capston.repository.ImageMealLogRepository;
import com.Hansung.Capston.repository.MealLogRepository;
import com.Hansung.Capston.repository.NutritionLogRepository;
import com.Hansung.Capston.repository.SearchMealLogRepository;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cglib.core.Local;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestParam;

@Service
public class NutrientService {
  private final FoodDataRepository foodDataRepository;
  private final NutritionLogRepository nutritionLogRepository;
  private final MealLogRepository mealLogRepository;
  private final ImageMealLogRepository imageMealLogRepository;
  private final SearchMealLogRepository searchMealLogRepository;

  @Autowired
  public NutrientService(FoodDataRepository foodDataRepository,
      NutritionLogRepository nutritionLogRepository, MealLogRepository mealLogRepository,
      ImageMealLogRepository imageMealLogRepository,
      SearchMealLogRepository searchMealLogRepository) {
    this.foodDataRepository = foodDataRepository;
    this.nutritionLogRepository = nutritionLogRepository;
    this.mealLogRepository = mealLogRepository;
    this.imageMealLogRepository = imageMealLogRepository;
    this.searchMealLogRepository = searchMealLogRepository;
  }


  public List<FoodDataDTO> checkNutrientData(List<String> foods, OpenAiApiService openAiApiService) {
    List<FoodDataDTO> foodDataDTOS = new ArrayList<>();

    for (String food : foods) {
     List<FoodData> foodDataList = foodDataRepository.findByFoodName(food);

     if(foodDataList.isEmpty()) {
       FoodDataDTO nutrientInfo = openAiApiService.getNutrientInfo(food);
       nutrientInfo.setFoodImage("");
       foodDataRepository.save(nutrientInfo.toEntity());
       foodDataDTOS.add(nutrientInfo);
     } else{
       foodDataDTOS.add(FoodDataDTO.fromEntity(foodDataList.get(0)));
     }

   }

    return foodDataDTOS;
  }

  public void setNutrientLog(String userId, LocalDateTime date, boolean addOrDel) {

    NutritionLog nutrientLog = nutritionLogRepository.findByDateAndUserId(date,userId).get(0);

    // 마지막 meal 로그를 추가 혹은 삭제
    MealLog mealLog = mealLogRepository.findMealLogsByUserIdAndDate
        (userId,date).getLast();
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

  public SelectDateNutritionDTO getSelectDateNutrition(String userId, LocalDateTime date) {
    SelectDateNutritionDTO selectDateNutritionDTO = new SelectDateNutritionDTO();
    NutritionLog nutritionLog = nutritionLogRepository.findByDateAndUserId(date,userId).get(0);
    selectDateNutritionDTO.setSelectDate(date);

    // NutritionLog
    selectDateNutritionDTO.setCalories(nutritionLog.getCalories());
    selectDateNutritionDTO.setProtein(nutritionLog.getProtein());
    selectDateNutritionDTO.setCarbohydrate(nutritionLog.getCarbohydrate());
    selectDateNutritionDTO.setFat(nutritionLog.getFat());
    selectDateNutritionDTO.setSugar(nutritionLog.getSugar());
    selectDateNutritionDTO.setSodium(nutritionLog.getSodium());
    selectDateNutritionDTO.setDietaryFiber(nutritionLog.getDietaryFiber());
    selectDateNutritionDTO.setCalcium(nutritionLog.getCalcium());
    selectDateNutritionDTO.setSaturatedFat(nutritionLog.getSaturatedFat());
    selectDateNutritionDTO.setTransFat(nutritionLog.getTransFat());
    selectDateNutritionDTO.setCholesterol(nutritionLog.getCholesterol());
    selectDateNutritionDTO.setVitaminA(nutritionLog.getVitaminA());
    selectDateNutritionDTO.setVitaminB1(nutritionLog.getVitaminB1());
    selectDateNutritionDTO.setVitaminC(nutritionLog.getVitaminC());
    selectDateNutritionDTO.setVitaminD(nutritionLog.getVitaminD());
    selectDateNutritionDTO.setVitaminE(nutritionLog.getVitaminE());
    selectDateNutritionDTO.setMagnesium(nutritionLog.getMagnesium());
    selectDateNutritionDTO.setZinc(nutritionLog.getZinc());
    selectDateNutritionDTO.setLactium(nutritionLog.getLactium());
    selectDateNutritionDTO.setPotassium(nutritionLog.getPotassium());
    selectDateNutritionDTO.setLArginine(nutritionLog.getLArginine());
    selectDateNutritionDTO.setOmega3(nutritionLog.getOmega3());

    return selectDateNutritionDTO;
  }

  public AverageNutritionDTO getAverageNutrition(String userId, LocalDateTime date){
    AverageNutritionDTO averageNutritionDTO = new AverageNutritionDTO();

    List<NutritionLog> nutritionLogs = new ArrayList<>();


    return averageNutritionDTO;
  }
}
