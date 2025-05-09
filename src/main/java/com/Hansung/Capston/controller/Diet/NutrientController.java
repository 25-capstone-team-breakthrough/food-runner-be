package com.Hansung.Capston.controller.Diet;

import com.Hansung.Capston.entity.Diet.Nutrient.NutritionLog;
import com.Hansung.Capston.entity.Diet.Nutrient.RecommendedNutrient;
import com.Hansung.Capston.service.Diet.NutrientService;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/diet/nutrition")
public class NutrientController {
  private final NutrientService nutrientService;

  @Autowired
  public NutrientController(NutrientService nutrientService) {
    this.nutrientService = nutrientService;
  }

  // 영양소 기록 불러오기
  @GetMapping("/log/load")
  public ResponseEntity<List<NutritionLog>> loadNutrients() {
    Authentication auth = SecurityContextHolder.getContext().getAuthentication();
    if (auth == null || auth.getPrincipal() == null) {
      return ResponseEntity.status(401).build();
    }
    String userId = (String) auth.getPrincipal();

    return ResponseEntity.ok(nutrientService.loadNutritionByUserId(userId));
  }

  // 추천 영양소 정보 불러오기
  @GetMapping("/rec/load")
  public ResponseEntity<List<RecommendedNutrient>> loadRecommendedNutrients() {
    Authentication auth = SecurityContextHolder.getContext().getAuthentication();
    if (auth == null || auth.getPrincipal() == null) {
      return ResponseEntity.status(401).build();
    }
    String userId = (String) auth.getPrincipal();

    return ResponseEntity.ok(nutrientService.loadRecommendedNutrients(userId));
  }
}
