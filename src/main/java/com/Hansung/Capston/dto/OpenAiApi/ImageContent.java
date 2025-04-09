package com.Hansung.Capston.dto.OpenAiApi;

import java.util.ArrayList;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Value;

@EqualsAndHashCode(callSuper = true)
@Data
public class ImageContent extends Content {
  private ImageUrl image_url;

  public ImageContent(String base64Image) {
    super("image_url");
    this.image_url = new ImageUrl("data:image/jpeg;base64," + base64Image);
  }

  @Data
  @AllArgsConstructor
  public static class ImageUrl {
    private String url;
  }
}
