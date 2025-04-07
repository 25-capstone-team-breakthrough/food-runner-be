package com.Hansung.Capston.service;

import com.Hansung.Capston.dto.BMI.BMIRequset;
import com.Hansung.Capston.entity.BMI;
import com.Hansung.Capston.entity.User;
import com.Hansung.Capston.repository.BMIRepository;
import com.Hansung.Capston.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class BMIService {
    @Autowired
    private BMIRepository bmiRepository;

    @Autowired
    private UserRepository userRepository;

    //bmi 저장과 업데이트
    public BMI saveOrUpdateBMI(String userId, BMIRequset request) {

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("존재 하지 않은 유저입니다."));

        BMI bmi = bmiRepository.findById(userId).orElse(new BMI());
        bmi.setUser(user);
        bmi.setAge(request.getAge());
        bmi.setGender(request.getGender());
        bmi.setHeight(request.getHeight());
        bmi.setWeight(request.getWeight());

        return bmiRepository.save(bmi);
    }

    // userId를 기준으로 BMI 정보 조회
    public BMI getBMI(String userId) {
        return bmiRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("BMI info not found for user id: " + userId));
    }

    //bmi 계산
    public float calculateBMI(float weight, float height) {
        return weight / height;
    }
}
