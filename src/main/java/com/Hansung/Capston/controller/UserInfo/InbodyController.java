package com.Hansung.Capston.controller.UserInfo;

import com.Hansung.Capston.service.UserInfo.InbodyService;
import java.security.Key;
import java.util.HashMap;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/inbody/webhook")
public class InbodyController {

    @Autowired
    private InbodyService inbodyService;
//
//    @GetMapping("/test")
//    public String test(@RequestParam ("imageUrl") String imageUrl) {
//        try{
//            return  inbodyService.extractTextFromImageUrl(imageUrl);
//        } catch (Exception e) {
//            e.printStackTrace();
//            return "Failed:" + e;
//        }
//    }


    @PostMapping("/user/GetTodayNewUser")
    public ResponseEntity<Map<String,Object>>getTodayNewUser() {
        Map<String, Object> requestBody = Map.of("Date", "");

        return ResponseEntity.ok(inbodyService.postToInbody("http://apikr.lookinbody.com/user/GetTodayNewUser", requestBody));
    }
}
