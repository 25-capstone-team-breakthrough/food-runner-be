package com.Hansung.Capston.service.Exercise;


import com.Hansung.Capston.dto.Exersice.YoutubeExerciseDTO;
import com.Hansung.Capston.entity.Exercise.ExerciseVideo;
import com.Hansung.Capston.entity.UserInfo.User;
import com.Hansung.Capston.repository.Exercise.ExerciseVideoRepository;
import com.Hansung.Capston.repository.UserInfo.UserRepository;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.services.youtube.YouTube;
import com.google.api.services.youtube.model.SearchListResponse;
import com.google.api.services.youtube.model.SearchResult;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class VideoService {

    @Value("${youtube.api.key}")
    private String apiKey;

    @Autowired
    private ExerciseVideoRepository exerciseVideoRepo;

    @Autowired
    private  UserRepository userRepo;

    /** YouTube API로부터 DTO 생성 */
    private List<YoutubeExerciseDTO> fetchFromYouTube(String query, long maxResults) throws IOException {
        JacksonFactory jsonFactory = JacksonFactory.getDefaultInstance();
        YouTube youtube = new YouTube.Builder(
                new NetHttpTransport(),
                jsonFactory,
                req -> {}
        ).setApplicationName("CapstonApp").build();

        YouTube.Search.List search = youtube.search().list(List.of("snippet"));
        search.setKey(apiKey);
        search.setQ(query);
        search.setType(List.of("video"));
        search.setMaxResults(maxResults);

        SearchListResponse response = search.execute();
        List<SearchResult> items = response.getItems();
        if (items == null || items.isEmpty()) {
            return Collections.emptyList();
        }
        return items.stream()
                .map(item -> YoutubeExerciseDTO.builder()
                        .videoId(item.getId().getVideoId())
                        .title(item.getSnippet().getTitle())
                        .url("https://www.youtube.com/watch?v=" + item.getId().getVideoId())
                        .isAIRecommendation(false)
                        .build())
                .collect(Collectors.toList());
    }

    /**
     * 1) DB에 캐시된 영상이 있으면 그대로 DTO로 반환
     * 2) 없으면 YouTube API 호출 → 저장 → DTO로 반환
     */
    public List<YoutubeExerciseDTO> searchVideos(String query, long maxResults) throws IOException {
        // 1) 사용자와 카테고리 추출
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String userId = (String) auth.getPrincipal();
        String category = query.replaceAll("\\s*운동\\s*", "").trim();

        // 2) 캐시 조회
        List<ExerciseVideo> cached = exerciseVideoRepo.findByUserUserIdAndCategory(userId, category);
        if (!cached.isEmpty()) {
            return cached.stream()
                    .map(ev -> YoutubeExerciseDTO.builder()
                            .videoId(ev.getVideoId())
                            .title(ev.getTitle())
                            .url(ev.getUrl())
                            .category(ev.getCategory())
                            .isAIRecommendation(ev.getIsAIRecommendation())
                            .build())
                    .collect(Collectors.toList());
        }

        // 3) YouTube API 호출
        List<YoutubeExerciseDTO> fetched = fetchFromYouTube(query, maxResults);
        if (fetched.isEmpty()) {
            return fetched;
        }

        // 4) 엔티티로 변환 + DB 저장
        User user = userRepo.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("사용자 없음: " + userId));

        List<ExerciseVideo> entities = fetched.stream()
                .map(dto -> ExerciseVideo.builder()
                        .user(user)
                        .category(category)
                        .videoId(dto.getVideoId())
                        .title(dto.getTitle())
                        .url(dto.getUrl())
                        .isAIRecommendation(false)
                        .build())
                .collect(Collectors.toList());

        exerciseVideoRepo.saveAll(entities);
        return fetched;
    }

//    public List<YoutubeExerciseDTO> searchVideos(String query, long maxResults) throws IOException {
//        JsonFactory jsonFactory = JacksonFactory.getDefaultInstance();
//        YouTube youtube = new YouTube.Builder(
//                new NetHttpTransport(),
//                jsonFactory,
//                request -> {}
//        ).setApplicationName("CapstonApp").build();
//
//        YouTube.Search.List search = youtube.search()
//                .list(List.of("snippet"));
//        search.setKey(apiKey);
//        search.setQ(query); //검색어 설정
//        search.setType(List.of("video")); //비디오 타입만
//        //search.setOrder("viewCount"); //조회수 순
//        search.setMaxResults(maxResults); //결과 개수 제한
//
//        SearchListResponse response = search.execute();
//        List<SearchResult> items = response.getItems();
//        if (items == null || items.isEmpty()) {
//            return Collections.emptyList();
//        }
//        return items.stream()
//                .map(item -> YoutubeExerciseDTO.builder()
//                        .videoId(item.getId().getVideoId())
//                        .title(item.getSnippet().getTitle())
//                        .url("https://www.youtube.com/watch?v=" + item.getId().getVideoId())
//                        .isAIRecommendation(false)  // 일반 검색 Ai 추천 ㄴㄴ
//                        .build())
//                .collect(Collectors.toList());
//    }



}

