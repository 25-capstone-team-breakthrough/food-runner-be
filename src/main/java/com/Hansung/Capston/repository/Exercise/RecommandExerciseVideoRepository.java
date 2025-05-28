package com.Hansung.Capston.repository.Exercise;

import com.Hansung.Capston.entity.Exercise.RecommandExerciseVideo;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RecommandExerciseVideoRepository extends JpaRepository<RecommandExerciseVideo, Long> {
    List<RecommandExerciseVideo> findByUserUserId(String userId);

    void deleteByUserUserId(String userId);
}
