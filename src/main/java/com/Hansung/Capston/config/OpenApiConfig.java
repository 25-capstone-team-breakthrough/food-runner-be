package com.Hansung.Capston.config;

import lombok.Getter;
import lombok.Setter;
import lombok.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@Configuration
@ConfigurationProperties(prefix = "openapi.api")
@Getter
@Setter
public class OpenApiConfig {
  private String baseUrl;
  private String serviceKey;
}
