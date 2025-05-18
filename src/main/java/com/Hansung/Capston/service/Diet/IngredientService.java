package com.Hansung.Capston.service.Diet;

import com.Hansung.Capston.dto.Diet.Ingredient.PreferredIngredientResponse;
import com.Hansung.Capston.dto.Diet.Ingredient.RecommendedIngredientResponse;
import com.Hansung.Capston.entity.Diet.Ingredient.IngredientData;
import com.Hansung.Capston.entity.Diet.Ingredient.PreferredIngredient;
import com.Hansung.Capston.entity.Diet.Ingredient.RecommendedIngredient;
import com.Hansung.Capston.entity.Diet.Nutrient.NutritionLog;
import com.Hansung.Capston.entity.Diet.Nutrient.RecommendedNutrient;
import com.Hansung.Capston.repository.Diet.Ingredient.IngredientDataRepository;
import com.Hansung.Capston.repository.Diet.Ingredient.PreferredIngredientRepository;
import com.Hansung.Capston.repository.Diet.Ingredient.RecommendedIngredientRepository;
import com.Hansung.Capston.repository.Diet.Nutrition.NutritionLogRepository;
import com.Hansung.Capston.repository.Diet.Nutrition.RecommendedNutrientRepository;
import com.Hansung.Capston.repository.UserInfo.UserRepository;
import com.opencsv.bean.CsvToBeanBuilder;
import jakarta.transaction.Transactional;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class IngredientService {
  private final IngredientDataRepository ingredientDataRepository;
  private final PreferredIngredientRepository preferredIngredientRepository;
  private final RecommendedIngredientRepository recommendedIngredientRepository;
  private final UserRepository userRepository;
  private final NutritionLogRepository nutritionLogRepository;
  private final RecommendedNutrientRepository recommendedNutrientRepository;

  private final NutrientService nutrientService;

  public IngredientService(IngredientDataRepository ingredientDataRepository,
      PreferredIngredientRepository preferredIngredientRepository,
      RecommendedIngredientRepository recommendedIngredientRepository, UserRepository userRepository,
      NutritionLogRepository nutritionLogRepository,
      RecommendedNutrientRepository recommendedNutrientRepository, NutrientService nutrientService) {
    this.ingredientDataRepository = ingredientDataRepository;
    this.preferredIngredientRepository = preferredIngredientRepository;
    this.recommendedIngredientRepository = recommendedIngredientRepository;
    this.userRepository = userRepository;
    this.nutritionLogRepository = nutritionLogRepository;
    this.recommendedNutrientRepository = recommendedNutrientRepository;
    this.nutrientService = nutrientService;
  }

  // 식재료 데이터 불러오기
  public List<IngredientData> loadIngredientData() {
    return ingredientDataRepository.findAll();
  }

  // 식재료 즐겨찾기 등록하기
  public String savePreferredIngredient(String userId, Long ingredientId) {
    PreferredIngredient preferredIngredient;
    if((preferredIngredientRepository.findByUserIdAndIngredientId(userId, ingredientId)).isPresent()) {
      return "실패 : 이미 추가되어 있습니다.";
    } else {
      preferredIngredient  = new PreferredIngredient();
      preferredIngredient.setIngredient(ingredientDataRepository.findById(ingredientId).get());
      preferredIngredient.setUser(userRepository.findById(userId).get());

      preferredIngredientRepository.save(preferredIngredient);
      return "성공 : 즐겨찾기 추가";
    }
  }

  // 식재료 즐겨찾기 불러오기
  public List<PreferredIngredientResponse> loadPreferredIngredients(String userId) {
    List<PreferredIngredient> preferredIngredients = preferredIngredientRepository.findByUserUserId(userId);
    List<PreferredIngredientResponse> res = new ArrayList<>();

    for(PreferredIngredient preferredIngredient : preferredIngredients){
      res.add(PreferredIngredientResponse.toDTO(preferredIngredient));
    }

    return res;
  }

  // 식재료 즐겨찾기 삭제하기
  @Transactional
  public String deletePreferredIngredient(Long preferredIngredientId) {
    recommendedIngredientRepository.deleteById(preferredIngredientId);
    return("성공 : 즐겨찾기 삭제");
  }

  // 추천 식재료 등록하기
  @Transactional
  public void saveRecommendedIngredient(String userId) {
    NutritionLog log = nutritionLogRepository.findByUserUserId(userId).getLast();
    RecommendedNutrient rec = nutrientService.getAverageRecommendedNutrient(userId);
    List<IngredientData> ingredients = ingredientDataRepository.findAll();

    Map<String, Double> nutrientMinWeight = Map.ofEntries(
        Map.entry("protein", 8.833),
        Map.entry("carbohydrate", 3.0),
        Map.entry("fat", 0.0),
        Map.entry("dietary_fiber", 25.806),
        Map.entry("vitamin_b1", 128.571),
        Map.entry("potassium", 0.011),
        Map.entry("vitamin_c", 2.163),
        Map.entry("calcium", 0.0644),
        Map.entry("vitamin_d", 112.5),
        Map.entry("vitamin_a", 0.0049),
        Map.entry("sodium", 0.0),
        Map.entry("cholesterol", 0.0),
        Map.entry("sugar", 0.0),
        Map.entry("saturated_fat", 0.0),
        Map.entry("trans_fat", 0.0)
    );

    Map<String, Double> nutrientMaxWeight = Map.ofEntries(
        Map.entry("protein", 1.665),
        Map.entry("carbohydrate", 0.3),
        Map.entry("fat", 0.0),
        Map.entry("dietary_fiber", 5.161),
        Map.entry("vitamin_b1", 25.714),
        Map.entry("potassium", 0.0),
        Map.entry("vitamin_c", 0.821),
        Map.entry("calcium", 0.0113),
        Map.entry("vitamin_d", 22.5),
        Map.entry("vitamin_a", 0.008),
        Map.entry("sodium", 0.0),
        Map.entry("cholesterol", 0.0),
        Map.entry("sugar", 0.0),
        Map.entry("saturated_fat", 0.0),
        Map.entry("trans_fat", 0.0)
    );

    List<IngredientScore> scored = new ArrayList<>();
    for (IngredientData ing : ingredients) {
      double score = 0.0;

      for (String key : nutrientMinWeight.keySet()) {
        double intake = getNutrientValue(log, key);
        double need = getNutrientValue(rec, key);
        double ingVal = getNutrientValue(ing, key);

        double diff = intake - need;

        if (diff < 0) {
          score += ingVal * nutrientMinWeight.get(key);
        } else if (diff > 0) {
          score -= ingVal * nutrientMaxWeight.get(key);
        }
      }

      scored.add(new IngredientScore(ing, score));
    }

    scored.sort((a, b) -> Double.compare(b.score, a.score));
    List<IngredientScore> top50 = scored.subList(0, Math.min(50, scored.size()));

    Collections.shuffle(top50);
    List<IngredientScore> selected14 = top50.subList(0, Math.min(14, top50.size()));

    List<RecommendedIngredient> existingRecommendations = recommendedIngredientRepository.findAllByUserUserId(userId);
    Set<Long> existingIngredientIds = existingRecommendations.stream()
        .map(recIng -> recIng.getIngredient().getIngredientId())
        .collect(Collectors.toSet());

    List<RecommendedIngredient> toSave = new ArrayList<>();
    Set<Long> selectedIngredientIds = selected14.stream()
        .map(top -> top.ingredient.getIngredientId())
        .collect(Collectors.toSet());

    for (IngredientScore top : selected14) {
      if (!existingIngredientIds.contains(top.ingredient.getIngredientId())) {
        RecommendedIngredient recIng = new RecommendedIngredient();
        recIng.setUser(userRepository.findById(userId).get());
        recIng.setIngredient(top.ingredient);
        toSave.add(recIng);
      }
    }

    recommendedIngredientRepository.saveAll(toSave);

    List<RecommendedIngredient> toDelete = existingRecommendations.stream()
        .filter(recIng -> !selectedIngredientIds.contains(recIng.getIngredient().getIngredientId()))
        .collect(Collectors.toList());
    recommendedIngredientRepository.deleteAll(toDelete);
  }

  private double getNutrientValue(Object obj, String key) {
    try {
      Field field = obj.getClass().getDeclaredField(toCamelCase(key));
      field.setAccessible(true);
      Object val = field.get(obj);
      return val != null ? (Double) val : 0.0;
    } catch (Exception e) {
      return 0.0;
    }
  }

  private String toCamelCase(String snake) {
    String[] parts = snake.split("_");
    StringBuilder sb = new StringBuilder(parts[0]);
    for (int i = 1; i < parts.length; i++) {
      sb.append(parts[i].substring(0, 1).toUpperCase()).append(parts[i].substring(1));
    }
    return sb.toString();
  }

  private static class IngredientScore {
    IngredientData ingredient;
    double score;

    IngredientScore(IngredientData ingredient, double score) {
      this.ingredient = ingredient;
      this.score = score;
    }
  }

  // 추천 식재료 불러오기
  public List<RecommendedIngredientResponse> loadRecommendedIngredient(String userId) {
    List<RecommendedIngredient> recommendedIngredients = recommendedIngredientRepository.findByUserUserId(userId);
    List<RecommendedIngredientResponse> res = new ArrayList<>();

    for (RecommendedIngredient recommendedIngredient : recommendedIngredients) {
      res.add(RecommendedIngredientResponse.toDTO(recommendedIngredient));
    }

    return res;
  }

  // csv 파일 to 음식 데이터
  @Transactional
  public String changeCsvToIngredientData(MultipartFile file) throws IOException {
    System.out.println("파일 이름: " + file.getOriginalFilename());
    System.out.println("파일 크기: " + file.getSize());

    // MultipartFile을 읽기 위한 InputStreamReader로 변환
    InputStreamReader reader = new InputStreamReader(file.getInputStream());

    // OpenCSV 라이브러리로 CSV를 객체 리스트로 변환
    List<IngredientData> ingredientDataList = new CsvToBeanBuilder<IngredientData>(reader)
        .withType(IngredientData.class)
        .build()
        .parse();

    for (IngredientData data : ingredientDataList) {
      if (data.getCalories() == null) data.setCalories(0.0);
      if (data.getProtein() == null) data.setProtein(0.0);
      if (data.getCarbohydrate() == null) data.setCarbohydrate(0.0);
      if (data.getFat() == null) data.setFat(0.0);
      if (data.getSugar() == null) data.setSugar(0.0);
      if (data.getDietaryFiber() == null) data.setDietaryFiber(0.0);
      if (data.getCalcium() == null) data.setCalcium(0.0);
      if (data.getPotassium() == null) data.setPotassium(0.0);
      if (data.getSodium() == null) data.setSodium(0.0);
      if (data.getVitaminA() == null) data.setVitaminA(0.0);
      if (data.getVitaminB1() == null) data.setVitaminB1(0.0);
      if (data.getVitaminC() == null) data.setVitaminC(0.0);
      if (data.getVitaminD() == null) data.setVitaminD(0.0);
      if (data.getCholesterol() == null) data.setCholesterol(0.0);
      if (data.getSaturatedFat() == null) data.setSaturatedFat(0.0);
      if (data.getTransFat() == null) data.setTransFat(0.0);
      if (data.getMagnesium() == null) data.setMagnesium(0.0);
      if (data.getZinc() == null) data.setZinc(0.0);
      if (data.getLactium() == null) data.setLactium(0.0);
      if (data.getLArginine() == null) data.setLArginine(0.0);
      if (data.getOmega3() == null) data.setOmega3(0.0);
    }

    // DB에 저장
    ingredientDataRepository.saveAll(ingredientDataList);

    String firstFoodName = ingredientDataList.stream()
        .findFirst()
        .map(IngredientData::getIngredientName)
        .orElse("없음");

    return "데이터 갯수 : " + ingredientDataList.size() + "\n첫 번째 데이터 음식 명: " + firstFoodName;
  }
}