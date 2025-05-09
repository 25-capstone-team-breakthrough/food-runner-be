package com.Hansung.Capston.controller.Diet;

import com.Hansung.Capston.dto.Diet.Meal.MealLogRequest;
import com.Hansung.Capston.dto.Diet.Meal.MealLogResponse;
import com.Hansung.Capston.service.Diet.MealService;
import com.Hansung.Capston.service.Diet.NutrientService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/diet/meal")
public class MealController {
  private final MealService mealService;
  private final NutrientService nutrientService;

  public MealController(MealService mealService, NutrientService nutrientService) {
    this.mealService = mealService;
    this.nutrientService = nutrientService;
  }

  // 식사 기록 저장
  @PostMapping("/log/save")
  public ResponseEntity<String> saveMealLog(@RequestBody MealLogRequest request) {
    Authentication auth = SecurityContextHolder.getContext().getAuthentication();
    if (auth == null || auth.getPrincipal() == null) {
      return ResponseEntity.status(401).build();
    }
    String userId = (String) auth.getPrincipal();

    nutrientService.saveNutrientLog(userId, true, mealService.saveMealLog(request, userId).getMealId());

    return ResponseEntity.ok("save success");
  }


  // 식사 기록 검색
  @GetMapping("/log/load")
  public ResponseEntity<MealLogResponse> loadMealLog() {
    Authentication auth = SecurityContextHolder.getContext().getAuthentication();
    if (auth == null || auth.getPrincipal() == null) {
      return ResponseEntity.status(401).build();
    }
    String userId = (String) auth.getPrincipal();

    MealLogResponse res = mealService.loadMealLogs(userId);

    return ResponseEntity.ok(res);
  }

  // 식사 기록 삭제
  @PostMapping("/log/delete")
  public ResponseEntity<String> deleteMealLog(@RequestParam(name = "log_id") Long logId) {
    Authentication auth = SecurityContextHolder.getContext().getAuthentication();
    if (auth == null || auth.getPrincipal() == null) {
      return ResponseEntity.status(401).build();
    }
    String userId = (String) auth.getPrincipal();

    nutrientService.saveNutrientLog(userId, false, logId);
    mealService.deleteMealLog(logId);

    return ResponseEntity.ok("delete success");
  }

}
