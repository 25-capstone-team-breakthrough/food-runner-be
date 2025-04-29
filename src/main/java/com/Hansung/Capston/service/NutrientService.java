package com.Hansung.Capston.service;

import com.Hansung.Capston.dto.FoodDataDTO;
import com.Hansung.Capston.entity.FoodData;
import com.Hansung.Capston.repository.FoodDataRepository;
import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class NutrientService {
  private final FoodDataRepository foodDataRepository;

  @Autowired
  public NutrientService(FoodDataRepository foodDataRepository) {
    this.foodDataRepository = foodDataRepository;
  }


  public List<FoodDataDTO> checkNutrientData(String[] foods, OpenAiApiService openAiApiService) {
    List<FoodDataDTO> foodDataDTOS = new ArrayList<>();

    for (String food : foods) {
     List<FoodData> foodDataList = foodDataRepository.findByFoodName(food);

     if(foodDataList.isEmpty()) {
       FoodDataDTO nutrientInfo = openAiApiService.getNutrientInfo(food);
       foodDataRepository.save(nutrientInfo.toEntity());
       foodDataDTOS.add(nutrientInfo);
     } else{
       foodDataDTOS.add(FoodDataDTO.fromEntity(foodDataList.get(0)));
     }

   }

    return foodDataDTOS;
  }

}
