package com.Hansung.Capston.controller;

import com.Hansung.Capston.dto.ExersiceSave.ExerciseSaveDto;
import com.Hansung.Capston.entity.ExerciseSave;
import com.Hansung.Capston.service.ExerciseService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/exercise")
public class ExerciseController {
    private final ExerciseService exerciseService;


    public ExerciseController(ExerciseService exerciseService) {
        this.exerciseService = exerciseService;
    }

    //즐겨찾기 추가
    @PostMapping("/favoriteAdd")
    public ResponseEntity<String> favoriteEx(@RequestBody ExerciseSaveDto dto) {
        // SecurityContext에서 JWT 토큰으로 인증된 사용자 ID 추출
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null || auth.getPrincipal() == null) {
            return ResponseEntity.status(401).build();
        }
        String userId = (String) auth.getPrincipal();

        ExerciseSave saved = exerciseService.saveExercise(userId, dto);
        if (saved == null) {
            return ResponseEntity.badRequest().body("이미 저장된 운동입니다.");
        }
        return ResponseEntity.ok("즐겨찾기 추가되었습니다.");
    }

    // 즐겨찾기 삭제 (경로 변수로 exerciseId 전달)
    @DeleteMapping("/remove/{exerciseId}")
    public ResponseEntity<?> removeExerciseSave(@PathVariable Integer exerciseId) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null || auth.getPrincipal() == null) {
            return ResponseEntity.status(401).build();
        }
        String userId = (String) auth.getPrincipal();

        exerciseService.removeExerciseSave(userId, exerciseId);
        return ResponseEntity.ok("운동 즐겨찾기가 삭제되었습니다.");
    }

    // 사용자의 즐겨찾기 목록 조회
    @GetMapping("/favoriteSearch")
    public ResponseEntity<List<ExerciseSave>> getExerciseSaves() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null || auth.getPrincipal() == null) {
            return ResponseEntity.status(401).build();
        }
        String userId = (String) auth.getPrincipal();

        List<ExerciseSave> saves = exerciseService.getExerciseSaves(userId);
        return ResponseEntity.ok(saves);
    }
}
