package com.Hansung.Capston.entity.UserInfo;

import com.Hansung.Capston.common.MemberType;
import jakarta.persistence.*;
import lombok.*;

import java.util.UUID;

@Entity                     // JPA가 이 클래스가 데이터베이스 테이블과 매핑되는 엔티티임을 인식
@Table(name = "User")       // 데이터베이스 테이블 이름을 "User"로 지정
@Data                      // Lombok 어노테이션: getter, setter, toString, equals, hashCode 메서드 자동 생성
@NoArgsConstructor         // Lombok 어노테이션: 기본 생성자 자동 생성 (JPA에서 필요)
@AllArgsConstructor        // Lombok 어노테이션: 모든 필드를 인자로 하는 생성자 자동 생성
public class User {

    @Id //PK
    @Column(name = "user_id", columnDefinition = "CHAR(36)",nullable = false, updatable = false)//User 엔티티가 저장될때 마다 userId가 장동으로 값 생성
    private String userId; //UUID

    @Column(name = "id", length = 15, nullable = false, unique = true)
    private String account;//아이디

    @Column(name = "name", length = 15, nullable = false)
    private String name;//이름

    @Column(name = "password", nullable = false)
    private String password;//비밀번호

    @Enumerated(EnumType.STRING)
    @Column(name = "role")
    private MemberType role; //유저인지 관리자인지

//    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
//    private List<ExerciseSave> favorites = new ArrayList<>();

    // user_id를 자동 생성하도록 설정
    @PrePersist
    public void prePersist() {
        if (this.userId == null) {
            this.userId = UUID.randomUUID().toString();
        }
    }


}
