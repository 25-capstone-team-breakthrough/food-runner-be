package com.Hansung.Capston.service.UserInfo;

import com.Hansung.Capston.dto.Exersice.YoutubeExerciseDTO;
import com.Hansung.Capston.entity.Exercise.RecommandExerciseVideo;
import com.Hansung.Capston.entity.UserInfo.BMI;
import com.Hansung.Capston.entity.UserInfo.Inbody.Inbody;
import com.Hansung.Capston.entity.UserInfo.Inbody.InbodyImage;
import com.Hansung.Capston.entity.UserInfo.User;
import com.Hansung.Capston.repository.UserInfo.BMIRepository;
import com.Hansung.Capston.repository.UserInfo.Inbody.InbodyImageRepository;
import com.Hansung.Capston.repository.UserInfo.Inbody.InbodyRepository;
import com.Hansung.Capston.repository.UserInfo.UserRepository;
import com.Hansung.Capston.service.ApiService.AwsS3Service;
import com.Hansung.Capston.service.ApiService.OpenAiApiService;
import com.Hansung.Capston.service.ApiService.VisionService;
import com.Hansung.Capston.service.Exercise.RecommandExerciseVideoService;
import com.Hansung.Capston.service.Exercise.VideoService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.*;
import java.util.regex.Pattern;
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
  @Autowired
  private VideoService videoService;
  @Autowired
  private RecommandExerciseVideoService recommandExerciseVideoService;
  @Autowired
  private UserRepository userRepository;
  @Autowired
  private BMIRepository bmiRepository;



  private static final Pattern SEG_PATTERN =
          Pattern.compile("^표준(?:이상|이하)?$");

  private static final List<String> SEG_LABELS = List.of(
          "왼쪽 상체","오른쪽 상체","복부","왼쪽 하체","오른쪽 하체"
  );

  private static final List<String> EX_VIDEO_CATEGORIES = List.of(
          "어깨","가슴","배","팔","허벅지","엉덩이","종아리","등"
  );
    @Autowired
    private InbodyImageRepository inbodyImageRepository;

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
    User user = userRepository.findById(userId)
            .orElseThrow(() -> new RuntimeException("존재 하지 않은 유저입니다."));

    Inbody inbody = Inbody.builder()
            .user(user)
            .bodyWater(        metrics.get("bodyWater"))
            .protein(          metrics.get("protein"))
            .minerals(         metrics.get("minerals"))
            .bodyFatAmount(    metrics.get("bodyFatAmount"))
            .weight(           metrics.get("weight"))
            .skeletalMuscleMass(metrics.get("skeletalMuscleMass"))
            .bmi(metrics.get("bmi"))
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


    // 인바디 업로드시 bmi 몸무게 변경
    BMI bmi = bmiRepository.findById(userId).orElse(new BMI());
    bmi.setWeight(metrics.get("weight"));
    bmiRepository.save(bmi);

    // 8) AI 및 YouTube 운동 추천
    List<String> leanArr = Arrays.asList(lean.split(","));
    List<String> fatArr  = Arrays.asList(fat.split(","));

    boolean upper = false, core = false, lower = false;
    for (int i = 0; i < SEG_LABELS.size(); i++) {
      if ("표준이하".equals(leanArr.get(i)) || "표준이상".equals(fatArr.get(i))) {
        String seg = SEG_LABELS.get(i);
        if (seg.contains("상체")) upper = true;
        if (seg.contains("복부")) core  = true;
        if (seg.contains("하체")) lower = true;
      }
    }
    if (!upper && !core && !lower) {
      return inbody;
    }

    List<String> targets = new ArrayList<>();
    if (upper) targets.addAll(List.of("어깨","가슴","팔","등"));
    if (core)  targets.add("배");
    if (lower) targets.addAll(List.of("허벅지","엉덩이","종아리"));
    targets = targets.stream().distinct().collect(Collectors.toList());
    targets = targets.stream().distinct().collect(Collectors.toList());


    recommandExerciseVideoService.clearRecommendationsByUserId(userId);

    for (String cat : targets) {
      Set<String> savedIds = new HashSet<>();
      int savedCount = 0;

      List<String> exs = openAiApiService.recommendExercisesBySegments(
              List.of(cat), 3
      ).stream().distinct().limit(3).toList();

      for (String ex : exs) {
        if (savedCount == 3) break;
        List<YoutubeExerciseDTO> vids = videoService.recommandSearchVideos(ex, 3);
        if (vids.isEmpty()) continue;
        String vidId = vids.get(0).getVideoId();
        if (savedIds.contains(vidId)) continue;

        RecommandExerciseVideo rec = new RecommandExerciseVideo();
        rec.setUser(inbody.getUser());
        rec.setCategory(cat);
        rec.setVideoId(vidId);
        rec.setTitle(vids.get(0).getTitle());
        rec.setUrl(vids.get(0).getUrl());
        rec.setIsAIRecommendation(true);
        recommandExerciseVideoService.saveRecommendation(rec);

        savedIds.add(vidId);
        savedCount++;
      }
      if (savedCount < 3) {
        List<YoutubeExerciseDTO> fallback = videoService.recommandSearchVideos(cat + " 운동", 3);
        for (YoutubeExerciseDTO dto : fallback) {
          if (savedCount == 3) break;
          if (savedIds.contains(dto.getVideoId())) continue;

          RecommandExerciseVideo rec = new RecommandExerciseVideo();
          rec.setUser(inbody.getUser());
          rec.setCategory(cat);
          rec.setVideoId(dto.getVideoId());
          rec.setTitle(dto.getTitle());
          rec.setUrl(dto.getUrl());
          rec.setIsAIRecommendation(true);
          recommandExerciseVideoService.saveRecommendation(rec);

          savedIds.add(dto.getVideoId());
          savedCount++;
        }
      }
    }
    return inbody;
  }

  // 인바디이미지 분석관련
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
  private String joinByIndex(List<String> list, int... idxs) {
    return Arrays.stream(idxs)
            .mapToObj(i -> i < list.size() ? list.get(i) : "")
            .collect(Collectors.joining(","));
  }

  //인바디 조회
  @Transactional(readOnly = true)
  public List<Inbody> getAllByUser(String userId) {
    List<Inbody> inbodyList = inbodyRepository.findAllByUserUserIdOrderByCreatedAtDesc(userId);
    List<Inbody> res = new ArrayList<>();
    for (Inbody inbody : inbodyList) {
      inbody.setCreatedAt(inbody.getCreatedAt().plusHours(9));
      res.add(inbody);
    }
    return res;
  }


  //인바디 삭제
  @Transactional
  public void deleteInbody(String userId, Integer inbodyId) {
      Inbody inbody = inbodyRepository.findByInbodyIdAndUser_UserId(inbodyId, userId)
              .orElseThrow(() -> new EntityNotFoundException("삭제할 기록을 찾을 수 없거나, 권한이 없습니다."));
      InbodyImage image = inbodyImageRepository.findByInbody_InbodyIdAndInbody_User_UserId(inbodyId, userId)
              .orElseThrow(() -> new EntityNotFoundException("삭제할 기록을 찾을 수 없거나, 권한이 없습니다."));

      inbodyImageRepository.delete(image);
      inbodyRepository.delete(inbody);

  }
}

