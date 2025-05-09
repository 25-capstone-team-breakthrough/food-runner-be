//package com.Hansung.Capston.controller;
//
//import com.Hansung.Capston.entity.Diet.Ingredient.IngredientData;
//import com.Hansung.Capston.repository.Diet.Meal.service.IngredientService;
//import java.util.List;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.ResponseEntity;
//import org.springframework.security.core.Authentication;
//import org.springframework.security.core.context.SecurityContextHolder;
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RestController;
//
//@RestController
//@RequestMapping("/diet/ingredient")
//public class IngredientController {
//  private final IngredientService ingredientService;
//
//  @Autowired
//  public IngredientController(IngredientService ingredientService) {
//    this.ingredientService = ingredientService;
//  }
//
//
//  @GetMapping("/recommendIngredient")
//  public ResponseEntity<List<IngredientData>> recommendedIngredient(){
//    Authentication auth = SecurityContextHolder.getContext().getAuthentication();
//    if (auth == null || auth.getPrincipal() == null) {
//      return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);  // 401 Unauthorized
//    }
//    String userId = (String) auth.getPrincipal();
//
//    List<IngredientData> ingredientDataList =ingredientService.recommendTop10Ingredients(userId);
//
//    return ResponseEntity.status(HttpStatus.OK).body(ingredientDataList);
//  }
//}
