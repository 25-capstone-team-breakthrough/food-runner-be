package com.Hansung.Capston.controller.STT;

import com.Hansung.Capston.dto.Exersice.ExerciseLog.ExerciseLogDto;
import com.Hansung.Capston.dto.Exersice.ExerciseLog.StrengthSetLogDto;
import com.Hansung.Capston.entity.Exercise.ExerciseData;
import com.Hansung.Capston.entity.Exercise.ExerciseLog;
import com.Hansung.Capston.repository.Exercise.ExerciseDataRepository;
import com.Hansung.Capston.service.ApiService.OpenAiApiService;
import com.Hansung.Capston.service.Exercise.CalorieAnalysisService;
import com.Hansung.Capston.service.Exercise.ExerciseService;
import com.Hansung.Capston.service.STT.SttService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

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

    @Autowired
    private OpenAiApiService openAiApiService;

    //음성녹음 -> 텍스트변환
    @PostMapping(value = "/audio", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<String> stt(@RequestParam("audioFile") MultipartFile audioFile) throws IOException {

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null || auth.getPrincipal() == null) {
            return ResponseEntity.status(401).build();
        }
        //String userId = (String) auth.getPrincipal();

        //음성녹음 관련
        String transcribe = sttService.transcribe(audioFile);

        return ResponseEntity.ok().body(transcribe);
    }
    // stt관련 운동 기록 저장
    @PostMapping(value = "/log", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> saveLog(@RequestBody Map<String,String> body) {

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null || auth.getPrincipal() == null) {
            return ResponseEntity.status(401).build();
        }
        String userId = (String) auth.getPrincipal();

        String transcript = body.get("transcript");
        if (transcript == null || transcript.isBlank()) {
            return ResponseEntity.badRequest().body("transcript이 없습니다. ");
        }

        try{
            Map<String, Object> parsed = openAiApiService.parseExercise(transcript);
//            String exerciseName = (String) parsed.get("exerciseName");
//
//            ExerciseData data = exerciseDataRepository
//                    .findByExerciseName(exerciseName)
//                    .orElseThrow(() -> new IllegalArgumentException("등록되지 않은 운동: " + exerciseName));

            String rawName = (String) parsed.get("exerciseName");
            String normalized = rawName.replaceAll("\\s+", "").toLowerCase();
            ExerciseData data = exerciseDataRepository
                    .findByNormalizedName(normalized)
                    .orElseThrow(() -> new IllegalArgumentException("등록되지 않은 운동: "+ rawName));
            String type = data.getExerciseType();

            ExerciseLogDto dto = new ExerciseLogDto();

            if("CARDIO".equals(type)){
                int minutes = ((Number) parsed.get("time")).intValue();
                double distance = ((Number) parsed.get("distance")).doubleValue();

                dto.setExerciseId(data.getExerciseId());
                dto.setTime(minutes);
                dto.setDistance(distance);
            }
            else if("STRENGTH".equals(type)){
                int sets = ((Number) parsed.get("sets")).intValue();
                double weight = ((Number) parsed.get("weight")).doubleValue();
                int reps = ((Number) parsed.get("reps")).intValue();

                dto.setExerciseId(data.getExerciseId());
                List<StrengthSetLogDto> setsLog = new ArrayList<>();
                for(int i=1; i<=sets; i++){
                    StrengthSetLogDto s = new StrengthSetLogDto();
                    s.setSets(i);
                    s.setWeight(weight);
                    s.setReps(reps);
                    setsLog.add(s);
                }
                dto.setStrengthSets(setsLog);
            }else {
                return ResponseEntity.badRequest().body("지원하지 않는 운동 타입: " + type);
            }

            ExerciseLog saved = exerciseService.saveExerciseLog(userId, dto);
            try {
                calorieAnalysisService.analyzeAndSave(saved);
            } catch (Exception e) {
                log.error("칼로리 분석 중 오류", e);
            }
            return ResponseEntity.ok("운동기록이 추가 되었습니다.");
        }catch (IllegalArgumentException e ){
            return ResponseEntity.badRequest().body(e.getMessage());
        }

    }
}
