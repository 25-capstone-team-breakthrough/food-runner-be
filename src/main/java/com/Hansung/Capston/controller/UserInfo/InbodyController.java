package com.Hansung.Capston.controller.UserInfo;

import com.Hansung.Capston.service.UserInfo.InbodyService;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/inbody")
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

    @PostMapping("/webhook")
    public ResponseEntity<String> receiveWebhook(@RequestBody Map<String, Object> payload) {
        return ResponseEntity.ok("Success");
    }


}
