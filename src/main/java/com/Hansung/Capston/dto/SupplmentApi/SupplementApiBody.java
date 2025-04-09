package com.Hansung.Capston.dto.SupplmentApi;

import java.util.List;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SupplementApiBody {
  private List<SupplementDataFromOpenApi> row;
  private int total_count;
}
