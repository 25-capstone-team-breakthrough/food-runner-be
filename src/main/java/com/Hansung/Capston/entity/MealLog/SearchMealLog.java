package com.Hansung.Capston.entity.MealLog;

import com.Hansung.Capston.entity.UserInfo.User;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "Search_Meal_Log")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SearchMealLog {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY) // MySQL AUTO_INCREMENT 적용
  @Column(name = "search_meal_log_id", nullable = false)
  private Long searchMealLogId;

  @OneToOne(cascade = CascadeType.ALL)
  @JoinColumn(name = "meal_id", nullable = false)
  private MealLog mealLog;  // MealLog와 연결

  @Column(name = "food_id", nullable = false)
  private Long foodId;

  @Column(name = "food_name", nullable = false)
  private String foodName;

  @Column(name = "food_image", nullable = false, columnDefinition = "TEXT")
  private String foodImage;
}
