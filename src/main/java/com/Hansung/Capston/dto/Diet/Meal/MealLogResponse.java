package com.Hansung.Capston.dto.Diet.Meal;

import com.Hansung.Capston.entity.Diet.Meal.ImageMealLog;
import com.Hansung.Capston.entity.Diet.Meal.SearchMealLog;
import java.util.ArrayList;
import java.util.List;
import lombok.Data;

@Data
public class MealLogResponse {
  List<ImageMealLogDTO> imageMealLogs;
  List<SearchMealLogDTO> searchMealLogs;

  public static MealLogResponse toDto(List<ImageMealLog> imageMealLogs, List<SearchMealLog> searchMealLogs) {
    MealLogResponse dto = new MealLogResponse();
    List<ImageMealLogDTO> imageMealLogDTOs = new ArrayList<>();
    List<SearchMealLogDTO> searchMealLogDTOs = new ArrayList<>();

    for (ImageMealLog imageMealLog : imageMealLogs) {
      ImageMealLogDTO imageMealLogDTO = ImageMealLogDTO.toDTO(imageMealLog);
      imageMealLogDTOs.add(imageMealLogDTO);
    }
    for (SearchMealLog searchMealLog : searchMealLogs) {
      SearchMealLogDTO searchMealLogDTO = SearchMealLogDTO.toDTO(searchMealLog);
      searchMealLogDTOs.add(searchMealLogDTO);
    }

    dto.setImageMealLogs(imageMealLogDTOs);
    dto.setSearchMealLogs(searchMealLogDTOs);

    return dto;
  }
}
