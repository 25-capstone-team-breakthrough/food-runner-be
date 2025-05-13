package com.Hansung.Capston.entity.Diet.Nutrient;

import com.Hansung.Capston.entity.UserInfo.User;
import jakarta.persistence.*;
import java.time.LocalDate;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "Nutrition_Log")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class NutritionLog {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY) // MySQL AUTO_INCREMENT 적용
  @Column(name = "nutrition_log_id")
  private Long nutritionLogId;

  @ManyToOne
  @JoinColumn(name = "user_id", nullable = false)
  private User user;

  @Column(name = "calories", nullable = false, columnDefinition = "DECIMAL(10,2) DEFAULT 0")
  private Double calories = 0.0;

  @Column(name = "protein", nullable = false, columnDefinition = "DECIMAL(10,2) DEFAULT 0")
  private Double protein = 0.0;

  @Column(name = "carbohydrate", nullable = false, columnDefinition = "DECIMAL(10,2) DEFAULT 0")
  private Double carbohydrate = 0.0;

  @Column(name = "fat", nullable = false, columnDefinition = "DECIMAL(10,2) DEFAULT 0")
  private Double fat = 0.0;

  @Column(name = "sugar", nullable = false, columnDefinition = "DECIMAL(10,2) DEFAULT 0")
  private Double sugar = 0.0;

  @Column(name = "sodium", nullable = false, columnDefinition = "DECIMAL(10,2) DEFAULT 0")
  private Double sodium = 0.0;

  @Column(name = "dietary_fiber", nullable = false, columnDefinition = "DECIMAL(10,2) DEFAULT 0")
  private Double dietaryFiber = 0.0;

  @Column(name = "calcium", nullable = false, columnDefinition = "DECIMAL(10,2) DEFAULT 0")
  private Double calcium = 0.0;

  @Column(name = "saturated_fat", nullable = false, columnDefinition = "DECIMAL(10,2) DEFAULT 0")
  private Double saturatedFat = 0.0;

  @Column(name = "trans_fat", nullable = false, columnDefinition = "DECIMAL(10,2) DEFAULT 0")
  private Double transFat = 0.0;

  @Column(name = "cholesterol", nullable = false, columnDefinition = "DECIMAL(10,2) DEFAULT 0")
  private Double cholesterol = 0.0;

  @Column(name = "vitamin_a", nullable = false, columnDefinition = "DECIMAL(10,2) DEFAULT 0")
  private Double vitaminA = 0.0;

  @Column(name = "vitamin_b1", nullable = false, columnDefinition = "DECIMAL(10,2) DEFAULT 0")
  private Double vitaminB1 = 0.0;

  @Column(name = "vitamin_c", nullable = false, columnDefinition = "DECIMAL(10,2) DEFAULT 0")
  private Double vitaminC = 0.0;

  @Column(name = "vitamin_d", nullable = false, columnDefinition = "DECIMAL(10,2) DEFAULT 0")
  private Double vitaminD = 0.0;

  @Column(name = "vitamin_e", nullable = false, columnDefinition = "DECIMAL(10,2) DEFAULT 0")
  private Double vitaminE = 0.0;

  @Column(name = "magnesium", nullable = false, columnDefinition = "DECIMAL(10,2) DEFAULT 0")
  private Double magnesium = 0.0;

  @Column(name = "zinc", nullable = false, columnDefinition = "DECIMAL(10,2) DEFAULT 0")
  private Double zinc = 0.0;

  @Column(name = "lactium", nullable = false, columnDefinition = "DECIMAL(10,2) DEFAULT 0")
  private Double lactium = 0.0;

  @Column(name = "potassium", nullable = false, columnDefinition = "DECIMAL(10,2) DEFAULT 0")
  private Double potassium = 0.0;

  @Column(name = "l_arginine", nullable = false, columnDefinition = "DECIMAL(10,2) DEFAULT 0")
  private Double lArginine = 0.0;

  @Column(name = "omega3", nullable = false, columnDefinition = "DECIMAL(10,2) DEFAULT 0")
  private Double omega3 = 0.0;

  @Column(name = "date", nullable = false)
  private LocalDate date;
}
