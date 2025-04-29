package com.Hansung.Capston.controller;

import com.Hansung.Capston.dto.FoodDataDTO;
import com.Hansung.Capston.dto.MealLog.ImageMealLogCreateRequest;
import com.Hansung.Capston.dto.MealLog.MealLogCreateResponse;
import com.Hansung.Capston.dto.MealLog.SearchMealLogCreateRequest;
import com.Hansung.Capston.entity.MealLog;
import com.Hansung.Capston.service.MealService;
import com.Hansung.Capston.service.NutrientService;
import com.Hansung.Capston.service.OpenAiApiService;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@CrossOrigin
@RestController
@RequestMapping(value = "/api/diet") // 공통 URL 경로를 지정함
public class MealLogController {
  private final MealService mealService;
  private final OpenAiApiService openAiApiService;
  private final NutrientService nutrientService;

  @Autowired
  public MealLogController(MealService mealService, OpenAiApiService openAiApiService,
      NutrientService nutrientService) {
    this.mealService = mealService;
    this.openAiApiService = openAiApiService;
    this.nutrientService = nutrientService;
  }

  @PostMapping("/save-image-meal") // 저장
  public ResponseEntity<Map<Long, String>> saveImageMealLog(@RequestBody ImageMealLogCreateRequest imageMealLogCreateRequest) {
    // SecurityContext에서 JWT 토큰으로 인증된 사용자 ID 추출
    Authentication auth = SecurityContextHolder.getContext().getAuthentication();
    if (auth == null || auth.getPrincipal() == null) {
      return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);  // 401 Unauthorized
    }
    String userId = (String) auth.getPrincipal();
    imageMealLogCreateRequest.setUserId(userId);

    Map<Long, String> response = new HashMap<>();

    // 이미지 분석 결과로 음식 이름 배열 받기
    String[] foodNames = openAiApiService.mealImageAnalysis(imageMealLogCreateRequest);

    if (foodNames != null && foodNames.length > 0) {
      List<FoodDataDTO> foodDataDTOS = nutrientService.checkNutrientData(foodNames, openAiApiService);

      // 음식 데이터를 처리하고 MealLog 저장
      for (FoodDataDTO foodDataDTO : foodDataDTOS) {
        MealLog mealLog = mealService.imageSave(imageMealLogCreateRequest, foodDataDTO);
        response.put(mealLog.getMealId(), "success"); // mealId를 키로 응답에 추가
      }

      return ResponseEntity.ok(response);
    }

    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);  // 400 Bad Request
  }


  @PostMapping("/save-search-meal") // 저장
  public ResponseEntity<MealLog> saveSearchMealLog( @RequestBody SearchMealLogCreateRequest searchMealLogCreateRequest) {
    // SecurityContext에서 JWT 토큰으로 인증된 사용자 ID 추출
    Authentication auth = SecurityContextHolder.getContext().getAuthentication();
    if (auth == null || auth.getPrincipal() == null) {
      return ResponseEntity.status(401).build();
    }
    String userId = (String) auth.getPrincipal();

    MealLog mealLog = mealService.searchSave(userId, searchMealLogCreateRequest); // image_meal_log나 search_meal_log는 서비스에서 자동으로 추가해줌
    // 저장뿐만 아니라 영양소도 추가해야 함

    return new ResponseEntity<>(mealLog, HttpStatus.OK);
  }

  @GetMapping("/main") // diet 메인화면
  public ResponseEntity<MealLogCreateResponse> getGCreateWindow(@RequestParam String userId,
                                                                @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime dateTime){
    MealLogCreateResponse mealLogCreateResponse = mealService.dietCreatePage(userId, dateTime);

    return new ResponseEntity<>(mealLogCreateResponse, HttpStatus.OK);
  }
}