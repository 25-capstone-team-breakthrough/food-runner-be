package com.Hansung.Capston.service.UserInfo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

@Service
public class InbodyService {

  @Qualifier("inbodyTemplate")
  @Autowired
  private RestTemplate restTemplate;

  public Map<String, Object> postToInbody(String apiUrl, Map<String, Object> requestBody) {
    HttpHeaders headers = new HttpHeaders();
    headers.setContentType(MediaType.APPLICATION_JSON);

    HttpEntity<Map<String, Object>> entity = new HttpEntity<>(requestBody, headers);

    ResponseEntity<Map> response = restTemplate.exchange(
        apiUrl,
        HttpMethod.POST,
        entity,
        Map.class
    );

    return response.getBody();
  }
}
