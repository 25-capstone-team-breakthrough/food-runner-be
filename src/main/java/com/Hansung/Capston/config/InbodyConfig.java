package com.Hansung.Capston.config;

import java.util.Map;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
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

  @Qualifier("inbodyTemplate")
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
  @Qualifier("inbodyTemplate")
  private ObjectProvider<RestTemplate> restTemplateProvider;

  public ResponseEntity<Map> postToInbody(String apiUrl, Map<String, Object> requestBody) {
    HttpHeaders headers = new HttpHeaders();
    headers.setContentType(org.springframework.http.MediaType.APPLICATION_JSON);
    HttpEntity<Map<String, Object>> requestEntity = new HttpEntity<>(requestBody, headers);

    return restTemplateProvider.getIfAvailable()
        .exchange(apiUrl, HttpMethod.POST, requestEntity, Map.class);
  }
}
