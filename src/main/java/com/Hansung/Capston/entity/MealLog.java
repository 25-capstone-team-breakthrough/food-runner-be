package com.Hansung.Capston.entity;

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

    @Column(name = "calories", nullable = false, columnDefinition = "DECIMAL(10,2) DEFAULT 0")
    private Double calories;

    @Column(name = "protein", nullable = false, columnDefinition = "DECIMAL(10,2) DEFAULT 0")
    private Double protein;

    @Column(name = "carbohydrate", nullable = false, columnDefinition = "DECIMAL(10,2) DEFAULT 0")
    private Double carbohydrate;

    @Column(name = "fat", nullable = false, columnDefinition = "DECIMAL(10,2) DEFAULT 0")
    private Double fat;

    @Column(name = "sugar", nullable = false, columnDefinition = "DECIMAL(10,2) DEFAULT 0")
    private Double sugar;

    @Column(name = "sodium", nullable = false, columnDefinition = "DECIMAL(10,2) DEFAULT 0")
    private Double sodium;

    @Column(name = "dietary_fiber", nullable = false, columnDefinition = "DECIMAL(10,2) DEFAULT 0")
    private Double dietaryFiber;

    @Column(name = "calcium", nullable = false, columnDefinition = "DECIMAL(10,2)")
    private Double calcium;

    @Column(name = "saturated_fat", nullable = false, columnDefinition = "DECIMAL(10,2)")
    private Double saturatedFat;

    @Column(name = "trans_fat", nullable = false, columnDefinition = "DECIMAL(10,2)")
    private Double transFat;

    @Column(name = "cholesterol", nullable = false, columnDefinition = "DECIMAL(10,2)")
    private Double cholesterol;

    @Column(name = "vitamin_a", nullable = false, columnDefinition = "DECIMAL(10,2)")
    private Double vitaminA;

    @Column(name = "vitamin_b1", nullable = false, columnDefinition = "DECIMAL(10,2)")
    private Double vitaminB1;

    @Column(name = "vitamin_c", nullable = false, columnDefinition = "DECIMAL(10,2)")
    private Double vitaminC;

    @Column(name = "vitamin_d", nullable = false, columnDefinition = "DECIMAL(10,2)")
    private Double vitaminD;

    @Column(name = "vitamin_e", nullable = false, columnDefinition = "DECIMAL(10,2)")
    private Double vitaminE;

    @Column(name = "magnesium", nullable = false, columnDefinition = "DECIMAL(10,2)")
    private Double magnesium;

    @Column(name = "zinc", nullable = false, columnDefinition = "DECIMAL(10,2)")
    private Double zinc;

    @Column(name = "lactium", nullable = false, columnDefinition = "DECIMAL(10,2)")
    private Double lactium;

    @Column(name = "potassium", nullable = false, columnDefinition = "DECIMAL(10,2)")
    private Double potassium;

    @Column(name = "l_arginine", nullable = false, columnDefinition = "DECIMAL(10,2)")
    private Double lArginine;

    @Column(name = "omega3", nullable = false, columnDefinition = "DECIMAL(10,2)")
    private Double omega3;

    @Column(name = "date", nullable = false)
    private LocalDateTime date;
}

