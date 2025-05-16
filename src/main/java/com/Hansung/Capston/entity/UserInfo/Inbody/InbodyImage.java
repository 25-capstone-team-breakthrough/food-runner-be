package com.Hansung.Capston.entity.UserInfo.Inbody;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name="inbody_image")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class InbodyImage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer pictureId;

    @ManyToOne @JoinColumn(name="inbody_id")
    private Inbody inbody;

    private String userId;
    private String filePath;
    private LocalDateTime createdAt;
}
