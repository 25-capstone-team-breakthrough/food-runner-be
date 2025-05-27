package com.Hansung.Capston.config;

import com.fasterxml.jackson.databind.util.StdDateFormat;
import org.springframework.boot.autoconfigure.jackson.Jackson2ObjectMapperBuilderCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.TimeZone;

@Configuration
public class JacksonConfig {

  @Bean
  public Jackson2ObjectMapperBuilderCustomizer jacksonTimeZoneCustomizer() {
    return builder -> builder.timeZone(TimeZone.getTimeZone("Asia/Seoul"));
  }
}
