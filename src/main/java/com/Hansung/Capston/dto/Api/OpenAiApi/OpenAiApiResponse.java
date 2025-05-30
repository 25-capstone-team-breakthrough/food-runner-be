package com.Hansung.Capston.dto.Api.OpenAiApi;


import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OpenAiApiResponse {
  private List<Choice> choices;


  @Data
  @NoArgsConstructor
  @AllArgsConstructor
  public static class Choice {
    private int index;
    private Message message;

  }
}