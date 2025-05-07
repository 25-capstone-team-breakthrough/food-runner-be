package com.Hansung.Capston.entity;

import com.Hansung.Capston.entity.UserInfo.User;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "Meal_Log")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MealLog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // MySQL AUTO_INCREMENT 적용
    @Column(name = "meal_id", nullable = false)
    private Long mealId;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false) // User 테이블과 관계 설정
    private User user;

    @Enumerated(EnumType.STRING)
    @Column(name = "type", nullable = false)
    private MealType type; // ENUM ('search', 'image')

    // ✅ NULL 허용 + 기본값 0 설정
    @Column(name = "calories", columnDefinition = "DECIMAL(10,2) DEFAULT 0", nullable = true)
    private Double calories;

    @Column(name = "protein", columnDefinition = "DECIMAL(10,2) DEFAULT 0", nullable = true)
    private Double protein;

    @Column(name = "carbohydrate", columnDefinition = "DECIMAL(10,2) DEFAULT 0", nullable = true)
    private Double carbohydrate;

    @Column(name = "fat", columnDefinition = "DECIMAL(10,2) DEFAULT 0", nullable = true)
    private Double fat;

    @Column(name = "sugar", columnDefinition = "DECIMAL(10,2) DEFAULT 0", nullable = true)
    private Double sugar;

    @Column(name = "sodium", columnDefinition = "DECIMAL(10,2) DEFAULT 0", nullable = true)
    private Double sodium;

    @Column(name = "dietary_fiber", columnDefinition = "DECIMAL(10,2) DEFAULT 0", nullable = true)
    private Double dietaryFiber;

    @Column(name = "calcium", columnDefinition = "DECIMAL(10,2) DEFAULT 0", nullable = true)
    private Double calcium;

    @Column(name = "saturated_fat", columnDefinition = "DECIMAL(10,2) DEFAULT 0", nullable = true)
    private Double saturatedFat;

    @Column(name = "trans_fat", columnDefinition = "DECIMAL(10,2) DEFAULT 0", nullable = true)
    private Double transFat;

    @Column(name = "cholesterol", columnDefinition = "DECIMAL(10,2) DEFAULT 0", nullable = true)
    private Double cholesterol;

    @Column(name = "vitamin_a", columnDefinition = "DECIMAL(10,2) DEFAULT 0", nullable = true)
    private Double vitaminA;

    @Column(name = "vitamin_b1", columnDefinition = "DECIMAL(10,2) DEFAULT 0", nullable = true)
    private Double vitaminB1;

    @Column(name = "vitamin_c", columnDefinition = "DECIMAL(10,2) DEFAULT 0", nullable = true)
    private Double vitaminC;

    @Column(name = "vitamin_d", columnDefinition = "DECIMAL(10,2) DEFAULT 0", nullable = true)
    private Double vitaminD;

    @Column(name = "vitamin_e", columnDefinition = "DECIMAL(10,2) DEFAULT 0", nullable = true)
    private Double vitaminE;

    @Column(name = "magnesium", columnDefinition = "DECIMAL(10,2) DEFAULT 0", nullable = true)
    private Double magnesium;

    @Column(name = "zinc", columnDefinition = "DECIMAL(10,2) DEFAULT 0", nullable = true)
    private Double zinc;

    @Column(name = "lactium", columnDefinition = "DECIMAL(10,2) DEFAULT 0", nullable = true)
    private Double lactium;

    @Column(name = "potassium", columnDefinition = "DECIMAL(10,2) DEFAULT 0", nullable = true)
    private Double potassium;

    @Column(name = "l_arginine", columnDefinition = "DECIMAL(10,2) DEFAULT 0", nullable = true)
    private Double lArginine;

    @Column(name = "omega3", columnDefinition = "DECIMAL(10,2) DEFAULT 0", nullable = true)
    private Double omega3;

    @Column(name = "date", nullable = false)
    private LocalDateTime date;
}
