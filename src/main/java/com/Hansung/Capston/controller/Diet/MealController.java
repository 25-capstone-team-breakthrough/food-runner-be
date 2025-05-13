package com.Hansung.Capston.controller.Diet;

import com.Hansung.Capston.dto.Diet.Meal.MealLogRequest;
import com.Hansung.Capston.dto.Diet.Meal.MealLogResponse;
import com.Hansung.Capston.service.ApiService.AwsS3Service;
import com.Hansung.Capston.service.Diet.MealService;
import com.Hansung.Capston.service.Diet.NutrientService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.function.Function;

@RestController
@RequestMapping("/diet/meal")
public class MealController {
  private final MealService mealService;
  private final NutrientService nutrientService;
  private final AwsS3Service awsS3Service;

  public MealController(MealService mealService, NutrientService nutrientService, AwsS3Service awsS3Service) {
    this.mealService = mealService;
    this.nutrientService = nutrientService;
      this.awsS3Service = awsS3Service;
  }

  // 이미지 클라이언트 요청 예시: /getS3URL?fileName=example.jpg&contentType=image/jpeg
  @GetMapping("/getS3URL")
  public ResponseEntity<String> getS3URL(@RequestParam("fileName") String fileName, @RequestParam("contentType") String contentType) {
    // SecurityContext에서 JWT 토큰으로 인증된 사용자 ID 추출
    Authentication auth = SecurityContextHolder.getContext().getAuthentication();
    if (auth == null || auth.getPrincipal() == null) {
      return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);  // 401 Unauthorized
    }
    String userId = (String) auth.getPrincipal();

    // 람다 표현식으로 파일 이름 해시화
    Function<String, String> hashFileName = (inputFileName) -> {
      try {
        MessageDigest digest = MessageDigest.getInstance("SHA-256");
        byte[] hashBytes = digest.digest(inputFileName.getBytes());
        StringBuilder hexString = new StringBuilder();
        for (byte b : hashBytes) {
          hexString.append(String.format("%02x", b));
        }
        return hexString.toString();
      } catch (NoSuchAlgorithmException e) {
        throw new RuntimeException("Hashing algorithm not found", e);
      }
    };

    Long number = mealService.loadMealLogs(userId).getImageMealLogs().getLast().getImageMealLogId();
    if(number == null) {
      number = 0L;
    }

    // 파일 이름 해시화
    String hashedFileName = hashFileName.apply(fileName + number);

    return ResponseEntity.ok(awsS3Service.generatePreSignedUrl(hashedFileName, contentType));
  }

  // 식사 기록 저장
  @PostMapping("/log/save")
  public ResponseEntity<String> saveMealLog(@RequestBody MealLogRequest request) {
    Authentication auth = SecurityContextHolder.getContext().getAuthentication();
    if (auth == null || auth.getPrincipal() == null) {
      return ResponseEntity.status(401).build();
    }
    String userId = (String) auth.getPrincipal();

    nutrientService.saveNutrientLog(userId, true, mealService.saveMealLog(request, userId).getMealId());

    return ResponseEntity.ok("save success");
  }


  // 식사 기록 불러오기
  @GetMapping("/log/load")
  public ResponseEntity<MealLogResponse> loadMealLog() {
    Authentication auth = SecurityContextHolder.getContext().getAuthentication();
    if (auth == null || auth.getPrincipal() == null) {
      return ResponseEntity.status(401).build();
    }
    String userId = (String) auth.getPrincipal();

    MealLogResponse res = mealService.loadMealLogs(userId);

    return ResponseEntity.ok(res);
  }

  // 식사 기록 삭제
  @PostMapping("/log/delete")
  public ResponseEntity<String> deleteMealLog(@RequestParam(name = "log_id") Long logId) {
    Authentication auth = SecurityContextHolder.getContext().getAuthentication();
    if (auth == null || auth.getPrincipal() == null) {
      return ResponseEntity.status(401).build();
    }
    String userId = (String) auth.getPrincipal();

    nutrientService.saveNutrientLog(userId, false, logId);
    mealService.deleteMealLog(logId);

    return ResponseEntity.ok("delete success");
  }

}
