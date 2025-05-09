package com.Hansung.Capston.entity.Diet.Nutrient;

import com.Hansung.Capston.common.NutritionType;
import com.Hansung.Capston.entity.UserInfo.User;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "Recommended_Nutrition")
@Getter
@Setter
public class RecommendedNutrient {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "recommended_nutrition_id")
  private Integer id;

  @ManyToOne(fetch = FetchType.EAGER)
  @JoinColumn(name = "user_id", nullable = false)
  private User user;

  @Enumerated(EnumType.STRING)
  @Column(name = "nutrition_type", nullable = false)
  private NutritionType type; // MIN, RECOMMENDED, MAX

  @Column(name = "inbody_info")
  private Boolean inbodyInfo = false;

  @Column(name = "calories")
  private Double calories = 0.0;

  @Column(name = "protein")
  private Double protein = 0.0;

  @Column(name = "carbohydrate")
  private Double carbohydrate = 0.0;

  @Column(name = "fat")
  private Double fat = 0.0;

  @Column(name = "sugar")
  private Double sugar = 0.0;

  @Column(name = "saturated_fat")
  private Double saturatedFat = 0.0;

  @Column(name = "trans_fat")
  private Double transFat = 0.0;

  @Column(name = "cholesterol")
  private Double cholesterol = 0.0;

  @Column(name = "sodium")
  private Double sodium = 0.0;

  @Column(name = "dietary_fiber")
  private Double dietaryFiber = 0.0;

  @Column(name = "calcium")
  private Double calcium = 0.0;

  @Column(name = "vitamin_a")
  private Double vitaminA = 0.0;

  @Column(name = "vitamin_b1")
  private Double vitaminB1 = 0.0;

  @Column(name = "vitamin_c")
  private Double vitaminC = 0.0;

  @Column(name = "vitamin_d")
  private Double vitaminD = 0.0;

  @Column(name = "vitamin_e")
  private Double vitaminE = 0.0;

  @Column(name = "magnesium")
  private Double magnesium = 0.0;

  @Column(name = "zinc")
  private Double zinc = 0.0;

  @Column(name = "potassium")
  private Double potassium = 0.0;

  @Column(name = "l_arginine")
  private Double lArginine = 0.0;

  @Column(name = "omega3")
  private Double omega3 = 0.0;

  @Column(name = "lactium")
  private Double lactium = 0.0;
}
