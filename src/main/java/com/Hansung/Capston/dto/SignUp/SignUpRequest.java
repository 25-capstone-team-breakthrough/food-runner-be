package com.Hansung.Capston.dto.SignUp;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SignUpRequest {
    private String account;
    private String password;
    private String name;
}
