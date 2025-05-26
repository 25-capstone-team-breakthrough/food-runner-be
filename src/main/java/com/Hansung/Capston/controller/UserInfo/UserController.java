package com.Hansung.Capston.controller.UserInfo;

import com.Hansung.Capston.dto.SignIn.SignInRequest;
import com.Hansung.Capston.dto.SignIn.SignInResponse;
import com.Hansung.Capston.dto.SignUp.SignUpRequest;
import com.Hansung.Capston.dto.SignUp.SignUpResponse;
import com.Hansung.Capston.entity.UserInfo.User;
import com.Hansung.Capston.service.UserInfo.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
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
    public ResponseEntity<String> signup(@RequestBody SignUpRequest request){
        SignUpResponse response = userService.signUp(request);
        return ResponseEntity.ok("회원가입 성공했습니다.");
    }

    //로그인
    @PostMapping("/login")
    public ResponseEntity<SignInResponse> login(@RequestBody SignInRequest request){
        SignInResponse response = userService.login(request);

        return ResponseEntity.ok(response);
    }

    //이름정보 조회(나중에 아이디나 비번 조회필요하면 말하시오)
    @GetMapping("/signup")
    public ResponseEntity<SignUpResponse> signUpInfo() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null || auth.getPrincipal() == null) {
            return ResponseEntity.status(401).build();
        }
        String userId = (String) auth.getPrincipal();
        User user = userService.signUpInfo(userId);
        return ResponseEntity.ok(SignUpResponse.from(user));
    }
}
