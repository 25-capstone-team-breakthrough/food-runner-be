package com.Hansung.Capston.entity;

import com.opencsv.bean.CsvBindByName;
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
  @Column(name = "sup_id", unique = true, nullable = false)
  private Long supplementId;

  @Column(name = "sup_company", nullable = false)
  private String company; // 제조업체

  @Column(name = "sup_name", nullable = false)
  private String supplementName; // 영양제 이름

  @Column(name = "sup_image")
  private String supplementImage;

  @Column(name = "usage_method", nullable = false, columnDefinition = "TEXT")
  private String usageMethod; // 사용 방법

  @Column(name = "preservation_period", nullable = false)
  private String preservationPeriod; // 보관기간

  @Column(name = "intake_information", nullable = false, columnDefinition = "TEXT")
  private String intakeInformation; // 섭취 정보

  @Column(name = "main_function", nullable = false, columnDefinition = "TEXT")
  private String mainFunction; // 주요 기능

  @Column(name = "calories")
  private Double calories;

  @Column(name = "protein") // CSV 열 이름과 매핑
  private Double protein;

  @Column(name = "fat")
  private Double fat;

  @Column(name = "carbohydrate")
  private Double carbohydrate;

  @Column(name = "sugar")
  private Double sugar;

  @Column(name = "dietary_fiber")
  private Double dietaryFiber;

  @Column(name = "calcium")
  private Double calcium;

  @Column(name = "potassium")
  private Double potassium;

  @Column(name = "sodium")
  private Double sodium;

  @Column(name = "vitamin_a")
  private Double vitaminA;

  @Column(name = "vitamin_b1")
  private Double vitaminB1;

  @Column(name = "vitamin_c")
  private Double vitaminC;

  @Column(name = "vitamin_d")
  private Double vitaminD;

  @Column(name = "cholesterol")
  private Double cholesterol;

  @Column(name = "saturated_fat")
  private Double saturatedFat;

  @Column(name = "trans_fat")
  private Double transFat;

  @Column(name = "vitamin_e")
  private Double vitaminE;

  @Column(name = "zinc")
  private Double zinc;

  @Column(name = "magnesium")
  private Double magnesium;

  @Column(name = "lactium")
  private Double lactium;

  @Column(name = "omega3")
  private Double omega3;

  @Column(name = "l_arginine")
  private Double lArginine;

}
