package com.Hansung.Capston.dto.SignUp;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import com.Hansung.Capston.entity.User;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SignUpResponse {
    private String userId;
    private String account;
    private String name;
    private String role;

    public static SignUpResponse from(User user) {
        return new SignUpResponse(
                user.getUserId(),
                user.getAccount(),
                user.getName(),
                user.getRole().toString()
        );
    }


}

