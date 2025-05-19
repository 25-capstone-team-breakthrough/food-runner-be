package com.Hansung.Capston.entity.UserInfo.Inbody;

import com.Hansung.Capston.entity.UserInfo.User;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name="inbody")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Inbody {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer inbodyId;

    @JsonIgnore
    @Getter
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @JsonProperty("userId")
    public String getUserId() {
        return user != null ? user.getUserId() : null;
    }

    private Float bodyWater;
    private Float protein;
    private Float minerals;
    private Float bodyFatAmount;
    private Float weight;
    private Float skeletalMuscleMass;
    private Float bmi;
    private Float bodyFatPercentage;
    private String segmentalLeanAnalysis;
    private String segmentalFatAnalysis;

    private LocalDateTime createdAt;
}
