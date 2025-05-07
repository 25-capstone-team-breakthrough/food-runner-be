package com.Hansung.Capston.entity;

import com.Hansung.Capston.entity.DataSet.IngredientData;
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
  private Integer id;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "ingredient_id", nullable = false)
  private IngredientData ingredient;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "user_id", nullable = false)
  private User user;
}
