package com.Hansung.Capston.repository.Diet.Recipe;

import com.Hansung.Capston.common.DietType;
import com.Hansung.Capston.entity.Diet.Recipe.RecommendedRecipeCandidate;
import com.Hansung.Capston.entity.UserInfo.User;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RecommendedRecipeCandidateRepository extends
    JpaRepository<RecommendedRecipeCandidate, Long> {

  void deleteByUser(User user);

  List<RecommendedRecipeCandidate> findByUserAndDietType(User user, DietType type);

}
