package com.Hansung.Capston.controller.Diet;

import com.Hansung.Capston.dto.Diet.Food.PreferredFoodResponse;
import com.Hansung.Capston.entity.Diet.Food.FoodData;
import com.Hansung.Capston.entity.Diet.Food.PreferredFood;
import com.Hansung.Capston.service.Diet.FoodService;
import java.io.IOException;
import java.util.List;
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
@RequestMapping("/diet/food")
public class FoodController {
  private final FoodService foodService;

  public FoodController(FoodService foodService) {
    this.foodService = foodService;
  }
  
  // 음식 데이터 불러오기
  @GetMapping("/data/load")
  public ResponseEntity<List<FoodData>> loadFoodData() {
    Authentication auth = SecurityContextHolder.getContext().getAuthentication();
    if (auth == null || auth.getPrincipal() == null) {
      return ResponseEntity.status(401).build();
    }
    String userId = (String) auth.getPrincipal();

    return ResponseEntity.ok(foodService.loadFoodData());
  }

  // 음식 즐겨찾기 등록하기
  @PostMapping("/pref/save")
  public ResponseEntity<String> savePreferredFood(@RequestParam(name = "food_id") Long foodId) {
    Authentication auth = SecurityContextHolder.getContext().getAuthentication();
    if (auth == null || auth.getPrincipal() == null) {
      return ResponseEntity.status(401).build();
    }
    String userId = (String) auth.getPrincipal();


    return ResponseEntity.ok(foodService.savePreferredFood(userId, foodId));
  }

  // 음식 즐겨찾기 불러오기
  @GetMapping("/pref/load")
  public ResponseEntity<List<PreferredFoodResponse>> loadPreferredFood() {
    Authentication auth = SecurityContextHolder.getContext().getAuthentication();
    if (auth == null || auth.getPrincipal() == null) {
      return ResponseEntity.status(401).build();
    }
    String userId = (String) auth.getPrincipal();

    return ResponseEntity.ok(foodService.loadPreferredFood(userId));
  }

  // 음식 즐겨찾기 삭제하기
  @PostMapping("/pref/delete")
  public ResponseEntity<String> deletePreferredFood(@RequestParam(name = "pref_id") Long preferredFoodId) {
    Authentication auth = SecurityContextHolder.getContext().getAuthentication();
    if (auth == null || auth.getPrincipal() == null) {
      return ResponseEntity.status(401).build();
    }
    String userId = (String) auth.getPrincipal();

    foodService.deletePreferredFood(preferredFoodId);

    return ResponseEntity.ok("delete success");
  }

  // csv 파일 to 음식 데이터
  @PostMapping("/data/change")
  public ResponseEntity<String> changeCsvToFoodData(@RequestParam("file") MultipartFile file) {
    try {
      // 업로드된 CSV 파일을 서버에 저장하고 처리
      foodService.changeCsvToFoodData(file);

      return ResponseEntity.ok("change success");
    } catch (IOException e) {

      return ResponseEntity.status(500).body("change fail: " + e.getMessage());
    }
  }

  @PostMapping("/data/get-one-serving")
  public ResponseEntity<String> getOneServing() {
    return ResponseEntity.ok(foodService.getOneServing());
  }
}
