package com.Hansung.Capston.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "Preferred_Supplement")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PreferredSupplement {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY) // AUTO_INCREMENT 적용
  @Column(name = "presupplement_id")
  private Long presupplementId;

  @ManyToOne
  @JoinColumn(name = "supplement_id", nullable = false) // Food_Data 테이블과 연관
  private SupplementData supplementData;

  @ManyToOne
  @JoinColumn(name = "user_id", nullable = false) // User 테이블과 연관
  private User user;
}
