package com.Hansung.Capston.entity;

import com.Hansung.Capston.entity.UserInfo.User;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "Preferred_Food")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PreferredFood {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY) // AUTO_INCREMENT 적용
  @Column(name = "prefood_id")
  private Long prefoodId;

  @ManyToOne
  @JoinColumn(name = "food_id", nullable = false) // Food_Data 테이블과 연관
  private FoodData foodData;

  @ManyToOne
  @JoinColumn(name = "user_id", nullable = false) // User 테이블과 연관
  private User user;
}
