package com.Hansung.Capston.service.UserInfo;

import com.Hansung.Capston.dto.BMI.BMIRequset;
import com.Hansung.Capston.entity.UserInfo.BMI;
import com.Hansung.Capston.entity.UserInfo.User;
import com.Hansung.Capston.repository.UserInfo.BMIRepository;
import com.Hansung.Capston.repository.UserInfo.UserRepository;
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

        bmiRepository.save(bmi);
        user.setExistBmi(true);
        userRepository.save(user);

        return bmi;
    }

    // userId를 기준으로 BMI 정보 조회
    public BMI getBMI(String userId) {
        return bmiRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("BMI info not found for user id: " + userId));
    }

    //bmi 계산
    public float calculateBMR(float weight, float height) {
        return weight / height;
    }

    public double calculateBMR(String user) {
        BMI bmiInfo = getBMI(user);

        double bmr = 0;

        if(bmiInfo.getGender().equals("male")){
            bmr = 88.362 + (13.397 * bmiInfo.getWeight()) + (4.799 * bmiInfo.getHeight()) - (5.677 * bmiInfo.getAge());
        } else if(bmiInfo.getGender().equals("female")){
            bmr = 447.593 + (9.247 * bmiInfo.getWeight()) + (3.098 * bmiInfo.getHeight()) - (4.330 * bmiInfo.getAge());
        }

        bmr = Math.round(bmr*10) / 10.0;

        return bmr;
    }
}
