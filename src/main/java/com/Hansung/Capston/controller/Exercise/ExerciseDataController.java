package com.Hansung.Capston.controller.Exercise;

import com.Hansung.Capston.service.Exercise.ExerciseDataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("/exerciseData")
public class ExerciseDataController {


    private final ExerciseDataService exerciseDataService;

    @Autowired
    public ExerciseDataController(ExerciseDataService exerciseDataService) {
        this.exerciseDataService = exerciseDataService;
    }

    @PostMapping("/upload-csv")
    public ResponseEntity<String> uploadCsvFile(@RequestParam("file") MultipartFile file) {
        try {
            // 업로드된 CSV 파일을 서버에 저장하고 처리
            exerciseDataService.processCsvFile(file);
            return ResponseEntity.ok("운동 데이터가 성공적으로 업로드되었습니다.");
        } catch (IOException e) {
            return ResponseEntity.status(500).body("CSV 파일 처리 실패: " + e.getMessage());
        }
    }
}
