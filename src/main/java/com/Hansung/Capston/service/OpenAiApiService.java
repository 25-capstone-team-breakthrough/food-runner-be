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
    TextContent textContent = new TextContent("이 이미지를 보고 어떤 음식인지 파악해줘.  \n"
        + "그리고 해당 음식의 1인분 기준 영양 성분을 아래 JSON 형식으로 알려줘.  \n"
        + "모든 값은 그램(g), 밀리그램(mg), 또는 kcal 단위로 추정해서 정리해줘.  \n"
        + "만약 특정 영양소를 알 수 없다면 null로 표시해줘.\n"
        + "\n"
        + "아래는 내가 원하는 응답 형식이야:\n"
        + "\n"
        + "{\n"
        + "  \"foodName\": \"\",\n"
        + "  \"calories\": 0,\n"
        + "  \"protein\": 0,\n"
        + "  \"carbohydrate\": 0,\n"
        + "  \"fat\": 0,\n"
        + "  \"sugar\": 0,\n"
        + "  \"sodium\": 0,\n"
        + "  \"dietaryFiber\": 0,\n"
        + "  \"calcium\": 0,\n"
        + "  \"saturatedFat\": 0,\n"
        + "  \"transFat\": 0,\n"
        + "  \"cholesterol\": 0,\n"
        + "  \"vitaminA\": 0,\n"
        + "  \"vitaminB1\": 0,\n"
        + "  \"vitaminC\": 0,\n"
        + "  \"vitaminD\": 0,\n"
        + "  \"vitaminE\": 0,\n"
        + "  \"magnesium\": 0,\n"
        + "  \"zinc\": 0,\n"
        + "  \"lactium\": 0,\n"
        + "  \"potassium\": 0,\n"
        + "  \"lArginine\": 0,\n"
        + "  \"omega3\": 0\n"
        + "}\n");

    List<Content> list = new ArrayList<>();
    list.add(textContent);
    list.add(imageContent);

    ImageAnalysisOpenAiApiRequest input = new ImageAnalysisOpenAiApiRequest(model,list);
    OpenAiApiResponse openAiApiResponse = restTemplate.postForObject(openAiUrl, input,
        OpenAiApiResponse.class);

    return openAiApiResponse.getChoices().get(0).getMessage().getContent();
  }


}
