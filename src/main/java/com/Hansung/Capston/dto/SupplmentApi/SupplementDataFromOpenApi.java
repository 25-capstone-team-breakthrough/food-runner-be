package com.Hansung.Capston.dto.SupplmentApi;

import com.Hansung.Capston.entity.SupplementData;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SupplementDataFromOpenApi { //OpenApi로부터 데이터를 받아내기 위한 DTO

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

    public SupplementData toEntity() {
        return SupplementData.builder()
                .supplementId(Long.valueOf(this.supplementId))
                .company(company != null ? company : "제조업체 정보 없음") // 기본값 설정
                .supplementName(supplementName != null ? supplementName : "영양제 이름 없음")
                .usageMethod(usageMethod != null ? usageMethod : "사용 방법 정보 없음")
                .preservationPeriod(preservationPeriod != null ? preservationPeriod : "보관 기간 정보 없음")
                .intakeInformation(intakeInformation != null ? intakeInformation : "섭취 정보 없음")
                .mainFunction(mainFunction != null ? mainFunction : "주요 기능 정보 없음")
                .baseStandard(baseStandard != null ? baseStandard : "기본 기준 정보 없음")
                .supplementImage(supplementImage != null ? supplementImage : "이미지 없음")
                .build();
    }
}
