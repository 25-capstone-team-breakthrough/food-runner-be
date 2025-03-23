package com.Hansung.Capston.dto;

import com.Hansung.Capston.entity.SupplementData;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
public class SupplementDataResponse {
  private Long supplementId;
  private String company;
  private String supplementName;
  private String supplementImage;
  private String distributionPeriod;
  private String usageMethod;
  private String preservationPeriod;
  private String intakeInformation;
  private String mainFunction;
  private String baseStandard;

  public static SupplementDataResponse fromEntity(SupplementData entity) {
    return SupplementDataResponse.builder()
        .supplementId(entity.getSupplementId())
        .company(entity.getCompany())
        .supplementName(entity.getSupplementName())
        .supplementImage(entity.getSupplementImage())
        .distributionPeriod(entity.getDistributionPeriod())
        .usageMethod(entity.getUsageMethod())
        .preservationPeriod(entity.getPreservationPeriod())
        .intakeInformation(entity.getIntakeInformation())
        .mainFunction(entity.getMainFunction())
        .baseStandard(entity.getBaseStandard())
        .build();
  }
}
