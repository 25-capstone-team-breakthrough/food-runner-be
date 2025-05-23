package com.Hansung.Capston.repository.UserInfo;

import com.Hansung.Capston.entity.UserInfo.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, String> {
    Optional<User> findByAccount(String account);
    Optional<User> findByUserId(String userId);
}
