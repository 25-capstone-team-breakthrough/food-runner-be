package com.Hansung.Capston.entity.DataSet;

import com.opencsv.bean.CsvBindByName;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.checkerframework.checker.units.qual.C;

@Data
@Getter
@Setter
public class FoodCSV {

  @CsvBindByName(column = "food_name")
  private String foodName;

  @CsvBindByName(column = "calories")
  private Double calories = 0.0;

  @CsvBindByName(column = "protein") // CSV 열 이름과 매핑
  private Double protein = 0.0;

  @CsvBindByName(column = "fat")
  private Double fat = 0.0;

  @CsvBindByName(column = "carbohydrate")
  private Double carbohydrate = 0.0;

  @CsvBindByName(column = "sugar")
  private Double sugar = 0.0;

  @CsvBindByName(column = "dietary_fiber")
  private Double dietaryFiber = 0.0;

  @CsvBindByName(column = "calcium")
  private Double calcium = 0.0;

  @CsvBindByName(column = "potassium")
  private Double potassium = 0.0;

  @CsvBindByName(column = "sodium")
  private Double sodium = 0.0;

  @CsvBindByName(column = "vitamin_a")
  private Double vitaminA = 0.0;

  @CsvBindByName(column = "vitamin_b1")
  private Double vitaminB1 = 0.0;

  @CsvBindByName(column = "vitamin_c")
  private Double vitaminC = 0.0;

  @CsvBindByName(column = "vitamin_d")
  private Double vitaminD = 0.0;

  @CsvBindByName(column = "cholesterol")
  private Double cholesterol = 0.0;

  @CsvBindByName(column = "saturated_fat")
  private Double saturatedFat= 0.0;

  @CsvBindByName(column = "trans_fat")
  private Double transFat = 0.0;

  @CsvBindByName(column = "food_company")
  private String foodCompany;

  @CsvBindByName(column = "food_image")
  private String foodImage;

}
