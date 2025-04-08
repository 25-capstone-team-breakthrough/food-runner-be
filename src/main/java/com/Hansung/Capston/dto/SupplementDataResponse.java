package com.Hansung.Capston.dto;

import com.Hansung.Capston.entity.SupplementData;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
public class SupplementDataResponse {
  @JsonProperty("PRDLST_REPORT_NO")
  private String supplementId;
  @JsonProperty("BSSH_NM")
  private String company;
  @JsonProperty("PRDLST_NM")
  private String supplementName;
  @JsonProperty("NTK_MTHD")
  private String usageMethod;
  @JsonProperty("POG_DAYCNT")
  private String preservationPeriod;
  @JsonProperty("CSTDY_MTHD")
  private String intakeInformation;
  @JsonProperty("PRIMARY_FNCLTY")
  private String mainFunction;
  @JsonProperty("INDIV_RAWMTRL_NM")
  private String baseStandard;
  @Builder.Default
  private String supplementImage = "";


  public static SupplementDataResponse fromEntity(SupplementData entity) {
    return SupplementDataResponse.builder()
        .supplementId(entity.getSupplementId())
        .company(entity.getCompany())
        .supplementName(entity.getSupplementName())
        .supplementImage(entity.getSupplementImage())
        .usageMethod(entity.getUsageMethod())
        .preservationPeriod(entity.getPreservationPeriod())
        .intakeInformation(entity.getIntakeInformation())
        .mainFunction(entity.getMainFunction())
        .baseStandard(entity.getBaseStandard())
        .build();
  }
}
