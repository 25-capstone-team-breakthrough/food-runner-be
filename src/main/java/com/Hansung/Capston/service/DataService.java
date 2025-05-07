package com.Hansung.Capston.service;

import com.Hansung.Capston.dto.MealLog.FoodDataDTO;
import com.Hansung.Capston.dto.SupplmentApi.SupplementDataFromOpenApi;
import com.Hansung.Capston.entity.FoodData;
import com.Hansung.Capston.entity.SupplementData;
import com.Hansung.Capston.repository.FoodDataRepository;
import com.Hansung.Capston.repository.SupplementDataRepository;
import java.util.List;
import java.util.stream.Collectors;

import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DataService {
  private final FoodDataRepository foodDataRepository;
  private final SupplementDataRepository supplementDataRepository;

  @Autowired
  public DataService(FoodDataRepository foodDataRepository,
      SupplementDataRepository supplementDataRepository) {
    this.foodDataRepository = foodDataRepository;
    this.supplementDataRepository = supplementDataRepository;
  }


  public List<FoodDataDTO> sendAllFoodData(){
    List<FoodData> foodData = foodDataRepository.findAll();
    return foodData.stream().map(FoodDataDTO::fromEntity).
        collect(Collectors.toList());
  }

  @Transactional
  public void saveSupplementAll(List<SupplementDataFromOpenApi> requests) {
    List<SupplementData> entities = requests.stream().map(SupplementDataFromOpenApi::toEntity).collect(Collectors.toList());
    supplementDataRepository.saveAll(entities);
  }

}
