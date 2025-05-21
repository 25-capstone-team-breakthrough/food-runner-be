package com.Hansung.Capston.controller.Diet;

import com.Hansung.Capston.dto.Diet.Recipe.RecommendRecipeResponse;
import com.Hansung.Capston.entity.Diet.Recipe.RecipeData;
import com.Hansung.Capston.service.Diet.RecipeService;
import java.io.IOException;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/diet/recipe")
public class RecipeController {
  private final RecipeService recipeService;

  public RecipeController(RecipeService recipeService) {
    this.recipeService = recipeService;
  }

  // 레시피 데이터 불러오기
  @GetMapping("/data/load")
  public ResponseEntity<List<RecipeData>> loadRecipeData(){
    return ResponseEntity.ok(recipeService.loadRecipeData());
  }


  @GetMapping("/rec/load")
  public ResponseEntity<List<RecommendRecipeResponse>> loadRecommendRecipe(){
    // SecurityContext에서 JWT 토큰으로 인증된 사용자 ID 추출
    Authentication auth = SecurityContextHolder.getContext().getAuthentication();
    if (auth == null || auth.getPrincipal() == null) {
      return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);  // 401 Unauthorized
    }
    String userId = (String) auth.getPrincipal();

    return ResponseEntity.ok(recipeService.loadRecommendRecipe(userId));
  }

  @PostMapping("/rec/set")
  public ResponseEntity<String> setRecommendRecipe() {
    // SecurityContext에서 JWT 토큰으로 인증된 사용자 ID 추출
    Authentication auth = SecurityContextHolder.getContext().getAuthentication();
    if (auth == null || auth.getPrincipal() == null) {
      return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);  // 401 Unauthorized
    }
    String userId = (String) auth.getPrincipal();
    recipeService.setRecommendRecipe(userId);

    return ResponseEntity.ok("대 성 공");
  }

  // 연관 레시피 설정하기
  @GetMapping("/data/relatedsave")
  public ResponseEntity<String> saveRelatedRecipeData(){
    recipeService.saveRelatedRecipeData();
    return ResponseEntity.ok("Saved");
  }

  // 푸드데이터로부터 탄단지와 칼로리 정보 가져오기
  @GetMapping("/data/from-food")
  public ResponseEntity<String> saveFromFoodData(){
    recipeService.nutritionFromFoodData();
    return ResponseEntity.ok("Saved");
  }

  // 업로드된 CSV 파일을 처리하여 DB에 저장
  @PostMapping("/data/change")
  public ResponseEntity<String> changeCsvToRecipeData(@RequestParam("file") MultipartFile file) {
    try {
      // 업로드된 CSV 파일을 서버에 저장하고 처리
      recipeService.changeCsvToRecipeData(file);

      return ResponseEntity.ok("change success");
    } catch (IOException e) {

      return ResponseEntity.status(500).body("change fail: " + e.getMessage());
    }
  }
}
