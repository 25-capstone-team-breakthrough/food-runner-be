package com.Hansung.Capston.service;

import com.Hansung.Capston.entity.MealLog;
import com.Hansung.Capston.repository.MealLogRepository;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MealLogService {

  private final MealLogRepository mealLogRepository;

  @Autowired
  public MealLogService(MealLogRepository mealLogRepository) {
    this.mealLogRepository = mealLogRepository;
  }


}
