package com.Hansung.Capston.entity.DataSet;


import com.opencsv.bean.CsvBindByName;
import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
@Table(name = "Food_Data")
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
  }


}

