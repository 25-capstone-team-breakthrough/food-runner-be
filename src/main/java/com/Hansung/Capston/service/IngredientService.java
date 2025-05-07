package com.Hansung.Capston.service;

import com.Hansung.Capston.dto.NutritionStatusDTO;
import com.Hansung.Capston.entity.IngredientData;
import com.Hansung.Capston.entity.NutritionStatus;
import com.Hansung.Capston.entity.PreferredIngredient;
import com.Hansung.Capston.entity.User;
import com.Hansung.Capston.repository.IngredientDataRepository;
import com.Hansung.Capston.repository.PreferredIngredientRepository;
import com.Hansung.Capston.repository.RecommendedIngredientRepository;
import com.Hansung.Capston.repository.UserRepository;
import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Random;
import java.util.Set;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class IngredientService {
  private final IngredientDataRepository ingredientDataRepository;
  private final RecommendedIngredientRepository recommendedIngredientRepository;
  private final PreferredIngredientRepository preferredIngredientRepository;
  private final UserRepository userRepository;
  private final NutrientService nutrientService;

  private static final Set<String> SUPPORTED_NUTRIENTS = Set.of(
      "calories", "protein", "carbohydrate", "fat", "sugar",
      "sodium", "dietaryFiber", "cholesterol", "vitaminA",
      "vitaminB1", "vitaminC", "vitaminD", "transFat", "saturatedFat"
  );

  private static final Set<String> MINERAL_NUTRIENTS = Set.of(
      "calcium", "magnesium", "zinc", "potassium"
  );


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

  public List<IngredientData> recommendTop10Ingredients(String userId) {
    NutritionStatusDTO dto = nutrientService.getNutritionStatusForDate(userId, LocalDateTime.now());

    Set<String> deficient = new HashSet<>();
    Set<String> excessive = new HashSet<>();
    Map<String, NutritionStatus> map = dto.getNutrientStatusMap();

    for (Map.Entry<String, NutritionStatus> entry : map.entrySet()) {
      if (entry.getValue() == NutritionStatus.DEFICIENT) deficient.add(entry.getKey());
      else if (entry.getValue() == NutritionStatus.EXCESS) excessive.add(entry.getKey());
    }

    // ash 포함 조건: magnesium, potassium, zinc 모두 부족할 때
    Set<String> ashTrigger = Set.of("magnesium", "potassium", "zinc");
    boolean includeAsh = ashTrigger.stream().allMatch(deficient::contains);
    if (includeAsh) deficient.add("ash");

    // 점수 계산
    List<IngredientData> all = ingredientDataRepository.findAll();
    Map<IngredientData, Double> scored = new HashMap<>();

    for (IngredientData i : all) {
      boolean exclude = false;

      for (String ex : excessive) {
        double value = getValueForNutrient(i, ex);
        if (value > getExcessiveThreshold(ex)) {
          exclude = true;
          break;
        }
      }
      if (exclude) continue;

      double score = 0.0;
      for (String def : deficient) {
        score += getValueForNutrient(i, def);
      }

      if (score > 0.0) {
        scored.put(i, score);
      }
    }

    return scored.entrySet().stream()
        .sorted(Map.Entry.<IngredientData, Double>comparingByValue().reversed())
        .limit(10)
        .map(Map.Entry::getKey)
        .toList();
  }

  private double getValueForNutrient(IngredientData i, String key) {
    return switch (key) {
      case "calories" -> i.getCalories();
      case "protein" -> i.getProtein();
      case "carbohydrate" -> i.getCarbohydrate();
      case "fat" -> i.getFat();
      case "sugar" -> i.getSugar();
      case "sodium" -> i.getSodium();
      case "dietaryFiber" -> i.getDietaryFiber();
      case "calcium" -> i.getCalcium();
      case "saturatedFat" -> i.getSaturatedFat();
      case "transFat" -> i.getTransFat();
      case "cholesterol" -> i.getCholesterol();
      case "vitaminA" -> i.getVitaminA();
      case "vitaminB1" -> i.getVitaminB1();
      case "vitaminC" -> i.getVitaminC();
      case "vitaminD" -> i.getVitaminD();
      case "ash", "magnesium", "potassium", "zinc" -> i.getAsh();
      default -> 0.0;
    };
  }

  private double getExcessiveThreshold(String key) {
    return switch (key) {
      case "sodium" -> 2300.0;
      case "sugar" -> 50.0;
      case "calories" -> 2800.0;
      case "fat" -> 100.0;
      case "cholesterol" -> 300.0;
      case "saturatedFat" -> 20.0;
      case "transFat" -> 2.0;
      default -> Double.MAX_VALUE; // 대부분은 제한 없음
    };
  }
}
