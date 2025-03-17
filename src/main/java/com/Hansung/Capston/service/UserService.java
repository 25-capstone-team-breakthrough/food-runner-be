package com.Hansung.Capston.service;

import com.Hansung.Capston.dto.UserSignUpRequest;
import com.Hansung.Capston.dto.UserSignUpResponse;
import com.Hansung.Capston.entity.User;
import com.Hansung.Capston.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;

    //회원가입
    public UserSignUpResponse registerUser(UserSignUpRequest request){
        User user = new User();
        user.setUserId(request.getUserId());
        user.setName(request.getName());
        user.setPassword(request.getPassword()); //Bcrypt 비밀 번호 암호화 만들기

        User savedUser = userRepository.save(user);
        return new UserSignUpResponse(savedUser.getUserId(), savedUser.getName());
    }
}
