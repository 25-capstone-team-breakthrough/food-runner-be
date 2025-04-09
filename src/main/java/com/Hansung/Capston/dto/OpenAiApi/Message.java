package com.Hansung.Capston.dto.OpenAiApi;

import java.util.List;
import lombok.Data;
import lombok.Getter;

@Data
@Getter
public class Message {
  private String role;
  private String content;
}
