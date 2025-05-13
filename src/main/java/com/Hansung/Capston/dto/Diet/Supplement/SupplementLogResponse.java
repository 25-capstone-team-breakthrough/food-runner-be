package com.Hansung.Capston.dto.Diet.Supplement;

import com.Hansung.Capston.entity.Diet.Supplement.SupplementData;
import com.Hansung.Capston.entity.Diet.Supplement.SupplementLog;
import java.time.LocalDateTime;
import lombok.Data;

@Data
public class SupplementLogResponse {
  private Long supplementLogId;
  private SupplementData supplementData;
  private LocalDateTime date;

  public static SupplementLogResponse toDto(SupplementLog supplementLog) {
    SupplementLogResponse res = new SupplementLogResponse();
    res.setSupplementLogId(supplementLog.getSupplementLogId());
    res.setSupplementData(supplementLog.getSupplementData());
    res.setDate(supplementLog.getDate());

    return res;
  }
}
