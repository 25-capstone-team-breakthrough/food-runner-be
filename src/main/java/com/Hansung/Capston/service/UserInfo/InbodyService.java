package com.Hansung.Capston.service.UserInfo;

import com.Hansung.Capston.entity.UserInfo.Inbody.Inbody;
import com.Hansung.Capston.entity.UserInfo.Inbody.InbodyImage;
import com.Hansung.Capston.repository.UserInfo.Inbody.InbodyImageRepository;
import com.Hansung.Capston.repository.UserInfo.Inbody.InbodyRepository;
import com.Hansung.Capston.service.ApiService.AwsS3Service;
import com.Hansung.Capston.service.ApiService.OpenAiApiService;
import com.Hansung.Capston.service.ApiService.VisionService;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;
import java.util.Arrays;
import java.util.stream.Collectors;

@Service
public class InbodyService {
  @Autowired
  private AwsS3Service awsS3Service;
  @Autowired
  private VisionService visionService;
  @Autowired
  private InbodyRepository inbodyRepository;
  @Autowired
  private InbodyImageRepository inbodyImgRepostiory;
  @Autowired
  private OpenAiApiService openAiApiService;

  private Float toFloat(Object o) {
    if (o == null) return null;
    if (o instanceof Number) {
      return ((Number) o).floatValue();
    }
    try {
      return Float.parseFloat(o.toString());
    } catch (Exception e) {
      return null;
    }
  }

  //인바디 이미지 업로드 시
  @Transactional
  public Inbody uploadAndSave(MultipartFile file, String userId) throws IOException {
    // 1) S3 업로드
    String imgUrl  = awsS3Service.uploadInbodyImage(file);
    // 2) OCR 추출
    String ocrText = visionService.extractText(file);
    // 3) OpenAI 파싱
    Map<String, Float> metrics = openAiApiService.extractInbodyMetrics(ocrText);

    // 4) 분류어 전체 추출 → 10개
    List<String> allSeg = extractAllSegmentalValues(ocrText);

    // 5) Lean은 0,1,4,6,7번 / Fat은 2,3,5,8,9번
    String lean = joinByIndex(allSeg, 0,1,4,6,7);
    String fat  = joinByIndex(allSeg, 2,3,5,8,9);

    // 6) 엔티티 빌드
    Inbody inbody = Inbody.builder()
            .userId(userId)
            .bodyWater(        metrics.get("bodyWater"))
            .protein(          metrics.get("protein"))
            .minerals(         metrics.get("minerals"))
            .bodyFatAmount(    metrics.get("bodyFatAmount"))
            .weight(           metrics.get("weight"))
            .skeletalMuscleMass(metrics.get("skeletalMuscleMass"))
            .bmi(              metrics.get("bmi"))
            .bodyFatPercentage(metrics.get("bodyFatPercentage"))
            .segmentalLeanAnalysis(lean)
            .segmentalFatAnalysis( fat)
            .createdAt(LocalDateTime.now())
            .build();

    inbody = inbodyRepository.save(inbody);

    inbodyImgRepostiory.save(
            InbodyImage.builder()
                    .inbody(inbody)
                    .userId(userId)
                    .filePath(imgUrl)
                    .createdAt(LocalDateTime.now())
                    .build()
    );

    return inbody;
  }

  // ────────────────────────────────────────────────────
  // 분류어만 골라내서 리스트로 반환 (순서 보존)
  private static final Pattern SEG_PATTERN =
          Pattern.compile("^표준(?:이상|이하)?$");
  private List<String> extractAllSegmentalValues(String text) {
    List<String> out = new ArrayList<>(10);
    boolean started = false;

    for (String line : text.split("\\r?\\n")) {
      if (!started) {
        if (line.contains("부위별근육분석")) {
          started = true;
        }
        continue;
      }
      String t = line.trim();
      if (SEG_PATTERN.matcher(t).matches()) {
        out.add(t);
        if (out.size() == 10) {
          break;
        }
      }
    }

    return out;
  }

  // 주어진 인덱스 배열로 리스트에서 꺼내 comma-join
  private String joinByIndex(List<String> list, int... idxs) {
    return Arrays.stream(idxs)
            .mapToObj(i -> i < list.size() ? list.get(i) : "")
            .collect(Collectors.joining(","));
  }

  //인바디 조회
  @Transactional(readOnly = true)
  public List<Inbody> getAllByUser(String userId) {
    return inbodyRepository.findAllByUserIdOrderByCreatedAtDesc(userId);
  }
}

