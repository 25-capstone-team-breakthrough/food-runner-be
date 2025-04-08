package com.Hansung.Capston.dto.BMI;

import com.Hansung.Capston.entity.BMI;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
public class BMIRequset {
    private int age;
    private String gender;
    private float height;
    private float weight;
}
