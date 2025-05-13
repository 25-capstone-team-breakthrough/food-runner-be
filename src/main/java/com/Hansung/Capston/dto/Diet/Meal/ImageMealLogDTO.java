package com.Hansung.Capston.dto.Diet.Meal;

import com.Hansung.Capston.entity.Diet.Meal.ImageMealLog;
import com.Hansung.Capston.entity.Diet.Meal.MealLog;
import lombok.Data;

@Data
public class ImageMealLogDTO {
  private Long imageMealLogId;
  private MealLogDTO mealLog;
  private String mealName;
  private String mealImage;

  public static ImageMealLogDTO toDTO(ImageMealLog imageMealLog) {
    ImageMealLogDTO imageMealLogDTO = new ImageMealLogDTO();
    imageMealLogDTO.setImageMealLogId(imageMealLogDTO.getImageMealLogId());
    imageMealLogDTO.setMealLog(MealLogDTO.toDTO(imageMealLog.getMealLog()));
    imageMealLogDTO.setMealName(imageMealLog.getMealName());
    imageMealLogDTO.setMealImage(imageMealLog.getMealImage());

    return imageMealLogDTO;
  }
}
