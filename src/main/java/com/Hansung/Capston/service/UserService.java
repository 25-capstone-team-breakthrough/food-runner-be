package com.Hansung.Capston.service;

import com.Hansung.Capston.Exception.InvalidCredentialsException;
import com.Hansung.Capston.common.MemberType;
import com.Hansung.Capston.dto.SignIn.SignInRequest;
import com.Hansung.Capston.dto.SignIn.SignInResponse;
import com.Hansung.Capston.dto.SignUp.SignUpRequest;
import com.Hansung.Capston.dto.SignUp.SignUpResponse;
import com.Hansung.Capston.entity.User;
import com.Hansung.Capston.repository.UserRepository;
import com.Hansung.Capston.jwt.TokenProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PasswordEncoder encoder;
    @Autowired
    private TokenProvider tokenProvider;

    //회원가입
    public SignUpResponse signUp (SignUpRequest request){
        // 계정 중복 확인
        userRepository.findByAccount(request.getAccount()).ifPresent(existingUser -> {
            throw new RuntimeException("이미 존재하는 계정입니다.");
        });

        User user = new User();
        user.setAccount(request.getAccount());
        user.setName(request.getName());
        user.setPassword(encoder.encode(request.getPassword()));
        user.setRole(MemberType.ROLE_USER);

        user = userRepository.save(user);
        return SignUpResponse.from(user);
    }

    //로그인
    public SignInResponse login(SignInRequest request){
        //ID 비교
        User user = userRepository.findByAccount(request.getAccount())
                .orElseThrow(() -> new InvalidCredentialsException("존재하지 않는 계정입니다."));
        // 비밀번호 비교
        if (!encoder.matches(request.getPassword(), user.getPassword())) {
            throw new InvalidCredentialsException("비밀번호가 틀렸습니다.");
        }
        String token = tokenProvider.createToken(String.format("%s:%s", user.getUserId(), user.getRole()));	// 토큰 생성
        return SignInResponse.from(user,token);
    }

}
