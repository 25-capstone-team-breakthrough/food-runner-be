package com.Hansung.Capston.entity.DataSet;


import com.opencsv.bean.CsvBindByName;
import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
@Table(name = "food_data")
public class FoodData {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY) // AUTO_INCREMENT 적용
  @Column(name = "food_id")
  private Long foodId;

  @CsvBindByName(column = "food_name")
  @Column(name = "food_name")
  private String foodName;

  @CsvBindByName(column = "calories")
  private Double calories;

  @CsvBindByName(column = "protein") // CSV 열 이름과 매핑
  private Double protein;

  @CsvBindByName(column = "fat")
  private Double fat;

  @CsvBindByName(column = "carbohydrate")
  private Double carbohydrate;

  @CsvBindByName(column = "sugar")
  private Double sugar;

  @CsvBindByName(column = "dietary_fiber")
  private Double dietaryFiber;

  @CsvBindByName(column = "calcium")
  private Double calcium;

  @CsvBindByName(column = "potassium")
  private Double potassium;

  @CsvBindByName(column = "sodium")
  private Double sodium;

  @CsvBindByName(column = "vitamin_a")
  private Double vitaminA;

  @CsvBindByName(column = "vitamin_b1")
  private Double vitaminB1;

  @CsvBindByName(column = "vitamin_c")
  private Double vitaminC;

  @CsvBindByName(column = "vitamin_d")
  private Double vitaminD;

  @CsvBindByName(column = "cholesterol")
  private Double cholesterol;

  @CsvBindByName(column = "saturated_fat")
  private Double saturatedFat;

  @CsvBindByName(column = "trans_fat")
  private Double transFat;

  @CsvBindByName(column = "food_company")
  private String foodCompany;

  @CsvBindByName(column = "food_image")
  @Column(columnDefinition = "TEXT")
  private String foodImage;

  @Column(name = "vitamin_e")
  private Double vitaminE;

  @Column(name = "zinc")
  private Double zinc;

  @Column(name = "magnesium")
  private Double magnesium;

  @Column(name = "lactium")
  private Double lactium;

  @Column(name = "omega3")
  private Double omega3;

  @Column(name = "l_arginine")
  private Double lArginine;

}

