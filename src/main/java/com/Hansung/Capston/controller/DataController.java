package com.Hansung.Capston.controller;

import com.Hansung.Capston.dto.FoodDataDTO;
import com.Hansung.Capston.dto.SupplmentApi.SupplementDataFromOpenApi;
import com.Hansung.Capston.service.DataService;
import com.Hansung.Capston.service.OpenApiService;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

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
  public ResponseEntity<List<FoodDataDTO>> getAllFoodData() {
    List<FoodDataDTO> data = dataService.sendAllFoodData();
    return ResponseEntity.ok(data);
  }

  @GetMapping("/supplements/{keyword}") // 영양소 데이터
  public ResponseEntity<List<SupplementDataFromOpenApi>> searchSupplement(@PathVariable String keyword) {

    List<SupplementDataFromOpenApi> data = openApiService.searchSupplement(keyword);

    return ResponseEntity.ok(data);
  }

  @PostMapping("/supplements/from-api")
  public ResponseEntity<Integer> saveSupplementFromApi() {
    List<SupplementDataFromOpenApi> requests = openApiService.getAllSupplements();
    dataService.saveSupplementAll(requests);

    return new ResponseEntity<>(requests.size(), HttpStatus.OK);
  }
}
