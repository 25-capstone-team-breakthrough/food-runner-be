package com.Hansung.Capston.dto.OpenAiApi;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@Data
public class TextContent extends Content {
  private String text;

  public TextContent(String text) {
    type = "text";
    this.text = text;
  }
}
