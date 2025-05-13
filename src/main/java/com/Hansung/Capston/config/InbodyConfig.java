package com.Hansung.Capston.config;

import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

@Configuration
public class InbodyConfig {
  @Value("${inbody.api.account}")
  private String account;
  @Value("${inbody.api.key}")
  private String key;

  @Bean
  public RestTemplate inbodyTemplate() {
    RestTemplate restTemplate = new RestTemplate();
    restTemplate.getInterceptors().add((request, body, execution) -> {
      request.getHeaders().add("Account", account);
      request.getHeaders().add("API-KEY", key);
      return execution.execute(request, body);
    });
    return restTemplate;
  }

  @Autowired
  private RestTemplate inbodyTemplate;

  // 적절한 api url로 설정할 때 사용
  public ResponseEntity<Map> postToInbody(String apiUrl, Map<String, Object> requestBody) { // apiUrl 파라미터 추가
    HttpHeaders headers = new HttpHeaders();
    headers.setContentType(org.springframework.http.MediaType.APPLICATION_JSON);

    HttpEntity<Map<String, Object>> requestEntity = new HttpEntity<>(requestBody, headers);

    return inbodyTemplate.exchange(
        apiUrl, 
        HttpMethod.POST,
        requestEntity,
        Map.class
    );
  }
}
