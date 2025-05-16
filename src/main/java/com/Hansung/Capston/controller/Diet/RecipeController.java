package com.Hansung.Capston.controller.Diet;

import com.Hansung.Capston.entity.Diet.Recipe.RecipeData;
import com.Hansung.Capston.service.Diet.RecipeService;
import java.io.IOException;
import java.util.List;
import org.springframework.http.ResponseEntity;
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
