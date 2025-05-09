package com.Hansung.Capston.dto.Api.OpenAiApi;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;

@Data
@Getter
@AllArgsConstructor
public class Message {
  private String role;
  private String content;
}
