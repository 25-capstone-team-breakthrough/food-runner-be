package com.Hansung.Capston.service.ApiService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class InbodyApiService {
  @Qualifier("inbodyTemplate")
  @Autowired
  private RestTemplate restTemplate;
  @Value("https://apikr.lookinbody.com")
  private String openAiUrl;
  @Value("${openai.model}")
  private String model;
}
