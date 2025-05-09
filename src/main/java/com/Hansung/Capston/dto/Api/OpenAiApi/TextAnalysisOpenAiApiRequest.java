package com.Hansung.Capston.dto.Api.OpenAiApi;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TextAnalysisOpenAiApiRequest {

  private String model;
  private List<Message> messages;  // messages는 List<Message>로 설정해야 합니다.

}