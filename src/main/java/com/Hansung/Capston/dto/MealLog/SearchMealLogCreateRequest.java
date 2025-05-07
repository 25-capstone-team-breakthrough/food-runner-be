package com.Hansung.Capston.dto.MealLog;

import com.Hansung.Capston.entity.DataSet.MealType;
import java.time.LocalDateTime;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SearchMealLogCreateRequest {
  private MealType type = MealType.search;
  private LocalDateTime date;


  private Long foodId;
}
