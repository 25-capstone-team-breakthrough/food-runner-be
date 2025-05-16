package com.Hansung.Capston.repository.UserInfo.Inbody;

import com.Hansung.Capston.entity.UserInfo.Inbody.Inbody;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface InbodyRepository extends JpaRepository<Inbody, Integer> {

    List<Inbody> findAllByUserIdOrderByCreatedAtDesc(String userId);
}
