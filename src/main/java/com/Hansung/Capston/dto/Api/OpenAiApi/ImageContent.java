package com.Hansung.Capston.dto.Api.OpenAiApi;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
public class ImageContent extends Content {
  private ImageUrl image_url;

  public ImageContent(String imageLink) {
    super("image_url");
    this.image_url = new ImageUrl(imageLink);
  }

  @Data
  @AllArgsConstructor
  public static class ImageUrl {
    private String url;
  }
}
