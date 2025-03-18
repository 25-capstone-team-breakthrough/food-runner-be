package com.Hansung.Capston.entity;

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

  @ManyToOne
  @JoinColumn(name = "user_id", nullable = false) // User 테이블과 관계 설정
  private User user;

//  @ManyToOne
//  @JoinColumn(name = "food_id", nullable = false)
//  private Food food;
}
