package com.Hansung.Capston.entity.DataSet;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "Food_Data")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FoodData {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY) // AUTO_INCREMENT 적용
  @Column(name = "food_id")
  private Long foodId;

  @Column(name = "food_image", length = 255)
  private String foodImage;

  @Column(name = "food_name", nullable = false, length = 255)
  private String foodName;

  @Column(name = "food_company", length = 255)
  private String foodCompany;

  @Column(name = "calories", columnDefinition = "DECIMAL(10,2) DEFAULT 0")
  private Double calories;

  @Column(name = "protein", columnDefinition = "DECIMAL(10,2) DEFAULT 0")
  private Double protein;

  @Column(name = "carbohydrate", columnDefinition = "DECIMAL(10,2) DEFAULT 0")
  private Double carbohydrate;

  @Column(name = "fat", columnDefinition = "DECIMAL(10,2) DEFAULT 0")
  private Double fat;

  @Column(name = "sugar", columnDefinition = "DECIMAL(10,2) DEFAULT 0")
  private Double sugar;

  @Column(name = "sodium", columnDefinition = "DECIMAL(10,2) DEFAULT 0")
  private Double sodium;

  @Column(name = "dietary_fiber", columnDefinition = "DECIMAL(10,2) DEFAULT 0")
  private Double dietaryFiber;

  @Column(name = "calcium", columnDefinition = "DECIMAL(10,2) DEFAULT 0")
  private Double calcium;

  @Column(name = "saturated_fat", columnDefinition = "DECIMAL(10,2) DEFAULT 0")
  private Double saturatedFat;

  @Column(name = "trans_fat", columnDefinition = "DECIMAL(10,2) DEFAULT 0")
  private Double transFat;

  @Column(name = "cholesterol", columnDefinition = "DECIMAL(10,2) DEFAULT 0")
  private Double cholesterol;

  @Column(name = "vitamin_a", columnDefinition = "DECIMAL(10,2) DEFAULT 0")
  private Double vitaminA;

  @Column(name = "vitamin_b1", columnDefinition = "DECIMAL(10,2) DEFAULT 0")
  private Double vitaminB1;

  @Column(name = "vitamin_c", columnDefinition = "DECIMAL(10,2) DEFAULT 0")
  private Double vitaminC;

  @Column(name = "vitamin_d", columnDefinition = "DECIMAL(10,2) DEFAULT 0")
  private Double vitaminD;

  @Column(name = "vitamin_e", columnDefinition = "DECIMAL(10,2) DEFAULT 0")
  private Double vitaminE;

  @Column(name = "magnesium", columnDefinition = "DECIMAL(10,2) DEFAULT 0")
  private Double magnesium;

  @Column(name = "zinc", columnDefinition = "DECIMAL(10,2) DEFAULT 0")
  private Double zinc;

  @Column(name = "lactium", columnDefinition = "DECIMAL(10,2) DEFAULT 0")
  private Double lactium;

  @Column(name = "potassium", columnDefinition = "DECIMAL(10,2) DEFAULT 0")
  private Double potassium;

  @Column(name = "l_arginine", columnDefinition = "DECIMAL(10,2) DEFAULT 0")
  private Double lArginine;

  @Column(name = "omega3", columnDefinition = "DECIMAL(10,2) DEFAULT 0")
  private Double omega3;
}
