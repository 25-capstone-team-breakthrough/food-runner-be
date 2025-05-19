package com.Hansung.Capston.service.Exercise;

import com.Hansung.Capston.entity.Exercise.RecommandExerciseVideo;
import com.Hansung.Capston.repository.Exercise.RecommandExerciseVideoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RecommandExerciseVideoService {

    private final RecommandExerciseVideoRepository recommandExerciseVideoRepository;

    @Autowired
    public RecommandExerciseVideoService(RecommandExerciseVideoRepository recommandExerciseVideoRepository) {
        this.recommandExerciseVideoRepository = recommandExerciseVideoRepository;
    }

    //추천 영상 저장
    public RecommandExerciseVideo saveRecommendation(RecommandExerciseVideo video) {
        return recommandExerciseVideoRepository.save(video);
    }

    /**
     * 특정 사용자ID의 모든 추천 운동 영상을 조회합니다.
     */
    public List<RecommandExerciseVideo> getRecommendationsByUserId(String userId) {
        return recommandExerciseVideoRepository.findByUserUserId(userId);
    }

    //기존 추천운동 영상 삭제
    public void clearRecommendationsByUserId(String userId) {
        recommandExerciseVideoRepository.deleteByUserUserId(userId);
    }
}
