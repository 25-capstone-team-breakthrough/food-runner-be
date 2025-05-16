package com.Hansung.Capston.repository.UserInfo.Inbody;

import com.Hansung.Capston.entity.UserInfo.Inbody.InbodyImage;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface InbodyImageRepository extends JpaRepository<InbodyImage, Integer> {
    List<InbodyImage> findAllByUserIdOrderByCreatedAtDesc(String userId);
}
