package com.Hansung.Capston.controller.UserInfo;

import com.Hansung.Capston.service.UserInfo.InbodyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class InbodyController {

    @Autowired
    private InbodyService inbodyService;

    @GetMapping("/test")
    public String test(@RequestParam ("imageUrl") String imageUrl) {
        try{
            return  inbodyService.extractTextFromImageUrl(imageUrl);
        } catch (Exception e) {
            return "Failed:" + e.getMessage();
        }
    }
}
