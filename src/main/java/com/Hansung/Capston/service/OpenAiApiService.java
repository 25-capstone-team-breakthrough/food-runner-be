package com.Hansung.Capston.service;

import com.Hansung.Capston.dto.MealLog.ImageMealLogCreateRequest;
import com.Hansung.Capston.dto.OpenAiApi.Content;
import com.Hansung.Capston.dto.OpenAiApi.ImageAnalysisOpenAiApiRequest;
import com.Hansung.Capston.dto.OpenAiApi.ImageContent;
import com.Hansung.Capston.dto.OpenAiApi.OpenAiApiResponse;
import com.Hansung.Capston.dto.OpenAiApi.TextContent;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class OpenAiApiService {
  @Autowired
  @Qualifier("openAiTemplate")
  private final RestTemplate restTemplate;
  @Value("${openai.api.url}")
  private String openAiUrl;
  @Value("${openai.model}")
  private String model;


  public OpenAiApiService(RestTemplate restTemplate) {
    this.restTemplate = restTemplate;
  }

  public String mealImageAnalysis(ImageMealLogCreateRequest request){
    byte[] image = request.getMealImage();
    String imageUrl = Base64.getEncoder().encodeToString(image);

    ImageContent imageContent = new ImageContent(imageUrl);
    TextContent textContent = new TextContent("이 이미지를 보고 어떤 음식인지 파악해줘. 그리고 생각되는 음식들을 3개로 추려줘  \n"
        + "그리고 각각의 해당하는 음식의 1인분 기준 영양 성분을 아래 JSON 형식으로 알려줘.  \n"
        + "아래의 배열을 참조해서 모든 값은 그램(g), 밀리그램(mg), 또는 kcal 단위로 추정해서 정리해줘.  \n"
        + "만약 특정 영양소를 알 수 없다면 null로 표시해줘. JSON 외의 다른 말은 하지마 코드블럭(```json)도 쓰지 마. 그냥 json 배열 형태 그대로 리턴해줘 fooname 필드는 한글로 적어줘\n"
        + "{"
        + "  foodName: ,"
        + "  calories: 0,"
        + "  protein: 0,"
        + "  carbohydrate: 0,"
        + "  fat: 0,"
        + "  sugar: 0,"
        + "  sodium: 0,"
        + "  dietaryFiber: 0,"
        + "  calcium: 0,"
        + "  saturatedFat: 0,"
        + "  transFat: 0,"
        + "  cholesterol: 0,"
        + "  vitaminA: 0,"
        + "  vitaminB1: 0,"
        + "  vitaminC: 0,"
        + "  vitaminD: 0,"
        + "  vitaminE: 0,"
        + "  magnesium: 0,"
        + "  zinc: 0,"
        + "  lactium: 0,"
        + "  potassium: 0,"
        + "  lArginine: 0,"
        + "  omega3: 0"
        + "}");

    List<Content> list = new ArrayList<>();
    list.add(textContent);
    list.add(imageContent);

    ImageAnalysisOpenAiApiRequest input = new ImageAnalysisOpenAiApiRequest(model,list);
    OpenAiApiResponse openAiApiResponse = restTemplate.postForObject(openAiUrl, input,
        OpenAiApiResponse.class);

    return openAiApiResponse.getChoices().get(0).getMessage().getContent();
  }


}
