package com.Hansung.Capston.repository.UserInfo.Inbody;

import com.Hansung.Capston.entity.UserInfo.Inbody.InbodyImage;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface InbodyImageRepository extends JpaRepository<InbodyImage, Integer> {
    List<InbodyImage> findAllByUserIdOrderByCreatedAtDesc(String userId);

    Optional<InbodyImage> findByInbody_InbodyIdAndInbody_User_UserId(Integer inbodyId, String userId);

}
