package com.Hansung.Capston.entity.Diet.Meal;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "Image_Meal_Log")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ImageMealLog {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY) // MySQL AUTO_INCREMENT 적용
  @Column(name = "image_meal_log_id", nullable = false)
  private Long imageMealLogId;

  @OneToOne(cascade = CascadeType.ALL)
  @JoinColumn(name = "meal_id", nullable = false)
  private MealLog mealLog;  // MealLog와 연결

  @Column(name = "meal_name", nullable = false)
  private String mealName;

  @Lob
  @Column(name = "meal_image", nullable = false, columnDefinition = "TEXT")
  private String mealImage;
}
