package com.Hansung.Capston.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserSignUpRequest {
    private String userId;
    private String name;
    private String password;

}
