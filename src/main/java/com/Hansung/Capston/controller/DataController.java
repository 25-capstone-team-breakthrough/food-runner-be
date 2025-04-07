package com.Hansung.Capston.controller;

import com.Hansung.Capston.config.OpenApiConfig;
import com.Hansung.Capston.dto.FoodDataResponse;
import com.Hansung.Capston.dto.SupplementDataResponse;
import com.Hansung.Capston.service.DataService;
import com.Hansung.Capston.service.MealService;
import com.Hansung.Capston.service.OpenApiService;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.Mapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("api/data")
public class DataController {

  private final DataService dataService;
  private final OpenApiService openApiService;

  @Autowired
  public DataController(DataService dataService, OpenApiService openApiService) {
    this.dataService = dataService;
    this.openApiService = openApiService;
  }

  @GetMapping("/foods") // 음식 데이터
  public ResponseEntity<List<FoodDataResponse>> getAllFoodData() {
    List<FoodDataResponse> data = dataService.sendAllFoodData();
    return ResponseEntity.ok(data);
  }

  @GetMapping("/supplements/{keyword}") // 영양소 데이터
  public ResponseEntity<List<SupplementDataResponse>> searchSupplement(@PathVariable String keyword) {
    List<SupplementDataResponse> data = openApiService.searchSupplement(keyword);

    return ResponseEntity.ok(data);
  }
}
