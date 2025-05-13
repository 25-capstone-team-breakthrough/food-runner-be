package com.Hansung.Capston.dto.Diet.Supplement;

import com.Hansung.Capston.entity.Diet.Supplement.PreferredSupplement;
import com.Hansung.Capston.entity.Diet.Supplement.SupplementData;
import lombok.Data;

@Data
public class PreferredSupplementResponse {
  private Long presupplementId;
  private SupplementData supplementData;

  public static PreferredSupplementResponse toDto(PreferredSupplement preferredSupplement) {
    PreferredSupplementResponse response = new PreferredSupplementResponse();
    response.presupplementId = preferredSupplement.getPresupplementId();
    response.setSupplementData(preferredSupplement.getSupplementData());

    return response;
  }
}
