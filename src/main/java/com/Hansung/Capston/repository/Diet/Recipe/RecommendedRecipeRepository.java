package com.Hansung.Capston.repository.Diet.Recipe;

import com.Hansung.Capston.common.DayOfWeek;
import com.Hansung.Capston.common.DietType;
import com.Hansung.Capston.entity.Diet.Recipe.RecommendedRecipe;
import com.Hansung.Capston.entity.UserInfo.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface RecommendedRecipeRepository extends JpaRepository<RecommendedRecipe, Long> {
    Optional<RecommendedRecipe> findByUserAndDateAndType(User user, DayOfWeek date, DietType type);

    List<RecommendedRecipe> findByUser_UserId(String user_userId);
}
