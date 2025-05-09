package com.Hansung.Capston.dto.Nutrition;

import com.Hansung.Capston.entity.NutritionStatus;
import java.time.LocalDateTime;
import lombok.Data;
import java.util.Map;

@Data
public class NutritionStatusDTO {
  private Map<String, NutritionStatus> nutrientStatusMap;
}
