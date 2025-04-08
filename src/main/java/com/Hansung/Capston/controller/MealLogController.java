package com.Hansung.Capston.controller;

import com.Hansung.Capston.dto.ImageDietCreateDTO;
import com.Hansung.Capston.dto.DietCreateWindowDTO;
import com.Hansung.Capston.dto.SearchDietCreateDTO;
import com.Hansung.Capston.entity.MealLog;
import com.Hansung.Capston.service.MealService;
import java.io.IOException;
import java.time.LocalDateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping(value = "/api/diet") // 공통 URL 경로를 지정함
public class MealLogController {
  private final MealService mealService;

  @Autowired
  public MealLogController(MealService mealService) {
    this.mealService = mealService;
  }

  @PostMapping("/save-image-meal") // 저장
  public ResponseEntity<MealLog> saveImageMealLog( @RequestPart("dto") ImageDietCreateDTO imageDietCreateDTO, @RequestPart(value = "file", required = false) MultipartFile file) {
    // SecurityContext에서 JWT 토큰으로 인증된 사용자 ID 추출
    Authentication auth = SecurityContextHolder.getContext().getAuthentication();
    if (auth == null || auth.getPrincipal() == null) {
      return ResponseEntity.status(401).build();
    }
    String userId = (String) auth.getPrincipal();

    byte[] data = null;
    try {
      data = file.getBytes();
    } catch (IOException e) {
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
    }

    imageDietCreateDTO.setUserId(userId);
    imageDietCreateDTO.setMealImage(data);
    MealLog mealLog = mealService.imageSave(imageDietCreateDTO); // image_meal_log나 search_meal_log는 서비스에서 자동으로 추가해줌

    return new ResponseEntity<>(mealLog, HttpStatus.OK);
  }

  @PostMapping("/save-search-meal") // 저장
  public ResponseEntity<MealLog> saveSearchMealLog( @RequestBody SearchDietCreateDTO searchDietCreateDTO) {
    // SecurityContext에서 JWT 토큰으로 인증된 사용자 ID 추출
    Authentication auth = SecurityContextHolder.getContext().getAuthentication();
    if (auth == null || auth.getPrincipal() == null) {
      return ResponseEntity.status(401).build();
    }
    String userId = (String) auth.getPrincipal();

    MealLog mealLog = mealService.searchSave(userId,searchDietCreateDTO); // image_meal_log나 search_meal_log는 서비스에서 자동으로 추가해줌

    return new ResponseEntity<>(mealLog, HttpStatus.OK);
  }

  @GetMapping("/main") // diet 메인화면
  public ResponseEntity<DietCreateWindowDTO> getGCreateWindow(@RequestParam String userId,
      @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime dateTime){
    DietCreateWindowDTO dietCreateWindowDTO = mealService.dietCreatePage(userId, dateTime);

    return new ResponseEntity<>(dietCreateWindowDTO, HttpStatus.OK);
  }
}