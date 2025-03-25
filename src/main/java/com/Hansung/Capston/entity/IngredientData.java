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
@Table(name = "Ingredient_Data")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class IngredientData {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "ingredient_id", nullable = false)
  Long ingredientId;

  @Column(name = "ingredient_name", nullable = false)
  String ingredientName;

  @Column(name = "ingredient_nutrition", nullable = false)
  String ingredientNutrition = "";

  @Column(name = "ingredient_cal", nullable = false)
  Double ingredientCal = 0.0;
}
