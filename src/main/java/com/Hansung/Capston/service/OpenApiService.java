package com.Hansung.Capston.service;

import com.Hansung.Capston.config.OpenApiConfig;
import com.Hansung.Capston.dto.SupplmentApi.SupplementApiResponse;
import com.Hansung.Capston.dto.SupplmentApi.SupplementDataRequest;
import com.Hansung.Capston.dto.SupplmentApi.SupplementDataFromOpenApi;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;

import java.net.URI;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class OpenApiService {

  private final OpenApiConfig openApiConfig;

  public List<SupplementDataFromOpenApi> searchSupplement(String keyword) {
    int pageSize = 100;
    int startIndex = 1;
    int endIndex = pageSize;
    List<SupplementDataFromOpenApi> list = new ArrayList<>();

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

      list.addAll(response.getBody().getRow());

      startIndex += pageSize;
      endIndex += pageSize;
    }

    return list;
  }

  // OpenAPI에서 모든 영양제 데이터를 가져오는 메소드 (페이지 여러개 처리)
  public List<SupplementDataFromOpenApi> getAllSupplements() {
    int pageSize = 100;
    int startIndex = 1;
    int endIndex = pageSize;
    List<SupplementDataFromOpenApi> list = new ArrayList<>();

    WebClient webClient = WebClient.builder()
            .baseUrl("https://openapi.foodsafetykorea.go.kr/api")
            .codecs(config -> config.defaultCodecs().maxInMemorySize(100 * 1024 * 1024))
            .build();

    while (true) {
      String fullPath = "/" + openApiConfig.getServiceKey() + "/I0030/json/" + startIndex + "/" + endIndex;

      SupplementApiResponse response = webClient.get()
              .uri(URI.create("https://openapi.foodsafetykorea.go.kr/api" + fullPath))
              .retrieve()
              .bodyToMono(SupplementApiResponse.class)
              .block();

      if (response == null || response.getBody() == null || response.getBody().getRow() == null || response.getBody().getRow().isEmpty()) {
        break;
      }

      list.addAll(response.getBody().getRow());

      startIndex += pageSize;
      endIndex += pageSize;
    }

    return list;
  }

}

