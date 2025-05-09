//package com.Hansung.Capston.service;
//
//import com.Hansung.Capston.dto.Nutrition.NutritionStatusDTO;
//import com.Hansung.Capston.entity.DataSet.IngredientData;
//import com.Hansung.Capston.entity.NutritionStatus;
//import com.Hansung.Capston.entity.PreferredIngredient;
//import com.Hansung.Capston.repository.IngredientDataRepository;
//import com.Hansung.Capston.repository.PreferredIngredientRepository;
//import com.Hansung.Capston.repository.RecommendedIngredientRepository;
//import com.Hansung.Capston.repository.UserInfo.UserRepository;
//import com.Hansung.Capston.service.Diet.NutrientService;
//import java.time.LocalDateTime;
//import java.util.ArrayList;
//import java.util.Collections;
//import java.util.HashMap;
//import java.util.HashSet;
//import java.util.List;
//import java.util.Map;
//import java.util.Set;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Service;
//
//@Service
//public class IngredientService {
//  private final IngredientDataRepository ingredientDataRepository;
//  private final RecommendedIngredientRepository recommendedIngredientRepository;
//  private final PreferredIngredientRepository preferredIngredientRepository;
//  private final UserRepository userRepository;
//  private final NutrientService nutrientService;
//
//  private static final Map<String, Double> NUTRIENT_WEIGHTS = Map.ofEntries(
//      Map.entry("protein", 33.3),
//      Map.entry("carbohydrate", 15.0),
//      Map.entry("fat", 15.0),
//      Map.entry("dietary_fiber", 10.0),
//      Map.entry("vitamin_b1", 100.0),
//      Map.entry("potassium", 0.04),
//      Map.entry("vitamin_c", 0.3),
//      Map.entry("calcium", 0.03),
//      Map.entry("vitamin_d", 30.0),
//      Map.entry("vitamin_a", 0.002),
//      Map.entry("sodium", 0.0),
//      Map.entry("cholesterol", 0.0),
//      Map.entry("sugar", 0.0),
//      Map.entry("saturated_fat", 0.0),
//      Map.entry("trans_fat", 0.0)
//  );
//
//
//
//  @Autowired
//  public IngredientService(IngredientDataRepository ingredientDataRepository,
//      RecommendedIngredientRepository recommendedIngredientRepository,
//      PreferredIngredientRepository preferredIngredientRepository, UserRepository userRepository,
//      NutrientService nutrientService) {
//    this.ingredientDataRepository = ingredientDataRepository;
//    this.recommendedIngredientRepository = recommendedIngredientRepository;
//    this.preferredIngredientRepository = preferredIngredientRepository;
//    this.userRepository = userRepository;
//    this.nutrientService = nutrientService;
//  }
//
//  public void addPreferredIngredient(String user, long ingredientId) {
//    PreferredIngredient preferredIngredient = new PreferredIngredient();
//
//    preferredIngredient.setIngredient(ingredientDataRepository.findById(ingredientId).get());
//    preferredIngredient.setUser(userRepository.findById(user).get());
//
//    preferredIngredientRepository.save(preferredIngredient);
//  }
//
//  public void removePreferredIngredient(String user, long ingredientId) {
//    preferredIngredientRepository.delete(preferredIngredientRepository.findByUserIdAndIngredientId(user, ingredientId));
//  }
//
//  public List<IngredientData> recommendTop10Ingredients(String userId) {
//    NutritionStatusDTO dto = nutrientService.GetNutritionByUserId(userId, LocalDateTime.now());
//
//    Set<String> deficient = new HashSet<>();
//    Set<String> excessive = new HashSet<>();
//    Map<String, NutritionStatus> map = dto.getNutrientStatusMap();
//
//    for (Map.Entry<String, NutritionStatus> entry : map.entrySet()) {
//      if (entry.getValue() == NutritionStatus.DEFICIENT) deficient.add(entry.getKey());
//      else if (entry.getValue() == NutritionStatus.EXCESS) excessive.add(entry.getKey());
//    }
//
//    List<IngredientData> all = ingredientDataRepository.findAll();
//    Map<IngredientData, Double> scored = new HashMap<>();
//
//    for (IngredientData i : all) {
//      boolean exclude = false;
//
//      for (String ex : excessive) {
//        double value = getValueForNutrient(i, ex);
//        if (value > getExcessiveThreshold(ex)) {
//          exclude = true;
//          break;
//        }
//      }
//      if (exclude) continue;
//
//      double score = 0.0;
//      for (String def : deficient) {
//        score += getValueForNutrient(i, def);
//      }
//
//      if (score > 0.0) {
//        scored.put(i, score);
//      }
//    }
//
//    List<IngredientData> topCandidates = new ArrayList<>(
//        scored.entrySet().stream()
//            .sorted(Map.Entry.<IngredientData, Double>comparingByValue().reversed())
//            .limit(50)
//            .map(Map.Entry::getKey)
//            .toList()
//    );
//
//    Collections.shuffle(topCandidates);
//    return topCandidates.stream().limit(10).toList();
//
//  }
//
//  private double getValueForNutrient(IngredientData i, String key) {
//    double weight = NUTRIENT_WEIGHTS.getOrDefault(key, 0.0);
//    double rawValue = switch (key) {
//      case "protein" -> i.getProtein();
//      case "carbohydrate" -> i.getCarbohydrate();
//      case "fat" -> i.getFat();
//      case "sugar" -> i.getSugar();
//      case "sodium" -> i.getSodium();
//      case "dietary_fiber" -> i.getDietaryFiber();
//      case "calcium" -> i.getCalcium();
//      case "saturated_fat" -> i.getSaturatedFat();
//      case "trans_fat" -> i.getTransFat();
//      case "cholesterol" -> i.getCholesterol();
//      case "vitamin_a" -> i.getVitaminA();
//      case "vitamin_b1" -> i.getVitaminB1();
//      case "vitamin_c" -> i.getVitaminC();
//      case "vitamin_d" -> i.getVitaminD();
//      case "potassium" -> i.getPotassium();
//      default -> 0.0;
//    };
//    return rawValue * weight;
//  }
//
//
//  private double getExcessiveThreshold(String key) {
//    // 대부분은 제한 없음
//    return Double.MAX_VALUE;
//  }
//}
