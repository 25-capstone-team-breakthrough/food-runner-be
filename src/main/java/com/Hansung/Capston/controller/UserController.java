package com.Hansung.Capston.controller;

import com.Hansung.Capston.dto.UserSignUpRequest;
import com.Hansung.Capston.dto.UserSignUpResponse;
import com.Hansung.Capston.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController              // 해당 클래스가 RESTful API의 컨트롤러임을 명시 (응답을 JSON 등으로 자동 변환)
@RequestMapping("/api/users") // 공통 URL 경로를 지정함
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }


    //회원가입 api: 요청 DTO를 받아 처리 후 응답 DTO 반환
    @PostMapping("/signup")
    public ResponseEntity<UserSignUpResponse> signup(@RequestBody UserSignUpRequest request){
        UserSignUpResponse response = userService.registerUser(request);

        return new ResponseEntity<>(response, HttpStatus.CREATED); // 응답 본문에 response객체를 담아 HTTP 상태코드 201반환: 성공적 생성
    }

}
