package com.Hansung.Capston.dto.Diet.Supplement;

import com.Hansung.Capston.entity.Diet.Supplement.SupplementData;
import com.Hansung.Capston.entity.UserInfo.User;
import jakarta.persistence.Column;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import java.time.LocalDateTime;
import lombok.Data;

@Data
public class SupplementLogResponse {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "supplement_log_id")
  private Long supplementLogId;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "user_id", nullable = false)
  private User user;

  @ManyToOne(fetch = FetchType.EAGER)
  @JoinColumn(name = "sup_id", nullable = false)
  private SupplementData supplementData;

  @Column(name = "date", nullable = false)
  private LocalDateTime date;
}
