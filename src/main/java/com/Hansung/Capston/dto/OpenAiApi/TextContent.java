package com.Hansung.Capston.dto.OpenAiApi;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class TextContent extends Content {
  private String text;

  public TextContent(String text) {
    type = "text";
    this.text = text;
  }
}
