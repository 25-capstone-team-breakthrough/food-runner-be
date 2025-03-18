package com.Hansung.Capston.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "Supplement_Data")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SupplementData {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY) // AUTO_INCREMENT 적용
  @Column(name = "supplement_id")
  private Long supplementId;

  @Column(name = "company", nullable = false)
  private String company; // 제조업체

  @Column(name = "supplement_name", nullable = false)
  private String supplementName; // 영양제 이름

  @Column(name = "distribution_period", nullable = false)
  private String distributionPeriod; // 유통기한

  @Column(name = "usage_method", nullable = false, columnDefinition = "TEXT")
  private String usageMethod; // 사용 방법

  @Column(name = "preservation_period", nullable = false)
  private String preservationPeriod; // 보관기간

  @Column(name = "intake_information", nullable = false, columnDefinition = "TEXT")
  private String intakeInformation; // 섭취 정보

  @Column(name = "main_function", nullable = false, columnDefinition = "TEXT")
  private String mainFunction; // 주요 기능

  @Column(name = "base_standard", nullable = false, columnDefinition = "TEXT")
  private String baseStandard; // 기준 정보
}
