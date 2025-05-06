package com.Hansung.Capston.dto;

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
  private RecommendedNutrient maxNutrient;
  private RecommendedNutrient minNutrient;
}
