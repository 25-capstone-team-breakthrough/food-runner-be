package com.Hansung.Capston.controller.Exercise;

import com.Hansung.Capston.dto.Exersice.YoutubeExerciseDTO;
import com.Hansung.Capston.entity.Exercise.ExerciseVideo;
import com.Hansung.Capston.entity.Exercise.RecommandExerciseVideo;
import com.Hansung.Capston.repository.Exercise.ExerciseVideoRepository;
import com.Hansung.Capston.service.Exercise.RecommandExerciseVideoService;
import com.Hansung.Capston.service.Exercise.VideoService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
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
public class VideoController {

    private final VideoService videoService;
    private final RecommandExerciseVideoService recommandExerciseVideoService;
    private final ExerciseVideoRepository exerciseVideoRepository;

    //운동영상
    @GetMapping("/exercises")
    public ResponseEntity<Map<String, Object>> getExerciseVideos() throws IOException {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null || auth.getPrincipal() == null) {
            return ResponseEntity.status(401).build();
        }

        String userId = (String) auth.getPrincipal();

        // DB에 저장된 추천 영상 조회
        List<RecommandExerciseVideo> recs = recommandExerciseVideoService.getRecommendationsByUserId(userId);
        List<YoutubeExerciseDTO> recommendedVideos = recs.stream().map(rec -> YoutubeExerciseDTO.builder()
                .videoId(rec.getVideoId())
                .title(rec.getTitle())
                .url(rec.getUrl())
                .category(rec.getCategory())
                .isAIRecommendation(rec.getIsAIRecommendation())
                .build()).toList();

//        // ✅ 2. YouTube에서 일반 영상 검색
//        String[] categories = {"어깨", "가슴", "배", "팔", "허벅지", "엉덩이", "종아리", "등"};
//        Map<String, List<YoutubeExerciseDTO>> searchedVideos = new LinkedHashMap<>();
//        for (String cat : categories) {
//            List<YoutubeExerciseDTO> list = videoService.searchVideos(cat + " 운동", 1);
//            searchedVideos.put(cat, list);
//        }

        // 2) 기본 운동 영상: DB 캐시 확인 → 없으면 YouTube 검색
        String[] categories = {"어깨", "가슴", "배", "팔", "허벅지", "엉덩이", "종아리", "등"};
        Map<String, List<YoutubeExerciseDTO>> searchedVideos = new LinkedHashMap<>();
        for (String cat : categories) {
            // DB 조회
            List<ExerciseVideo> cached = exerciseVideoRepository.findByUserUserIdAndCategory(userId, cat);
            if (!cached.isEmpty()) {
                // 캐시된 엔티티를 DTO로 매핑
                List<YoutubeExerciseDTO> list = cached.stream()
                        .map(ev -> YoutubeExerciseDTO.builder()
                                .videoId(ev.getVideoId())
                                .title(ev.getTitle())
                                .url(ev.getUrl())
                                .category(ev.getCategory())
                                .isAIRecommendation(ev.getIsAIRecommendation())
                                .build())
                        .toList();
                searchedVideos.put(cat, list);
            } else {
                // 없으면 YouTube API 호출 및 DB 저장 (service 내 로직)
                List<YoutubeExerciseDTO> list = videoService.searchVideos(cat + " 운동", 10);
                searchedVideos.put(cat, list);
            }
        }

        //3. 두 데이터 합쳐서 반환
        Map<String, Object> response = new LinkedHashMap<>();
        response.put("recommended", recommendedVideos);  // AI 추천 영상 (DB 기반)
        response.put("searched", searchedVideos);        // 일반 카테고리 영상 (YouTube 검색)

        return ResponseEntity.ok(response);
    }

}
