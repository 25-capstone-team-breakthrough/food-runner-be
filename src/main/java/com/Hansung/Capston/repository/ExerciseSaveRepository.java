package com.Hansung.Capston.repository;

import com.Hansung.Capston.dto.ExersiceSave.ExerciseSaveDto;
import com.Hansung.Capston.entity.ExerciseSave;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ExerciseSaveRepository extends JpaRepository<ExerciseSave, String> {
}
