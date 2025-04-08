package com.Hansung.Capston.service;

import com.Hansung.Capston.dto.FoodDataResponse;
import com.Hansung.Capston.dto.SupplementDataResponse;
import com.Hansung.Capston.entity.FoodData;
import com.Hansung.Capston.entity.SupplementData;
import com.Hansung.Capston.repository.FoodDataRepository;
import com.Hansung.Capston.repository.SupplementDataRepository;
import java.util.List;
import java.util.stream.Collectors;
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


  public List<FoodDataResponse> sendAllFoodData(){
    List<FoodData> foodData = foodDataRepository.findAll();
    return foodData.stream().map(FoodDataResponse::fromEntity).
        collect(Collectors.toList());
  }

  public List<SupplementDataResponse> sendAllSupplementData(){
    List<SupplementData> supplementData = supplementDataRepository.findAll();
    return supplementData.stream().map(SupplementDataResponse::fromEntity).
        collect(Collectors.toList());
  }
}
