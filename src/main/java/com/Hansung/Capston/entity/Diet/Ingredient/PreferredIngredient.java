package com.Hansung.Capston.entity.Diet.Ingredient;

import com.Hansung.Capston.entity.UserInfo.User;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "Preferred_Ingredient")
@Getter
@Setter
public class PreferredIngredient {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "preingredient_id")
  private Integer id;

  @ManyToOne(fetch = FetchType.EAGER)
  @JoinColumn(name = "ingredient_id", nullable = false)
  private IngredientData ingredient;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "user_id", nullable = false)
  private User user;
}
