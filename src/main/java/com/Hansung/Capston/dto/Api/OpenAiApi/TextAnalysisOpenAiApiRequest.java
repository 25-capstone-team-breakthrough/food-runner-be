package com.Hansung.Capston.dto.Api.OpenAiApi;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TextAnalysisOpenAiApiRequest {

  private String model;
  private List<Message> messages;  // messages는 List<Message>로 설정해야 합니다.

  // 추가
  private double temperature;
  private double top_p = 1.0;

  // 기존 생성자
  public TextAnalysisOpenAiApiRequest(String model, List<Message> messages) {
    this.model = model;
    this.messages = messages;
  }

  // 새 생성자: temperature 포함
  public TextAnalysisOpenAiApiRequest(String model, List<Message> messages, double temperature) {
    this.model       = model;
    this.messages    = messages;
    this.temperature = temperature;
  }
}