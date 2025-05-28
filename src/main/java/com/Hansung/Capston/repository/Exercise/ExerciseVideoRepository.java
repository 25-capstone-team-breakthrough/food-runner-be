package com.Hansung.Capston.repository.Exercise;

import com.Hansung.Capston.entity.Exercise.ExerciseVideo;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;


public interface ExerciseVideoRepository extends JpaRepository<ExerciseVideo, Long> {
    List<ExerciseVideo> findByUserUserId(String userId);

    //사용자 + 카테고리
    List<ExerciseVideo> findByUserUserIdAndCategory(String userId, String category);

    void deleteByUserUserId(String userId);
}
