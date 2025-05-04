package com.Hansung.Capston.service.Exercise;

import com.Hansung.Capston.common.ExerciseType;
import com.Hansung.Capston.dto.Exersice.ExerciseLogDto;
import com.Hansung.Capston.dto.Exersice.ExerciseSave.ExerciseSaveRequest;
import com.Hansung.Capston.entity.Exercise.*;
import com.Hansung.Capston.entity.User;
import com.Hansung.Capston.repository.Exercise.*;
import com.Hansung.Capston.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ExerciseService {
    @Autowired
    private ExerciseSaveRepository exerciseSaveRepository;
    @Autowired
    private ExerciseLogRepository exerciseLogRepository;
    @Autowired
    private CardioExerciseLogRepository cardioExerciseLogRepository;
    @Autowired
    private StrengthExerciseLogRepository strengthExerciseLogRepository;

    @Autowired
    private ExerciseDataRepository exerciseDataRepository;
    @Autowired
    private UserRepository userRepository;

    /*운동 기록 관련*/
    //운동 기록 저장
    public ExerciseLog saveExerciseLog(String userId, ExerciseLogDto dto) {

        //유저 조희
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("존재 하지 않은 유저입니다."));
        //운동데이터 조회
        ExerciseData data = exerciseDataRepository.findById(dto.getExerciseId())
                .orElseThrow(() -> new RuntimeException("운동 데이터가 존재하지 않습니다. id=" + dto.getExerciseId()));

        ExerciseLog exerciseLog = new ExerciseLog();
        exerciseLog.setUser(user);
        exerciseLog.setExerciseData(data);
        exerciseLog.setExerciseType(ExerciseType.valueOf(data.getExerciseType()));
        exerciseLog.setCreatedAt(LocalDateTime.now());

        exerciseLogRepository.save(exerciseLog);

        // 운동 타입에 따라 저장
        if (exerciseLog.getExerciseType() == ExerciseType.CARDIO) {
            CardioExerciseLog cardioLog = new CardioExerciseLog();
            cardioLog.setExerciseLog(exerciseLog);
            cardioLog.setDistance(dto.getDistance());
            cardioLog.setTime(dto.getTime());
            cardioLog.setPace(dto.getPace());
            cardioExerciseLogRepository.save(cardioLog);
        } else if (exerciseLog.getExerciseType() == ExerciseType.STRENGTH) {
            StrengthExerciseLog strengthLog = new StrengthExerciseLog();
            strengthLog.setExerciseLog(exerciseLog);
            strengthLog.setSets(dto.getSets());
            strengthLog.setReps(dto.getReps());
            strengthLog.setWeight(dto.getWeight());
            strengthExerciseLogRepository.save(strengthLog);
        }
        return exerciseLog;
    }
    //운동 기록 조회
    public List<ExerciseLogDto> searchExerciseLog(String userId) {
        List<ExerciseLog> logs = exerciseLogRepository.serachUserlog(userId);

        return logs.stream()
                .map(log -> {
                    ExerciseLogResponseDto dto = new ExerciseLogResponseDto();
                    dto.setLogId(log.getLogId());
                    dto.setExerciseId(log.getExerciseData().getId());
                    dto.setExerciseName(log.getExerciseData().getName());
                    dto.setExerciseType(log.getExerciseType());
                    dto.setCreatedAt(log.getCreatedAt());

                    if (log.getExerciseType() == ExerciseType.CARDIO) {
                        CardioExerciseLog cardio = cardioExerciseLogRepository
                                .findByExerciseLog_LogId(log.getLogId())
                                .orElse(null);
                        if (cardio != null) {
                            dto.setDistance(cardio.getDistance());
                            dto.setTime(cardio.getTime());
                            dto.setPace(cardio.getPace());
                        }
                    } else {
                        StrengthExerciseLog strength = strengthExerciseLogRepository
                                .findByExerciseLog_LogId(log.getLogId())
                                .orElse(null);
                        if (strength != null) {
                            dto.setSets(strength.getSets());
                            dto.setReps(strength.getReps());
                            dto.setWeight(strength.getWeight());
                        }
                    }
                    return dto;
                })
                .collect(Collectors.toList());
    }
    }


    /*즐겨찾기 관련*/
    //즐겨찾기 추가
    public ExerciseSave addfavoriteEx(String userId, ExerciseSaveRequest dto) {
        //만약 이미 즐겨찾기가 존재하면 추가하지 않음
        if (exerciseSaveRepository.findByUserIdAndExerciseId(userId, dto.getExerciseId()).isPresent()) {
            return null;
        }

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("존재 하지 않은 유저입니다."));

        ExerciseSave exerciseSave = new ExerciseSave();
        exerciseSave.setUser(user);
        exerciseSave.setExerciseId(dto.getExerciseId());
        return exerciseSaveRepository.save(exerciseSave);
    }
    //사용자에 따른 즐겨찾기 조회
    public List<ExerciseSave> searchfavoriteEx(String userId) {
        return exerciseSaveRepository.findAllByUserId(userId);
    }

    //저장된 즐겨찾기 운동 삭제
    public void removefavoriteEx(String userId, Integer exerciseId) {
        Optional<ExerciseSave> existing = exerciseSaveRepository.findByUserIdAndExerciseId(userId, exerciseId);
        existing.ifPresent(exerciseSaveRepository::delete);
    }


}
