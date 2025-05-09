package com.Hansung.Capston.controller.Exercise;

import com.Hansung.Capston.dto.Exersice.YoutubeExerciseDTO;
import com.Hansung.Capston.service.Exercise.YoutubeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/videos")
public class YoutubeController {

    private final YoutubeService youtubeService;

    @GetMapping("/exercises")
    public ResponseEntity<Map<String, List<YoutubeExerciseDTO>>> getExerciseVideos() throws IOException {
        String[] categories = {"어깨", "가슴", "배", "팔", "허벅지", "엉덩이", "종아리", "등"};
        Map<String, List<YoutubeExerciseDTO>> result = new LinkedHashMap<>();
        for (String cat : categories) {
            // "부위 운동" 키워드로 검색
            List<YoutubeExerciseDTO> list = youtubeService.searchVideos(cat + " 운동", 2);
            result.put(cat, list);
        }
        return ResponseEntity.ok(result);
    }
}
