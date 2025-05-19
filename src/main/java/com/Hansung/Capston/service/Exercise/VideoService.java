package com.Hansung.Capston.service.Exercise;

import com.Hansung.Capston.dto.Exersice.YoutubeExerciseDTO;
import com.Hansung.Capston.service.ApiService.OpenAiApiService;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.services.youtube.YouTube;
import com.google.api.services.youtube.model.SearchListResponse;
import com.google.api.services.youtube.model.SearchResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class VideoService {

    @Value("${youtube.api.key}")
    private String apiKey;

    private final OpenAiApiService openAiApiService;

    @Autowired
    public VideoService(OpenAiApiService openAiApiService) {
        this.openAiApiService = openAiApiService;
    }

    public List<YoutubeExerciseDTO> searchVideos(String query, long maxResults) throws IOException {
        JsonFactory jsonFactory = JacksonFactory.getDefaultInstance();
        YouTube youtube = new YouTube.Builder(
                new NetHttpTransport(),
                jsonFactory,
                request -> {}
        ).setApplicationName("CapstonApp").build();

        YouTube.Search.List search = youtube.search()
                .list(List.of("snippet"));
        search.setKey(apiKey);
        search.setQ(query); //검색어 설정
        search.setType(List.of("video")); //비디오 타입만
        //search.setOrder("viewCount"); //조회수 순
        search.setMaxResults(maxResults); //결과 개수 제한

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
                        .isAIRecommendation(false)  // 일반 검색 Ai 추천 ㄴㄴ
                        .build())
                .collect(Collectors.toList());
    }
}

