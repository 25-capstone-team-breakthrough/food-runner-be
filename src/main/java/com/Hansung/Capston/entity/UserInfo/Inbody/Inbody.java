package com.Hansung.Capston.entity.UserInfo.Inbody;

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

    private String userId;
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
