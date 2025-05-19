package com.Hansung.Capston.entity.Exercise;

import com.Hansung.Capston.entity.UserInfo.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "recommand_exercise_video")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class RecommandExerciseVideo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    /** 운동 카테고리 (예: 어깨, 가슴, 허벅지 등) */
    @Column(length = 50, nullable = false)
    private String category;

    /** YouTube 영상 ID */
    @Column(name = "video_id", length = 20, nullable = false)
    private String videoId;

    /** 영상 제목 */
    @Column(length = 255, nullable = false)
    private String title;

    /** 영상 URL */
    @Column(length = 255, nullable = false)
    private String url;

    /** AI 추천 여부 */
    @Column(name = "is_ai_recommendation", nullable = false)
    private Boolean isAIRecommendation = true;
}
