package com.Hansung.Capston.repository.UserInfo.Inbody;

import com.Hansung.Capston.entity.UserInfo.Inbody.Inbody;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface InbodyRepository extends JpaRepository<Inbody, Integer> {

    List<Inbody> findAllByUserUserIdOrderByCreatedAtDesc(String userId);

    Optional<Inbody> findByInbodyIdAndUser_UserId(Integer inbodyId, String userId);
}
