package com.Hansung.Capston.dto.BMI;

import com.Hansung.Capston.entity.BMI;
import lombok.Data;

@Data
public class BMIRequset {
    private int age;
    private String gender;
    private float height;
    private float weight;
}
