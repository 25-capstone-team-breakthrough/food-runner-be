package com.Hansung.Capston.entity.DataSet;

import com.opencsv.bean.CsvBindByName;
import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
@Table(name = "food_data")
public class FoodData {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "food_id")
  private Long foodId;

  @CsvBindByName(column = "food_name")
  @Column(name = "food_name")
  private String foodName;

  @CsvBindByName(column = "calories")
  @Column(name = "calories")
  private Double calories;

  @CsvBindByName(column = "protein")
  @Column(name = "protein")
  private Double protein;

  @CsvBindByName(column = "fat")
  @Column(name = "fat")
  private Double fat;

  @CsvBindByName(column = "carbohydrate")
  @Column(name = "carbohydrate")
  private Double carbohydrate;

  @CsvBindByName(column = "sugar")
  @Column(name = "sugar")
  private Double sugar;

  @CsvBindByName(column = "dietary_fiber")
  @Column(name = "dietary_fiber")
  private Double dietaryFiber;

  @CsvBindByName(column = "calcium")
  @Column(name = "calcium")
  private Double calcium;

  @CsvBindByName(column = "potassium")
  @Column(name = "potassium")
  private Double potassium;

  @CsvBindByName(column = "sodium")
  @Column(name = "sodium")
  private Double sodium;

  @CsvBindByName(column = "vitamin_a")
  @Column(name = "vitamin_a")
  private Double vitaminA;

  @CsvBindByName(column = "vitamin_b1")
  @Column(name = "vitamin_b1")
  private Double vitaminB1;

  @CsvBindByName(column = "vitamin_c")
  @Column(name = "vitamin_c")
  private Double vitaminC;

  @CsvBindByName(column = "vitamin_d")
  @Column(name = "vitamin_d")
  private Double vitaminD;

  @CsvBindByName(column = "cholesterol")
  @Column(name = "cholesterol")
  private Double cholesterol;

  @CsvBindByName(column = "saturated_fat")
  @Column(name = "saturated_fat")
  private Double saturatedFat;

  @CsvBindByName(column = "trans_fat")
  @Column(name = "trans_fat")
  private Double transFat;

  @CsvBindByName(column = "food_company")
  @Column(name = "food_company")
  private String foodCompany;

  @CsvBindByName(column = "food_image")
  @Column(name = "food_image", columnDefinition = "TEXT")
  private String foodImage;

  @CsvBindByName(column = "vitamin_e")
  @Column(name = "vitamin_e")
  private Double vitaminE;

  @CsvBindByName(column = "zinc")
  @Column(name = "zinc")
  private Double zinc;

  @CsvBindByName(column = "magnesium")
  @Column(name = "magnesium")
  private Double magnesium;

  @CsvBindByName(column = "lactium")
  @Column(name = "lactium")
  private Double lactium;

  @CsvBindByName(column = "omega3")
  @Column(name = "omega3")
  private Double omega3;

  @CsvBindByName(column = "l_arginine")
  @Column(name = "l_arginine")
  private Double lArginine;

  @PrePersist
  @PreUpdate
  public void setDefaults() {
    if (calories == null) calories = 0.0;
    if (protein == null) protein = 0.0;
    if (fat == null) fat = 0.0;
    if (carbohydrate == null) carbohydrate = 0.0;
    if (sugar == null) sugar = 0.0;
    if (dietaryFiber == null) dietaryFiber = 0.0;
    if (calcium == null) calcium = 0.0;
    if (potassium == null) potassium = 0.0;
    if (sodium == null) sodium = 0.0;
    if (vitaminA == null) vitaminA = 0.0;
    if (vitaminB1 == null) vitaminB1 = 0.0;
    if (vitaminC == null) vitaminC = 0.0;
    if (vitaminD == null) vitaminD = 0.0;
    if (cholesterol == null) cholesterol = 0.0;
    if (saturatedFat == null) saturatedFat = 0.0;
    if (transFat == null) transFat = 0.0;
    if (vitaminE == null) vitaminE = 0.0;
    if (zinc == null) zinc = 0.0;
    if (magnesium == null) magnesium = 0.0;
    if (lactium == null) lactium = 0.0;
    if (omega3 == null) omega3 = 0.0;
    if (lArginine == null) lArginine = 0.0;
    if (foodImage == null) foodImage = "";
  }
}
