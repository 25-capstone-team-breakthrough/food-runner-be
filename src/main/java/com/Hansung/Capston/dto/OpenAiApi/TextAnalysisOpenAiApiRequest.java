package com.Hansung.Capston.dto.OpenAiApi;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TextAnalysisOpenAiApiRequest {
  private String model;
  private TextContent prompt;

  @Data
  @AllArgsConstructor
  public static class Message {
    private String role;
    private List<Content> content;
  }

}
