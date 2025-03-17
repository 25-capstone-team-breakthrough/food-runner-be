package com.Hansung.Capston.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserBMIInfo {
    private Integer age;
    private String gender;
    private Float height;
    private Float weight;
}
