package com.Hansung.Capston.dto.BMI;

import lombok.Data;

@Data
public class BMIRequset {
    private int age;
    private String gender;
    private float height;
    private float weight;
}
