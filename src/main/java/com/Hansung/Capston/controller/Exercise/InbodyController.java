package com.Hansung.Capston.controller.Exercise;

import java.util.Map;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class InbodyController {
  @PostMapping("/webhook")
  public ResponseEntity<String> receiveWebhook(@RequestBody Map<String, Object> payload) {
    return ResponseEntity.ok("Success");
  }

}
