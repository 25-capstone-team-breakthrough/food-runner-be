package com.Hansung.Capston.service;

import com.Hansung.Capston.Exception.InvalidCredentialsException;
import com.Hansung.Capston.Exception.UserAlreadyExistsException;
import com.Hansung.Capston.dto.UserBMIInfo;
import com.Hansung.Capston.dto.UserInfoResponse;
import com.Hansung.Capston.dto.UserLoginRequest;
import com.Hansung.Capston.dto.UserSignUpRequest;
import com.Hansung.Capston.entity.User;
import com.Hansung.Capston.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;

    //회원가입
    public void registerUser(UserSignUpRequest request){

        // 아이디 중복 체크
        if (userRepository.existsById(request.getId())) {
            throw new UserAlreadyExistsException("User with ID " + request.getId() + " already exists.");
        }

        User user = new User();
        user.setId(request.getId());
        user.setName(request.getName());
        user.setPassword(request.getPassword()); //Bcrypt 비밀 번호 암호화 만들기

        userRepository.save(user);
    }

    //로그인
    public void loginUser(UserLoginRequest request){
        //ID조회
        User user = userRepository.findById(request.getId())
                .orElseThrow(() -> new InvalidCredentialsException("User Not Found"));
        //비밀번호 비교
        if(!user.getPassword().equals(request.getPassword())){
            throw new InvalidCredentialsException("User Not Found");
        }
    }

    //키, 몸무게, 성별, 나이 정보 등록
    public void userBMIInfo(String userId, UserBMIInfo userBMIInfo){
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found with ID: " + userId));

        user.setAge(userBMIInfo.getAge());
        user.setGender(userBMIInfo.getGender());
        user.setHeight(userBMIInfo.getHeight());
        user.setWeight(userBMIInfo.getWeight());

         userRepository.save(user);
    }

    //사용자 정보 조회
    public UserInfoResponse getUserInfo(String userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found with ID: " + userId));
        return new UserInfoResponse(
                user.getId(),
                user.getName(),
                user.getAge(),
                user.getGender(),
                user.getHeight(),
                user.getWeight()
        );
    }
}
