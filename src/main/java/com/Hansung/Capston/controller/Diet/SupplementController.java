package com.Hansung.Capston.controller.Diet;

import com.Hansung.Capston.dto.Diet.Supplement.SupplementLogRequest;
import com.Hansung.Capston.entity.Diet.Supplement.PreferredSupplement;
import com.Hansung.Capston.entity.Diet.Supplement.SupplementData;
import com.Hansung.Capston.entity.Diet.Supplement.SupplementLog;
import com.Hansung.Capston.service.Diet.SupplementService;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/diet/sup")
public class SupplementController {
  private final SupplementService supplementService;

  @Autowired
  public SupplementController(SupplementService supplementService) {
    this.supplementService = supplementService;
  }
  
  // 영양제 데이터 불러오기
  @GetMapping("/data/load")
  public ResponseEntity<List<SupplementData>> loadSupplementData() {
    Authentication auth = SecurityContextHolder.getContext().getAuthentication();
    if (auth == null || auth.getPrincipal() == null) {
      return ResponseEntity.status(401).build();
    }

    return ResponseEntity.ok(supplementService.loadSupplementData());
  }

  // 영양제 섭취 기록 등록하기
  @PostMapping("/log/save")
  public ResponseEntity<String> saveSupplementLog(@RequestBody SupplementLogRequest request) {
    Authentication auth = SecurityContextHolder.getContext().getAuthentication();
    if (auth == null || auth.getPrincipal() == null) {
      return ResponseEntity.status(401).build();
    }
    String userId = (String) auth.getPrincipal();

    supplementService.saveSupplementLog(userId, request.getDateTime(), request.getId());

    return ResponseEntity.ok("save success");
  }

  // 영양제 섭취 기록 불러오기
  @GetMapping("/log/load")
  public ResponseEntity<List<SupplementLog>> loadSupplementLogs() {
    Authentication auth = SecurityContextHolder.getContext().getAuthentication();
    if (auth == null || auth.getPrincipal() == null) {
      return ResponseEntity.status(401).build();
    }
    String userId = (String) auth.getPrincipal();

    return ResponseEntity.ok(supplementService.loadSupplementLogs(userId));
  }

  // 영양제 섭취 기록 삭제하기
  @PostMapping("/log/delete")
  public ResponseEntity<String> deleteSupplementLog(@RequestParam(name = "log_id") Long supplementLogId) {
    Authentication auth = SecurityContextHolder.getContext().getAuthentication();
    if (auth == null || auth.getPrincipal() == null) {
      return ResponseEntity.status(401).build();
    }
    String userId = (String) auth.getPrincipal();

    supplementService.deleteSupplementLog(userId, supplementLogId);

    return ResponseEntity.ok("delete success");
  }
  // 영양제 즐겨찾기 등록하기
  @PostMapping("/pref/save")
  public ResponseEntity<String> savePreferredSupplement(@RequestParam(name = "sup_id") Long supplementId) {
    Authentication auth = SecurityContextHolder.getContext().getAuthentication();
    if (auth == null || auth.getPrincipal() == null) {
      return ResponseEntity.status(401).build();
    }
    String userId = (String) auth.getPrincipal();

    return ResponseEntity.ok(supplementService.savePreferredSupplement(userId, supplementId));
  }

  // 영양제 즐겨찾기 불러오기
  @GetMapping("/pref/load")
  public ResponseEntity<List<PreferredSupplement>> loadPreferredSupplements() {
    Authentication auth = SecurityContextHolder.getContext().getAuthentication();
    if (auth == null || auth.getPrincipal() == null) {
      return ResponseEntity.status(401).build();
    }
    String userId = (String) auth.getPrincipal();

    return ResponseEntity.ok(supplementService.getPreferredSupplements(userId));
  }
  
  // 영양제 즐겨찾기 삭제하기
  @PostMapping("/pref/delete")
  public ResponseEntity<String> deletePreferredSupplement(@RequestParam(name = "pref_id") Long preferredSupplementId) {
    Authentication auth = SecurityContextHolder.getContext().getAuthentication();
    if (auth == null || auth.getPrincipal() == null) {
      return ResponseEntity.status(401).build();
    }

    supplementService.deletePreferredSupplement(preferredSupplementId);

    return ResponseEntity.ok("delete success");
  }
}
