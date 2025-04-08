package com.Hansung.Capston.dto.SignIn;

import com.Hansung.Capston.entity.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SignInResponse {
    private String name;
    private String role;
    private String token;

    public static SignInResponse from(User user, String token){
        return new SignInResponse(
                user.getName(),
                user.getRole().toString(),
                token
        );
    }
}
