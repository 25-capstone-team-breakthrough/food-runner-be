package com.Hansung.Capston.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor

public class UserSignUpResponse {
    private String userId;
    private String name;
}
