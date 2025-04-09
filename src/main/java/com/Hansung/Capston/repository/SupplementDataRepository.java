package com.Hansung.Capston.repository;

import com.Hansung.Capston.entity.SupplementData;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SupplementDataRepository extends JpaRepository<SupplementData, Long> {

}
