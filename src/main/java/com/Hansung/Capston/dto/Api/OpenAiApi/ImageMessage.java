package com.Hansung.Capston.dto.Api.OpenAiApi;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;

@Data
@AllArgsConstructor
@Getter
public class ImageMessage {
  private String role;
  private List<Content> content;
}
