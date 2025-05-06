package com.Hansung.Capston.controller.Exercise;

import com.Hansung.Capston.dto.Exersice.ExerciseLog.ExerciseLogDto;
import com.Hansung.Capston.dto.Exersice.ExerciseLogCalories.ExerciseLogCaloriesDto;
import com.Hansung.Capston.dto.Exersice.ExerciseSave.ExerciseSaveRequest;
import com.Hansung.Capston.dto.Exersice.ExerciseSave.ExerciseSaveResponse;
import com.Hansung.Capston.entity.Exercise.ExerciseLog;
import com.Hansung.Capston.entity.Exercise.ExerciseSave;
import com.Hansung.Capston.service.Exercise.CalorieAnalysisService;
import com.Hansung.Capston.service.Exercise.ExerciseService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/exercise")
@Slf4j
public class ExerciseController {
    private final ExerciseService exerciseService;
    private final CalorieAnalysisService calorieAnalysisService;

    public ExerciseController(ExerciseService exerciseService, CalorieAnalysisService calorieAnalysisService) {
        this.exerciseService = exerciseService;
        this.calorieAnalysisService = calorieAnalysisService;
    }

    //운동 기록 관련
    //운동 기록 저장
    @PostMapping("/log")
    public ResponseEntity<String> saveExerciselog(@RequestBody ExerciseLogDto dto) {
        // SecurityContext에서 JWT 토큰으로 인증된 사용자 ID 추출
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null || auth.getPrincipal() == null) {
            return ResponseEntity.status(401).build();
        }
        String userId = (String) auth.getPrincipal();

        ExerciseLog saved = exerciseService.saveExerciseLog(userId, dto);

        // 칼로리 분석및 저장
        try {
            calorieAnalysisService.analyzeAndSave(saved);
        } catch (Exception e) {
            log.error("칼로리 분석 중 오류", e);
        }
        return ResponseEntity.ok("운동 기록이 저장되었습니다.");
    }

    //운동 기록 조회
    @GetMapping("/logSearch")
    public ResponseEntity<List<ExerciseLogDto>> searchExerciseLog(){
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null || auth.getPrincipal() == null) {
            return ResponseEntity.status(401).build();
        }
        String userId = (String) auth.getPrincipal();
        List<ExerciseLogDto> logs = exerciseService.searchExerciseLog(userId);
        return ResponseEntity.ok(logs);
    }

    // 운동 기록 삭제
    @DeleteMapping("/removeLog/{logId}")
    public ResponseEntity<?> removeExerciseLog(@PathVariable Integer logId) {

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null || auth.getPrincipal() == null) {
            return ResponseEntity.status(401).build();
        }
        String userId = (String) auth.getPrincipal();
        exerciseService.deleteExerciseLog(userId, logId);
        return ResponseEntity.ok("운동 기록이 삭제되었습니다.");

    }

    //소모 칼로리 조회
    @GetMapping("/calories")
    public ResponseEntity<List<ExerciseLogCaloriesDto>> getCalories() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null || auth.getPrincipal() == null) {
            return ResponseEntity.status(401).build();
        }
        String userId = (String) auth.getPrincipal();
        List<ExerciseLogCaloriesDto> dtos = calorieAnalysisService.getCaloriesByUser(userId);
        return ResponseEntity.ok(dtos);
    }


    //운동 즐겨찾기 관련
    //즐겨찾기 추가
    @PostMapping("/favoriteAdd")
    public ResponseEntity<String> addfavoriteEx(@RequestBody ExerciseSaveRequest dto) {
        // SecurityContext에서 JWT 토큰으로 인증된 사용자 ID 추출
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null || auth.getPrincipal() == null) {
            return ResponseEntity.status(401).build();
        }
        String userId = (String) auth.getPrincipal();

        ExerciseSave saved = exerciseService.addfavoriteEx(userId, dto);
        if (saved == null) {
            return ResponseEntity.badRequest().body("이미 저장된 운동입니다.");
        }
        return ResponseEntity.ok("즐겨찾기 추가되었습니다.");
    }

    // 사용자의 즐겨찾기 목록 조회
    @GetMapping("/favoriteSearch")
    public ResponseEntity<List<ExerciseSaveResponse>> searchfavoriteEx() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null || auth.getPrincipal() == null) {
            return ResponseEntity.status(401).build();
        }
        String userId = (String) auth.getPrincipal();

        List<ExerciseSave> responses = exerciseService.searchfavoriteEx(userId);
        List<ExerciseSaveResponse> dtos = responses.stream()
                .map(es -> new ExerciseSaveResponse(es.getExerciseId()))
                .toList();
        return ResponseEntity.ok(dtos);
    }

    // 즐겨찾기 삭제 (경로 변수로 exerciseId 전달)
    @DeleteMapping("/remove/{exerciseId}")
    public ResponseEntity<?> removefavoriteEx(@PathVariable Integer exerciseId) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null || auth.getPrincipal() == null) {
            return ResponseEntity.status(401).build();
        }
        String userId = (String) auth.getPrincipal();

        exerciseService.removefavoriteEx(userId, exerciseId);
        return ResponseEntity.ok("운동 즐겨찾기가 삭제되었습니다.");
    }


}
