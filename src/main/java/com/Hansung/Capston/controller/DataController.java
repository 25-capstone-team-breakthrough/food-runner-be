//package com.Hansung.Capston.controller;
//
//import com.Hansung.Capston.entity.Diet.Food.FoodData;
//import com.Hansung.Capston.service.DataService;
//import java.io.IOException;
//import java.util.List;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.*;
//import org.springframework.web.multipart.MultipartFile;
//
//@RestController
//@RequestMapping("api/data")
//public class DataController {
//
//  private final DataService dataService;
//
//  @Autowired
//  public DataController(DataService dataService) {
//    this.dataService = dataService;
//  }
//
//  @GetMapping("/foods") // 음식 데이터
//  public ResponseEntity<List<FoodData>> getAllFoodData() {
//    List<FoodData> data = dataService.sendAllFoodData();
//    return ResponseEntity.ok(data);
//  }
//
//
//
////  @PostMapping("/foods/upload-csv")
////  public ResponseEntity<String> uploadCsvFile(@RequestParam("file") MultipartFile file) {
////    try {
////      // 업로드된 CSV 파일을 서버에 저장하고 처리
////      dataService.processFoodCsvFile(file);
////      return ResponseEntity.ok("음식 데이터가 성공적으로 업로드되었습니다.");
////    } catch (IOException e) {
////      return ResponseEntity.status(500).body("CSV 파일 처리 실패: " + e.getMessage());
////    }
//  }
//
////  @PostMapping("/ingredients/upload-csv")
////  public ResponseEntity<String> uploadIngredientCsvFile(@RequestParam("file") MultipartFile file) {
////    try {
////      dataService.processIngredientCsvFile(file);
////      return ResponseEntity.ok("식재료 데이터가 성공적으로 업로드되었습니다.");
////    } catch (IOException e) {
////      return ResponseEntity.status(500).body("CSV 파일 처리 실패: " + e.getMessage());
////    }
////  }
