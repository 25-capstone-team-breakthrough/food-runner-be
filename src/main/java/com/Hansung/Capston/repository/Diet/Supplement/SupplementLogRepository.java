package com.Hansung.Capston.repository.Diet.Supplement;

import com.Hansung.Capston.entity.Diet.Supplement.SupplementLog;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SupplementLogRepository extends JpaRepository<SupplementLog, Long> {

  List<SupplementLog> findByUserUserId(String userId);
}
