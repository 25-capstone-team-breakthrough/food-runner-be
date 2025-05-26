package com.Hansung.Capston.entity.Diet.Recipe;

import com.Hansung.Capston.common.DietType;
import com.Hansung.Capston.entity.UserInfo.User;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder // 빌더 패턴 사용 시 편리
@Table(name = "recommended_recipe_candidate")
public class RecommendedRecipeCandidate {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "user_id", nullable = false)
  private User user;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "recipe_data_id", nullable = false)
  private RecipeData recipeData;

  @Enumerated(EnumType.STRING)
  @Column(name = "diet_type", nullable = false)
  private DietType dietType;
}
