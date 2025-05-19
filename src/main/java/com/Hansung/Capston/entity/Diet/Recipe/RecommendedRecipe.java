package com.Hansung.Capston.entity.Diet.Recipe;

import com.Hansung.Capston.common.DayOfWeek;
import com.Hansung.Capston.common.DietType;
import com.Hansung.Capston.common.MealType;
import com.Hansung.Capston.entity.UserInfo.User;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.time.LocalDate;
import lombok.Data;

@Entity
@Table(name = "recommended_recipe")
@Data
public class RecommendedRecipe {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "recommended_recipe_id")
  private Long recommendedRecipeId;

  @ManyToOne(fetch = FetchType.EAGER)
  @JoinColumn(name = "user_id", referencedColumnName = "user_id")
  private User user;  // User 엔티티와의 관계 (user_id)

  @ManyToOne(fetch = FetchType.EAGER)
  @JoinColumn(name = "recipe_id", referencedColumnName = "recipe_id")
  private RecipeData recipeData;  // RecipeData 엔티티와의 관계 (recipe_id)

  @Enumerated(EnumType.STRING)
  @Column(name = "type")
  private DietType type;  // Enum type (  BREAKFAST,LUNCH,DINNER)

  @Column(name = "date", nullable = false)
  private DayOfWeek date;  // 날짜 필드
}
