package com.Hansung.Capston.dto;

import com.Hansung.Capston.entity.NutritionStatus;
import java.time.LocalDateTime;
import lombok.Data;
import java.util.Map;

@Data
public class NutritionStatusDTO {
  private String userId;
  private LocalDateTime date;
  private Map<String, NutritionStatus> nutrientStatusMap;
}
