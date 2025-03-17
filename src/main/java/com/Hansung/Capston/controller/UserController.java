package com.Hansung.Capston.controller;

import com.Hansung.Capston.dto.UserBMIInfo;
import com.Hansung.Capston.dto.UserInfoResponse;
import com.Hansung.Capston.dto.UserLoginRequest;
import com.Hansung.Capston.dto.UserSignUpRequest;
import com.Hansung.Capston.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController              // 해당 클래스가 RESTful API의 컨트롤러임을 명시 (응답을 JSON 등으로 자동 변환)
@RequestMapping("/api/users") // 공통 URL 경로를 지정함
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }


    //회원가입 api
    @PostMapping("/signup")
    public ResponseEntity<String> signup(@RequestBody UserSignUpRequest request){
        userService.registerUser(request);

        return new ResponseEntity<>("회원가입 성공", HttpStatus.CREATED);
    }

    //로그인 api
    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody UserLoginRequest request){
        userService.loginUser(request);

        return new ResponseEntity<>("로그인 성공", HttpStatus.CREATED);
    }
    //키, 몸무게, 성별, 나이 정보 등록 api
    @PutMapping("/{userId}/update")
    public ResponseEntity<String> userBMIInfo(@PathVariable String userId, @RequestBody UserBMIInfo userBMIInfo){
        userService.userBMIInfo(userId, userBMIInfo);

        return new ResponseEntity<>("BMI 업데이트 성공", HttpStatus.OK);
    }

    //사용자 정보 조회 api
    @GetMapping("/{userId}")
    public ResponseEntity<UserInfoResponse> userInfo(@PathVariable String userId){
        UserInfoResponse response = userService.getUserInfo(userId);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
