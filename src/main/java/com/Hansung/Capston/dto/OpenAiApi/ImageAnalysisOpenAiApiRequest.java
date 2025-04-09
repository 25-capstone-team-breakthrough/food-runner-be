package com.Hansung.Capston.dto.OpenAiApi;

import java.util.ArrayList;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ImageAnalysisOpenAiApiRequest {
  private String model;
  private List<Message> messages;
  private Integer max_tokens = 1000;

  public ImageAnalysisOpenAiApiRequest(String model, List<Content> content) {
    this.model = model;
    this.messages = List.of(new Message("user", content));
  }

  @Data
  @AllArgsConstructor
  public static class Message {
    private String role;
    private List<Content> content;
  }
}
