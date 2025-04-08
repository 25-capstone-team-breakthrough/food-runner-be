package com.Hansung.Capston.service;

import com.Hansung.Capston.dto.ExersiceSave.ExerciseSaveDto;
import com.Hansung.Capston.entity.ExerciseSave;
import com.Hansung.Capston.repository.ExerciseSaveRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ExerciseService {

    private final ExerciseSaveRepository exerciseSaveRepository;

    public ExerciseService(ExerciseSaveRepository exerciseSaveRepository) {
        this.exerciseSaveRepository = exerciseSaveRepository;
    }

    //즐겨찾기 추가
    public ExerciseSave saveExercise(String userId, ExerciseSaveDto dto) {
        //만약 이미 즐겨찾기가 존재하면 추가하지 않음
        if (exerciseSaveRepository.findByUserIdAndExerciseId(userId, dto.getExerciseId()).isPresent()) {
            return null;
        }
        ExerciseSave exerciseSave = new ExerciseSave();
        exerciseSave.setUserId(userId);
        exerciseSave.setExerciseId(dto.getExerciseId());
        return exerciseSaveRepository.save(exerciseSave);
    }

    //저장된 즐겨찾기 운동 삭제
    public void removeExerciseSave(String userId, Integer exerciseId) {
        Optional<ExerciseSave> existing = exerciseSaveRepository.findByUserIdAndExerciseId(userId, exerciseId);
        existing.ifPresent(exerciseSaveRepository::delete);
    }
    //저장된 운동 전체 저장 목록 조회
    public List<ExerciseSave> getExerciseSaves(String userId) {
        return exerciseSaveRepository.findAllByUserId(userId);
    }
}
