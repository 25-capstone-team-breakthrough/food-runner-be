package com.Hansung.Capston.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity                     // JPA가 이 클래스가 데이터베이스 테이블과 매핑되는 엔티티임을 인식
@Table(name = "BMI")       // 데이터베이스 테이블 이름을 "User"로 지정
@Data                      // Lombok 어노테이션: getter, setter, toString, equals, hashCode 메서드 자동 생성
@NoArgsConstructor         // Lombok 어노테이션: 기본 생성자 자동 생성 (JPA에서 필요)
@AllArgsConstructor
public class BMI {

    @Id
    @Column(name = "user_id", columnDefinition = "CHAR(36)")
    private String userId;

    // User 엔티티와 1:1 매핑 (공유 primary key)
    @OneToOne
    @MapsId
    @JoinColumn(name = "user_id")
    private User user;

    @Column(name = "age")
    private int age;

    @Column(name = "gender", length = 10)
    private String gender;

    @Column(name = "height")
    private float height;  // 미터 단위로 저장한다고 가정

    @Column(name = "weight")
    private float weight;

}
