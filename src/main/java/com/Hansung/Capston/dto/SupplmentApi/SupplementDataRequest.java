package com.Hansung.Capston.dto.SupplmentApi;

import com.Hansung.Capston.entity.SupplementData;
import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SupplementDataRequest {

  private String company;
  private String supplementName;
  private String usageMethod;
  private String preservationPeriod;
  private String intakeInformation;
  private String mainFunction;
  private String baseStandard;

  @Builder.Default
  private String supplementImage = "";

  public SupplementDataFromOpenApi fromEntity(SupplementData entity) {
    return SupplementDataFromOpenApi.builder()
            .company(entity.getCompany()) // company 기본값 설정
            .supplementName(entity.getSupplementName()) // supplementName 기본값 설정
            .usageMethod(entity.getUsageMethod()) // usageMethod 기본값 설정
            .preservationPeriod(entity.getPreservationPeriod()) // preservationPeriod 기본값 설정
            .intakeInformation(entity.getIntakeInformation()) // intakeInformation 기본값 설정
            .mainFunction(entity.getMainFunction()) // mainFunction 기본값 설정
            .baseStandard(entity.getBaseStandard()) // baseStandard 기본값 설정
            .supplementImage(entity.getSupplementImage()) // supplementImage 기본값 설정
            .build();
  }

}
