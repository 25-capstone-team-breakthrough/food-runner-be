package com.Hansung.Capston.dto.MealLog;
import com.Hansung.Capston.entity.MealType;
import java.time.LocalDateTime;
import lombok.*;

@Data
@Getter
@Setter
public class ImageMealLogCreateRequest {
  private String userId = null;  // 사용자 ID
  private MealType type = MealType.image;  // ENUM ('search', 'image')
  private LocalDateTime date;

  // 이미지 로그용
  private String mealImage;
}
