package com.Hansung.Capston.entity.Diet.Ingredient;

import com.Hansung.Capston.entity.UserInfo.User;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "Recommended_Ingredient")
@Getter
@Setter
public class RecommendedIngredient {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "recommendation_ingredient_id")
  private Long id;

  @ManyToOne(fetch = FetchType.EAGER)
  @JoinColumn(name = "ingredient_id", nullable = false)
  private IngredientData ingredient;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "user_id", nullable = false)
  private User user;
}
