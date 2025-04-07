package com.Hansung.Capston.dto;

import java.util.List;

public class ResisterPreferredIngredientResponse {
  private String userId;
  private List<IngredientDataDTO> ingredients;
  private List<String> recommendedIngredientIds;
}