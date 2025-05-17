package com.Hansung.Capston.entity.Diet.Recipe;

import com.opencsv.bean.CsvBindByName;
import io.opencensus.stats.Aggregation.Count;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Table(name = "recipe_data")
@Data
public class RecipeData {
  @Id
  @Column(name = "recipe_id")
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long recipeId;

  @CsvBindByName(column = "CKG_NM")
  @Column(name = "recipe_name")
  private String recipeName;

  @CsvBindByName(column = "INQ_CNT")
  @Column(name = "recommend_count")
  private int recommendedCount;

  @CsvBindByName(column = "CKG_MTRL_CN")
  @Column(name = "ingredients")
  private String ingredients;

  @CsvBindByName(column = "CKG_INBUN_NM")
  @Column(name = "serving")
  private String serving;

  @CsvBindByName(column = "RCP_IMG_URL")
  @Column(name = "recipe_image")
  private String recipeImage;

  @CsvBindByName(column = "RCP_INS")
  @Column(name = "recipe")
  private String recipe;

  @CsvBindByName(column = "cleaned_ingredients")
  @Column(name = "cleaned_ingredients")
  private String cleanedIngredients;

  @Column(name = "related_recipe_1")
  private String relatedRecipe1;

  @Column(name = "related_recipe_2")
  private String relatedRecipe2;

  @Column(name = "related_recipe_3")
  private String relatedRecipe3;

  @Column(name = "calories")
  private double calories;

  @Column(name = "carbohydrate")
  private double carbohydrate;

  @Column(name = "protein")
  private double protein;

  @Column(name = "fat")
  private double fat;
}
