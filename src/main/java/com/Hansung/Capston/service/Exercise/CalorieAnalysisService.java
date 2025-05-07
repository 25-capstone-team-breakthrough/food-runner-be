package com.Hansung.Capston.service.Exercise;

import com.Hansung.Capston.common.ExerciseType;
import com.Hansung.Capston.dto.Exersice.ExerciseLogCalories.ExerciseLogCaloriesDto;
import com.Hansung.Capston.entity.UserInfo.BMI;
import com.Hansung.Capston.entity.Exercise.*;
import com.Hansung.Capston.repository.UserInfo.BMIRepository;
import com.Hansung.Capston.repository.Exercise.CardioExerciseLogRepository;
import com.Hansung.Capston.repository.Exercise.ExerciseDataRepository;
import com.Hansung.Capston.repository.Exercise.ExerciseLogCaloriesRepository;
import com.Hansung.Capston.repository.Exercise.StrengthExerciseLogRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor // 생성자 알아서 만들어줌 진짜 개꿀이네
@Slf4j
public class CalorieAnalysisService {

    private final BMIRepository bmiRepository;
    private final ExerciseDataRepository exerciseDataRepository;
    private final ExerciseLogCaloriesRepository exerciseLogCaloriesRepository;

    private final CardioExerciseLogRepository cardioExerciseLogRepository;
    private final StrengthExerciseLogRepository strengthExerciseLogRepository;

    //유산소
    private int calculateCaloriesByCardio(double weightKg, int paceMinPerKm, int durationMin) {
        double speed     = 1000.0 / paceMinPerKm;      // m/min
        double vo2       = 0.2 * speed + 3.5;          // ml/kg/min
        double kcalPerMin= vo2 * weightKg / 200.0;     // kcal/min
        return (int) Math.round(kcalPerMin * durationMin);
    }

    //근력
    private int calculateCaloriesByStrength(double weightKg, int sets, int reps, Double weight, double met) {
        double repTimeSec = 3.0;
        double hours = (sets * reps)* repTimeSec / 3600.0;
        double weightForCalc = (weight != null && weight > 0)
                ? weight
                : weightKg;
        double calories = met * weightForCalc * hours;
        return (int) Math.round(calories);
    }

    // 소모칼로리 분석
    public void analyzeAndSave(ExerciseLog exerciseLog) {
        //운동데이터 조회
        ExerciseData data = exerciseDataRepository.findById(exerciseLog.getExerciseData().getExerciseId())
                .orElseThrow();

        // 사용자 BMI 조회
        BMI bmi = bmiRepository.findById(exerciseLog.getUser().getUserId())
                .orElseThrow(() -> new RuntimeException("BMI 정보가 없습니다."));
        double userWeight = bmi.getWeight(); //kg


        int calories = 0;

        if (exerciseLog.getExerciseType() == ExerciseType.CARDIO) {
            // 1) 유산소계산
            CardioExerciseLog cardio = cardioExerciseLogRepository
                    .findByExerciseLog_LogId(exerciseLog.getLogId())
                    .orElseThrow(() -> new IllegalArgumentException("유산소 기록이 없습니다."));

            int minutes = cardio.getTime();              // 분
            int pace = (int) Math.round(cardio.getPace());  // min/km
            calories = calculateCaloriesByCardio(userWeight, pace, minutes);

        }
        else { //근력 계산
            List<StrengthExerciseLog> sets = strengthExerciseLogRepository
                    .findAllByExerciseLog_LogId(exerciseLog.getLogId());
            for (StrengthExerciseLog s : sets) {
                calories += calculateCaloriesByStrength(
                        userWeight,
                        s.getSets(),
                        s.getReps(),
                        s.getWeight(),
                        data.getEnergyConsumptionPerKg()
                );
            }
        }


        ExerciseLogCalories result = ExerciseLogCalories.builder()
                .exerciseLog(exerciseLog)
                .caloriesBurned(calories)
                .createdAt(LocalDateTime.now())
                .build();
        exerciseLogCaloriesRepository.save(result);

    }


    //소모칼로리 조회
    public List<ExerciseLogCaloriesDto> getCaloriesByUser(String userId) {
        List<ExerciseLogCalories> logs = exerciseLogCaloriesRepository
                .findAllByExerciseLog_User_UserIdOrderByCreatedAtDesc(userId);
        return logs.stream()
                .map(log -> new ExerciseLogCaloriesDto(
                        log.getCalorieLogId(),
                        log.getExerciseLog().getLogId(),
                        log.getCaloriesBurned(),
                        log.getCreatedAt()
                ))
                .collect(Collectors.toList());
    }
}
