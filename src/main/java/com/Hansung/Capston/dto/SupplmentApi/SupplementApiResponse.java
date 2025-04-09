package com.Hansung.Capston.dto.SupplmentApi;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SupplementApiResponse {
  @JsonProperty("I0030")
  private SupplementApiBody body;
}

