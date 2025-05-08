package com.Hansung.Capston.controller;

import com.Hansung.Capston.dto.SupplmentApi.SupplementDataFromOpenApi;
import com.Hansung.Capston.entity.DataSet.FoodData;
import com.Hansung.Capston.service.DataService;
import com.Hansung.Capston.service.OpenApiService;
import java.io.IOException;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

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
  public ResponseEntity<List<FoodData>> getAllFoodData() {
    List<FoodData> data = dataService.sendAllFoodData();
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

  @PostMapping("/foods/upload-csv")
  public ResponseEntity<String> uploadCsvFile(@RequestParam("file") MultipartFile file) {
    try {
      // 업로드된 CSV 파일을 서버에 저장하고 처리
      dataService.processFoodCsvFile(file);
      return ResponseEntity.ok("운동 데이터가 성공적으로 업로드되었습니다.");
    } catch (IOException e) {
      return ResponseEntity.status(500).body("CSV 파일 처리 실패: " + e.getMessage());
    }
  }
}
