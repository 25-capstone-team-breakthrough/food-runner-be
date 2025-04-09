package com.Hansung.Capston.dto.OpenAiApi;

import lombok.Data;

@Data
public class Content {
   protected String type;

   public Content(String type) {
      this.type = type;
   }

   public Content() {
      this.type = "text";
   }
}

