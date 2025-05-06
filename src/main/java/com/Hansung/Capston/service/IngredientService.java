package com.Hansung.Capston.service;

import com.Hansung.Capston.entity.IngredientData;
import com.Hansung.Capston.entity.PreferredIngredient;
import com.Hansung.Capston.entity.User;
import com.Hansung.Capston.repository.IngredientDataRepository;
import com.Hansung.Capston.repository.PreferredIngredientRepository;
import com.Hansung.Capston.repository.RecommendedIngredientRepository;
import com.Hansung.Capston.repository.UserRepository;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class IngredientService {
  private final IngredientDataRepository ingredientDataRepository;
  private final RecommendedIngredientRepository recommendedIngredientRepository;
  private final PreferredIngredientRepository preferredIngredientRepository;
  private final UserRepository userRepository;
  private final NutrientService nutrientService;

  @Autowired
  public IngredientService(IngredientDataRepository ingredientDataRepository,
      RecommendedIngredientRepository recommendedIngredientRepository,
      PreferredIngredientRepository preferredIngredientRepository, UserRepository userRepository,
      NutrientService nutrientService) {
    this.ingredientDataRepository = ingredientDataRepository;
    this.recommendedIngredientRepository = recommendedIngredientRepository;
    this.preferredIngredientRepository = preferredIngredientRepository;
    this.userRepository = userRepository;
    this.nutrientService = nutrientService;
  }

  public void addPreferredIngredient(String user, long ingredientId) {
    PreferredIngredient preferredIngredient = new PreferredIngredient();

    preferredIngredient.setIngredient(ingredientDataRepository.findById(ingredientId).get());
    preferredIngredient.setUser(userRepository.findById(user).get());

    preferredIngredientRepository.save(preferredIngredient);
  }

  public void removePreferredIngredient(String user, long ingredientId) {
    preferredIngredientRepository.delete(preferredIngredientRepository.findByUserIdAndIngredientId(user, ingredientId));
  }

  public void setRecommendedIngredient(String user, long ingredientId) {

  }
}
