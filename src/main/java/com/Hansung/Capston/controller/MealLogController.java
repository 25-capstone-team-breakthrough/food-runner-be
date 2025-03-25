package com.Hansung.Capston.controller;

import com.Hansung.Capston.dto.DietCreateDTO;
import com.Hansung.Capston.dto.DietCreateWindowDTO;
import com.Hansung.Capston.dto.FoodDataResponse;
import com.Hansung.Capston.dto.SupplementDataResponse;
import com.Hansung.Capston.entity.FoodData;
import com.Hansung.Capston.entity.MealLog;
import com.Hansung.Capston.entity.SupplementData;
import com.Hansung.Capston.service.DietService;
import java.time.LocalDateTime;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/api/diet") // 공통 URL 경로를 지정함
public class MealLogController {
  private final DietService dietService;

  @Autowired
  public MealLogController(DietService dietService) {
    this.dietService = dietService;
  }

  @PostMapping("/loggingMeal")
  public ResponseEntity<MealLog> saveMealLog( @RequestBody DietCreateDTO dietCreateDTO){
    MealLog mealLog = dietService.save(dietCreateDTO);

    return new ResponseEntity<>(mealLog, HttpStatus.OK);
  }

  @GetMapping("/main")
  public ResponseEntity<DietCreateWindowDTO> getGCreateWindow(@RequestParam String userId,
      @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime dateTime){
    DietCreateWindowDTO dietCreateWindowDTO = dietService.dietCreatePage(userId, dateTime);

    return new ResponseEntity<>(dietCreateWindowDTO, HttpStatus.OK);
  }

  @GetMapping("/foods")
  public ResponseEntity<List<FoodDataResponse>> getAllFoodData() {
    List<FoodDataResponse> data = dietService.sendAllFoodData();
    return ResponseEntity.ok(data);
  }

  @GetMapping("/supplements")
  public ResponseEntity<List<SupplementDataResponse>> getAllSupplementData() {
    List<SupplementDataResponse> data = dietService.sendAllSupplementData();
    return ResponseEntity.ok(data);
  }
}