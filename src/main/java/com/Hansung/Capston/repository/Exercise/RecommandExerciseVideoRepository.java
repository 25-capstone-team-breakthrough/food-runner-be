package com.Hansung.Capston.repository.Exercise;

import com.Hansung.Capston.entity.Exercise.RecommandExerciseVideo;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RecommandExerciseVideoRepository extends JpaRepository<RecommandExerciseVideo, Long> {

    /**
     * 특정 사용자ID로 추천 운동 영상 조회
     */
    List<RecommandExerciseVideo> findByUserUserId(String userId);

    void deleteByUserUserId(String userId);
}
