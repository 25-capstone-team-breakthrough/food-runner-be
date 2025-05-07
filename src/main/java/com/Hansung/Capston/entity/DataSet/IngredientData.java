package com.Hansung.Capston.entity.DataSet;


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
@Table(name = "ingredient_data")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class IngredientData {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long ingredientId;

  @Column(nullable = false)
  private String ingredientName;

  @Column(columnDefinition = "DECIMAL(10,2)")
  private Double calories;

  @Column(columnDefinition = "DECIMAL(10,2)")
  private Double protein;

  @Column(columnDefinition = "DECIMAL(10,2)")
  private Double carbohydrate;

  @Column(columnDefinition = "DECIMAL(10,2)")
  private Double fat;

  @Column(columnDefinition = "DECIMAL(10,2)")
  private Double sugar;

  @Column(columnDefinition = "DECIMAL(10,2)")
  private Double sodium;

  @Column(columnDefinition = "DECIMAL(10,2)")
  private Double dietaryFiber;

  @Column(columnDefinition = "DECIMAL(10,2)")
  private Double calcium;

  @Column(columnDefinition = "DECIMAL(10,2)")
  private Double saturatedFat;

  @Column(columnDefinition = "DECIMAL(10,2)")
  private Double transFat;

  @Column(columnDefinition = "DECIMAL(10,2)")
  private Double cholesterol;

  @Column(columnDefinition = "DECIMAL(10,2)")
  private Double vitaminA;

  @Column(columnDefinition = "DECIMAL(10,2)")
  private Double vitaminB1;

  @Column(columnDefinition = "DECIMAL(10,2)")
  private Double vitaminC;

  @Column(columnDefinition = "DECIMAL(10,2)")
  private Double vitaminD;

  @Column(columnDefinition = "DECIMAL(10,2)")
  private Double ash;

  private String ingredientImage;
}