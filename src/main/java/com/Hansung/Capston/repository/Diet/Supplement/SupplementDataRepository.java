package com.Hansung.Capston.repository.Diet.Supplement;

import com.Hansung.Capston.entity.Diet.Supplement.PreferredSupplement;
import com.Hansung.Capston.entity.Diet.Supplement.SupplementData;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SupplementDataRepository extends JpaRepository<SupplementData, Long> {

}
