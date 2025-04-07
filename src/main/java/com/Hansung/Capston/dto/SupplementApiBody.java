package com.Hansung.Capston.dto;

import java.util.List;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SupplementApiBody {
  private List<SupplementDataResponse> row;
  private int total_count;
}
