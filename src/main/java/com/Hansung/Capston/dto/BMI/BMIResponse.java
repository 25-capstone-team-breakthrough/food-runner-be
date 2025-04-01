package com.Hansung.Capston.dto.BMI;

import com.Hansung.Capston.entity.BMI;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BMIResponse {
    private float height;
    private float weight;
    private String Gender;
    private int age;

    public static BMIResponse from(BMI bmi) {
        BMIResponse response = new BMIResponse();

        response.setAge(bmi.getAge());
        response.setGender(bmi.getGender());
        response.setHeight(bmi.getHeight());
        response.setWeight(bmi.getWeight());

        return response;
    }
}
