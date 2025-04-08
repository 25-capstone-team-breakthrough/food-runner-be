package com.Hansung.Capston.service;

import com.Hansung.Capston.config.OpenApiConfig;
import com.Hansung.Capston.dto.SupplementApiResponse;
import com.Hansung.Capston.dto.SupplementDataResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.net.URI;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class OpenApiService {

  private final OpenApiConfig openApiConfig;

  public List<SupplementDataResponse> searchSupplement(String keyword) {
    int pageSize = 100;
    int startIndex = 1;
    int endIndex = pageSize;
    List<SupplementDataResponse> supplementDataResponses = new ArrayList<>();

    WebClient webClient = WebClient.builder()
        .baseUrl("https://openapi.foodsafetykorea.go.kr/api")
        .codecs(config -> config.defaultCodecs().maxInMemorySize(10 * 1024 * 1024))
        .build();

    while (true) {
      String encodedKeyword = URLEncoder.encode(keyword, StandardCharsets.UTF_8);
      String fullPath = "/" + openApiConfig.getServiceKey() + "/I0030/json/" + startIndex + "/" + endIndex +
          "/PRDLST_NM=" + encodedKeyword;

      SupplementApiResponse response = webClient.get()
          .uri(URI.create("https://openapi.foodsafetykorea.go.kr/api" + fullPath))
          .retrieve()
          .bodyToMono(SupplementApiResponse.class)
          .block();

      if (response == null || response.getBody() == null || response.getBody().getRow() == null || response.getBody().getRow().isEmpty()) {
        break; // 검색 결과가 없으면 종료
      }

      supplementDataResponses.addAll(response.getBody().getRow());

      startIndex += pageSize;
      endIndex += pageSize;
    }

    return supplementDataResponses;
  }
}
