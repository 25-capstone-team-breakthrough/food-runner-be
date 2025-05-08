package com.Hansung.Capston.dto.Nutrition;

import com.Hansung.Capston.entity.RecommendedNutrient;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Data
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class RecommendedNutrientDTO {
  private SimpleRecNutDTO maxNutrient; // 권장 칼로리 상한선
  private SimpleRecNutDTO minNutrient; // 권장 칼로리 하한선
}
