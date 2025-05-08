package com.Hansung.Capston.entity.Exercise;


import com.opencsv.bean.CsvBindByName;
import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
@Table(name = "food_data")
public class FoodDataSet {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // AUTO_INCREMENT 적용
    @Column(name = "food_id")
    private Long foodId;

    @CsvBindByName(column = "food_name")
    private String foodNames;

    @CsvBindByName(column = "calories")
    private Double caloriess;

    @CsvBindByName(column = "protein") // CSV 열 이름과 매핑
    private Double proteins;

    @CsvBindByName(column = "fat")
    private Double fats;

    @CsvBindByName(column = "carbohydrate")
    private Double carbohydrates;

    @CsvBindByName(column = "sugar")
    private Double sugars;

    @CsvBindByName(column = "dietary_fiber")
    private Double dietaryFibers;

    @CsvBindByName(column = "calcium")
    private Double calciums;

    @CsvBindByName(column = "potassium")
    private Double potassiums;

    @CsvBindByName(column = "sodium")
    private Double sodiums;

    @CsvBindByName(column = "vitamin_a")
    private Double vitaminAs;

    @CsvBindByName(column = "vitamin_b1")
    private Double vitaminB1s;

    @CsvBindByName(column = "vitamin_c")
    private Double vitaminCs;

    @CsvBindByName(column = "vitamin_d")
    private Double vitaminDs;

    @CsvBindByName(column = "cholesterol")
    private Double cholesterols;

    @CsvBindByName(column = "saturated_fat")
    private Double saturatedFats;

    @CsvBindByName(column = "trans_fat")
    private Double transFats;

    @CsvBindByName(column = "food_company")
    private String foodCompanys;

    @CsvBindByName(column = "food_image")
    @Column(columnDefinition = "TEXT")
    private String foodImages;



}

