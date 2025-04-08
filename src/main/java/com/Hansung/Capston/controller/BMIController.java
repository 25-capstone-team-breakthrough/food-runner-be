package com.Hansung.Capston.controller;

import com.Hansung.Capston.dto.BMI.BMIRequset;
import com.Hansung.Capston.dto.BMI.BMIResponse;
import com.Hansung.Capston.entity.BMI;
import com.Hansung.Capston.service.BMIService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/BMI")
public class BMIController {

    private final BMIService bmiService;

    public BMIController(BMIService bmiService) {
        this.bmiService = bmiService;
    }
    // BMI 정보 저장 또는 업데이트 (JWT 토큰을 통해 본인의 userId 사용)
    @PostMapping("/update")
    public ResponseEntity<String> saveBMI(@RequestBody BMIRequset request) {
        // SecurityContext에서 JWT 토큰으로 인증된 사용자 ID 추출
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null || auth.getPrincipal() == null) {
            return ResponseEntity.status(401).build();
        }
        String userId = (String) auth.getPrincipal();
        // BMIService를 호출하여 해당 userId의 BMI 데이터를 저장 또는 업데이트
        bmiService.saveOrUpdateBMI(userId, request);

        return new ResponseEntity<>("bmi 업데이트 성공",HttpStatus.CREATED);
    }

    //bmi 조회
    @GetMapping("/info")
    public ResponseEntity<BMIResponse> BMIInfo(){
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null || auth.getPrincipal() == null) {
            return ResponseEntity.status(401).build();
        }
        String userId = (String) auth.getPrincipal();

        BMI bmi = bmiService.getBMI(userId);
        return ResponseEntity.ok(BMIResponse.from(bmi));
    }

}
