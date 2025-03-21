package com.Hansung.Capston.controller;

import com.Hansung.Capston.dto.DietCreateDTO;
import com.Hansung.Capston.entity.FoodData;
import com.Hansung.Capston.entity.MealLog;
import com.Hansung.Capston.service.DietService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/api/diet") // 공통 URL 경로를 지정함
public class MealLogController {
  private final DietService dietService;

  public MealLogController(DietService dietService) {
    this.dietService = dietService;
  }

  @PostMapping("/create")
  public ResponseEntity<MealLog> saveMealLog(@RequestBody DietCreateDTO dietCreateDTO){
    MealLog mealLog = dietService.save(dietCreateDTO);

    return new ResponseEntity<>(mealLog, HttpStatus.OK);
  }

  @GetMapping("/data")
  public ResponseEntity<FoodData> getAllFoodData(){

  }
}