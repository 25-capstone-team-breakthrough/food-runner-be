package com.Hansung.Capston.service;

import com.Hansung.Capston.repository.IngredientDataRepository;
import com.Hansung.Capston.repository.NutritionLogRepository;
import com.Hansung.Capston.repository.PreferredIngredientRepository;
import com.Hansung.Capston.repository.RecommendedIngredientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DietService {
  private final NutrientService nutrientService;
  private final IngredientDataRepository ingredientDataRepository;
  private final PreferredIngredientRepository preferredIngredientRepository;
  private final RecommendedIngredientRepository recommendedIngredientRepository;

  @Autowired
  public DietService(NutrientService nutrientService,
      IngredientDataRepository ingredientDataRepository,
      PreferredIngredientRepository preferredIngredientRepository,
      RecommendedIngredientRepository recommendedIngredientRepository) {
    this.nutrientService = nutrientService;
    this.ingredientDataRepository = ingredientDataRepository;
    this.preferredIngredientRepository = preferredIngredientRepository;
    this.recommendedIngredientRepository = recommendedIngredientRepository;
  }

  
}
