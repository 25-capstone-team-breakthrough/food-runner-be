package com.Hansung.Capston.service.Exercise;

import com.Hansung.Capston.dto.Exersice.YoutubeExerciseDTO;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.services.youtube.YouTube;
import com.google.api.services.youtube.model.SearchListResponse;
import com.google.api.services.youtube.model.SearchResult;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class VideoService {

    @Value("${youtube.api.key}")
    private String apiKey;

    /**
     * @param query      검색어(예: "어깨 운동")
     * @param maxResults 최대 가져올 영상 개수(예: 5)
     * @return VideoDto 리스트
     *
     */
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
                .map(item -> new YoutubeExerciseDTO(
                        item.getId().getVideoId(),
                        item.getSnippet().getTitle(),
                        "https://www.youtube.com/watch?v=" + item.getId().getVideoId()
                ))
                .collect(Collectors.toList());
    }
}

