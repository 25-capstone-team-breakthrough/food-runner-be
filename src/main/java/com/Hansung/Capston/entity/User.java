package com.Hansung.Capston.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.*;

@Entity                     // JPA가 이 클래스가 데이터베이스 테이블과 매핑되는 엔티티임을 인식
@Table(name = "User")       // 데이터베이스 테이블 이름을 "User"로 지정
@Data                      // Lombok 어노테이션: getter, setter, toString, equals, hashCode 메서드 자동 생성
@NoArgsConstructor         // Lombok 어노테이션: 기본 생성자 자동 생성 (JPA에서 필요)
@AllArgsConstructor        // Lombok 어노테이션: 모든 필드를 인자로 하는 생성자 자동 생성
public class User {

    @Id
    @Column(name = "user_id", length = 15, nullable = false)
    private String userId;

    @Column(name = "name", length = 15, nullable = false)
    private String name;

    @Column(name = "password", length = 15, nullable = false)
    private String password;

    @Column(name = "age")
    private int age;

    @Column(name = "gender", length = 10)
    private String gender;

    @Column(name = "height")
    private float height;

    @Column(name = "weight")
    private float weight;

}
