package com.Hansung.Capston.controller.UserInfo;

import com.Hansung.Capston.dto.Inbody.InbodyDTO;
import com.Hansung.Capston.entity.UserInfo.Inbody.Inbody;
import com.Hansung.Capston.entity.UserInfo.Inbody.InbodyImage;
import com.Hansung.Capston.repository.UserInfo.Inbody.InbodyImageRepository;
import com.Hansung.Capston.service.UserInfo.InbodyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/inbody")
public class InbodyController {

    @Autowired
    private InbodyService inbodyService;

    @Autowired
    private InbodyImageRepository imgRepository;

    //인바디 이미지 업로드
    //업로드시 자동으로 인바디 분석가능
    @PostMapping(value = "/imageUpload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<String> upload(@RequestParam("file") MultipartFile file) throws IOException {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null || auth.getPrincipal() == null) {
            return ResponseEntity.status(401).build();
        }
        String userId = (String) auth.getPrincipal();

        Inbody saved = inbodyService.uploadAndSave(file, userId);

        return ResponseEntity.ok("인바디 이미지가 저장되었습니다.");
    }

    //인바디 조회
    @GetMapping("/inbody-info")
    public ResponseEntity<List<Inbody>> getAll() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null || auth.getPrincipal() == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        String userId = (String) auth.getPrincipal();
        List<Inbody> all = inbodyService.getAllByUser(userId);
        return ResponseEntity.ok(all);
    }

    //인바디 이미지 조회 - 웹서비스 용
    @GetMapping("/image-info")
    public ResponseEntity<List<InbodyDTO>> listByUser() {

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null || auth.getPrincipal() == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        String userId = (String) auth.getPrincipal();
        List<InbodyImage> images = imgRepository.findAllByUserIdOrderByCreatedAtDesc(userId);

        List<InbodyDTO> dtos = images.stream()
                .map(img ->
                        new InbodyDTO(
                                img.getPictureId(),
                                img.getInbody().getInbodyId(),
                                img.getFilePath(),
                                img.getCreatedAt()
                        )
                )
                .collect(Collectors.toList());
        return ResponseEntity.ok(dtos);
    }

    //인바디 정보 삭제
    @DeleteMapping("/inbody-info/{inbodyId}")
    public ResponseEntity<String> removeInbody(@PathVariable Integer inbodyId) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null || auth.getPrincipal() == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        String userId = (String) auth.getPrincipal();
        inbodyService.deleteInbody(userId, inbodyId);
        return ResponseEntity.ok("인바디 정보가 삭제 되었습니다.");

    }
//    @PostMapping(
//            value = "/ocr-test",
//            consumes = MediaType.MULTIPART_FORM_DATA_VALUE,
//            produces = MediaType.TEXT_PLAIN_VALUE
//    )
//    public ResponseEntity<String> ocrTest(@RequestParam("file") MultipartFile file) throws IOException {
//        // 파일이 비어있다면 400
//        if (file.isEmpty()) {
//            return ResponseEntity.badRequest().body("파일이 없습니다.");
//        }
//
//        String text = visionService.extractText(file);
//        return ResponseEntity.ok(text);
//    }

}
