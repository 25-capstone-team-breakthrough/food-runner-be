package com.Hansung.Capston.controller.STT;

import com.Hansung.Capston.dto.Exersice.ExerciseLog.ExerciseLogDto;
import com.Hansung.Capston.dto.Exersice.ExerciseLog.StrengthSetLogDto;
import com.Hansung.Capston.entity.Exercise.ExerciseData;
import com.Hansung.Capston.entity.Exercise.ExerciseLog;
import com.Hansung.Capston.repository.Exercise.ExerciseDataRepository;
import com.Hansung.Capston.service.Exercise.CalorieAnalysisService;
import com.Hansung.Capston.service.Exercise.ExerciseService;
import com.Hansung.Capston.service.STT.SttService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@RestController
@RequestMapping("/stt")
@Slf4j
public class SttController {
    @Autowired
    private SttService sttService;

    @Autowired
    private ExerciseDataRepository exerciseDataRepository;

    @Autowired
    private ExerciseService exerciseService;

    @Autowired
    private CalorieAnalysisService calorieAnalysisService;

    @PostMapping(value = "/audio", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<String> stt(@RequestParam("audioFile") MultipartFile audioFile) throws IOException {

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null || auth.getPrincipal() == null) {
            return ResponseEntity.status(401).build();
        }
        String userId = (String) auth.getPrincipal();

        //음성녹음 관련
        String transcribe = sttService.transcribe(audioFile);

        //음성 파싱
        ExerciseLogDto dto = DtoTranscribe(transcribe);

        //운동 기록 저장
        ExerciseLog saved = exerciseService.saveExerciseLog(userId, dto);

        try {
            calorieAnalysisService.analyzeAndSave(saved);
        } catch (Exception e) {
            log.error("칼로리 분석 중 오류", e);
        }

        return ResponseEntity.ok().body(transcribe);
    }


    private ExerciseLogDto DtoTranscribe(String transcribe) {

        // 유산소 ex) 30분 5km
        Pattern cardioPattern = Pattern.compile("(.+?)\\s*(\\d+)분\\s*(\\d+(?:\\.\\d+)?)km");
        Matcher m1 = cardioPattern.matcher(transcribe);
        if (m1.find()) {
            String name = m1.group(1).trim();
            int mins = Integer.parseInt(m1.group(2));
            double distance = Double.parseDouble(m1.group(3));

            ExerciseData data = exerciseDataRepository
                    .findByExerciseName(name)
                    .orElseThrow(() -> new IllegalArgumentException("등록되지 않은 운동: " + name));
            if (!"CARDIO".equalsIgnoreCase(data.getExerciseType())) {
                throw new IllegalArgumentException(name + " 은 CARDIO 타입이 아닙니다.");
            }

            ExerciseLogDto dto = new ExerciseLogDto();
            dto.setExerciseId(data.getExerciseId());
            dto.setTime(mins);
            dto.setDistance(distance);
            return dto;
        }
        //  근력 ex) 3세트 10kg 10회
        Pattern strengthPattern = Pattern.compile("(.+?)\\s*(\\d+)세트\\s*(\\d+(?:\\.\\d+)?)kg\\s*(\\d+)회");
        Matcher m2 = strengthPattern.matcher(transcribe);
        if (m2.find()) {
            String name = m2.group(1).trim();
            int setsCnt = Integer.parseInt(m2.group(2));
            double weight = Double.parseDouble(m2.group(3));
            int reps = Integer.parseInt(m2.group(4));

            ExerciseData data = exerciseDataRepository
                    .findByExerciseName(name)
                    .orElseThrow(() -> new IllegalArgumentException("등록되지 않은 운동: " + name));
            if (!"STRENGTH".equalsIgnoreCase(data.getExerciseType())) {
                throw new IllegalArgumentException(name + " 은 STRENGTH 타입이 아닙니다.");
            }

            ExerciseLogDto dto = new ExerciseLogDto();
            dto.setExerciseId(data.getExerciseId());

            List<StrengthSetLogDto> sets = new ArrayList<>();
            for (int i = 1; i <= setsCnt; i++) {
                StrengthSetLogDto s = new StrengthSetLogDto();
                s.setSets(i);
                s.setWeight(weight);
                s.setReps(reps);
                sets.add(s);
            }
            dto.setStrengthSets(sets);
            return dto;
        }

        // 둘 다 아니면 예외
        throw new IllegalArgumentException("지원하지 않는 음성 패턴입니다: " + transcribe);
    }
}
