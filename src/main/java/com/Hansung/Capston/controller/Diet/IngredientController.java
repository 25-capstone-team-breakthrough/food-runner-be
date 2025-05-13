package com.Hansung.Capston.controller.Diet;

import com.Hansung.Capston.dto.Diet.Ingredient.PreferredIngredientResponse;
import com.Hansung.Capston.dto.Diet.Ingredient.RecommendedIngredientResponse;
import com.Hansung.Capston.entity.Diet.Ingredient.IngredientData;
import com.Hansung.Capston.entity.Diet.Ingredient.PreferredIngredient;
import com.Hansung.Capston.entity.Diet.Ingredient.RecommendedIngredient;
import com.Hansung.Capston.service.Diet.IngredientService;
import java.io.IOException;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/diet/ingredient")
public class IngredientController {

  private final IngredientService ingredientService;

  @Autowired
  public IngredientController(IngredientService ingredientService) {
    this.ingredientService = ingredientService;
  }

  // 식재료 데이터 불러오기
  @GetMapping("/data/load")
  public ResponseEntity<List<IngredientData>> loadIngredients() {
    return ResponseEntity.ok(ingredientService.loadIngredientData());
  }

  // 식재료 즐겨찾기 등록하기
  @PostMapping("/pref/save")
  public ResponseEntity<String> savePreferredIngredient(@RequestParam("id") Long IngredientId){
    Authentication auth = SecurityContextHolder.getContext().getAuthentication();
    if (auth == null || auth.getPrincipal() == null) {
      return ResponseEntity.status(401).build();
    }
    String userId = (String) auth.getPrincipal();

    ingredientService.savePreferredIngredient(userId, IngredientId);

    return ResponseEntity.ok("save success");
  }

  // 식재료 즐겨찾기 불러오기
  @GetMapping("/pref/load")
  public ResponseEntity<List<PreferredIngredientResponse>> loadPreferredIngredients() {
    Authentication auth = SecurityContextHolder.getContext().getAuthentication();
    if (auth == null || auth.getPrincipal() == null) {
      return ResponseEntity.status(401).build();
    }
    String userId = (String) auth.getPrincipal();

    return ResponseEntity.ok(ingredientService.loadPreferredIngredients(userId));
  }

  // 식재료 즐겨찾기 삭제하기
  @PostMapping("/pref/delete")
  public ResponseEntity<String> deletePreferredIngredient(@RequestParam("pref_id") Long preferredIngredientId) {
    Authentication auth = SecurityContextHolder.getContext().getAuthentication();
    if (auth == null || auth.getPrincipal() == null) {
      return ResponseEntity.status(401).build();
    }
    String userId = (String) auth.getPrincipal();

    ingredientService.deletePreferredIngredient(preferredIngredientId);

    return ResponseEntity.ok("delete success");
  }

  // 추천 식재료 등록하기
  @PostMapping("/rec/save")
  public ResponseEntity<String> saveRecommendedIngredient() {
    Authentication auth = SecurityContextHolder.getContext().getAuthentication();
    if (auth == null || auth.getPrincipal() == null) {
      return ResponseEntity.status(401).build();
    }
    String userId = (String) auth.getPrincipal();

    ingredientService.saveRecommendedIngredient(userId);

    return ResponseEntity.ok("save success");
  }

  // 추천 식재료 불러오기
  @GetMapping("/rec/load")
  public ResponseEntity<List<RecommendedIngredientResponse>> loadRecommendedIngredients() {
    Authentication auth = SecurityContextHolder.getContext().getAuthentication();
    if (auth == null || auth.getPrincipal() == null) {
      return ResponseEntity.status(401).build();
    }
    String userId = (String) auth.getPrincipal();

    return ResponseEntity.ok(ingredientService.loadRecommendedIngredient(userId));
  }

  // csv 파일 to 음식 데이터
  @PostMapping("/data/change")
  public ResponseEntity<String> changeCsvToIngredientData(@RequestParam("file") MultipartFile file) {
    try {
      ingredientService.changeCsvToIngredientData(file);
      return ResponseEntity.ok("식재료 데이터가 성공적으로 업로드되었습니다.");
    } catch (IOException e) {
      return ResponseEntity.status(500).body("CSV 파일 처리 실패: " + e.getMessage());
    }
  }
}
