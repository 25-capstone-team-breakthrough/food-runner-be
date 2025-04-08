package com.Hansung.Capston.controller;

import com.Hansung.Capston.dto.SignIn.SignInRequest;
import com.Hansung.Capston.dto.SignIn.SignInResponse;
import com.Hansung.Capston.dto.SignUp.SignUpRequest;
import com.Hansung.Capston.dto.SignUp.SignUpResponse;
import com.Hansung.Capston.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController              // 해당 클래스가 RESTful API의 컨트롤러임을 명시 (응답을 JSON 등으로 자동 변환)
@RequestMapping("/users") // 공통 URL 경로를 지정함
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    //회원가입
    @PostMapping("/signup")
    public ResponseEntity<SignUpResponse> signup(@RequestBody SignUpRequest request){
        SignUpResponse response = userService.signUp(request);
        return ResponseEntity.ok(response);
    }

    //로그인
    @PostMapping("/login")
    public ResponseEntity<SignInResponse> login(@RequestBody SignInRequest request){
        SignInResponse response = userService.login(request);

        return ResponseEntity.ok(response);
    }

}
