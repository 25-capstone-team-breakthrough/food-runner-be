package com.Hansung.Capston.dto.Diet.Supplement;

import java.time.LocalDateTime;
import lombok.Data;

@Data
public class SupplementLogRequest {
  private Long id;
  private LocalDateTime dateTime;
}
