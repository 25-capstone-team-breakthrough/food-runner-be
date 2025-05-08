package com.Hansung.Capston.entity.DataSet;


import com.opencsv.bean.CsvBindByName;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "ingredient_data")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class IngredientData {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long ingredientId;

  @CsvBindByName(column = "ingredient_name")
  @Column(name = "ingredient_name", nullable = false)
  private String ingredientName;

  @CsvBindByName(column = "calories")
  private Double calories;

  @CsvBindByName(column = "protein") // CSV 열 이름과 매핑
  private Double protein;

  @CsvBindByName(column = "carbohydrate")
  private Double carbohydrate;

  @CsvBindByName(column = "fat")
  private Double fat;

  @CsvBindByName(column = "sugar")
  private Double sugar;

  @CsvBindByName(column = "sodium")
  private Double sodium;

  @CsvBindByName(column = "dietary_fiber")
  private Double dietaryFiber;

  @CsvBindByName(column = "calcium")
  private Double calcium;

  @CsvBindByName(column = "saturated_fat")
  private Double saturatedFat;

  @CsvBindByName(column = "trans_fat")
  private Double transFat;

  @CsvBindByName(column = "cholesterol")
  private Double cholesterol;

  @CsvBindByName(column = "vitamin_a")
  private Double vitaminA;

  @CsvBindByName(column = "vitamin_b1")
  private Double vitaminB1;

  @CsvBindByName(column = "vitamin_c")
  private Double vitaminC;

  @CsvBindByName(column = "vitamin_d")
  private Double vitaminD;

  @CsvBindByName(column = "potassium")
  private Double potassium;

  @CsvBindByName(column = "image_url")
  @Column(columnDefinition = "TEXT")
  private String ingredientImage;

  @CsvBindByName(column = "vitamin_e")
  private Double vitaminE;

  @CsvBindByName(column = "zinc")
  private Double zinc;

  @CsvBindByName(column = "magnesium")
  private Double magnesium;

  @CsvBindByName(column = "lactium")
  private Double lactium;

  @CsvBindByName(column = "omega3")
  private Double omega3;

  @CsvBindByName(column = "l_arginine")
  private Double lArginine;

}