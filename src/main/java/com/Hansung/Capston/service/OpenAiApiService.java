package com.Hansung.Capston.service;

import com.Hansung.Capston.dto.MealLog.FoodDataDTO;
import com.Hansung.Capston.dto.MealLog.ImageMealLogCreateRequest;
import com.Hansung.Capston.dto.OpenAiApi.Content;
import com.Hansung.Capston.dto.OpenAiApi.ImageAnalysisOpenAiApiRequest;
import com.Hansung.Capston.dto.OpenAiApi.ImageContent;
import com.Hansung.Capston.dto.OpenAiApi.Message;
import com.Hansung.Capston.dto.OpenAiApi.OpenAiApiResponse;
import com.Hansung.Capston.dto.OpenAiApi.TextAnalysisOpenAiApiRequest;
import com.Hansung.Capston.dto.OpenAiApi.TextContent;
import java.util.ArrayList;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class OpenAiApiService {
  private static final Logger logger = LoggerFactory.getLogger(OpenAiApiService.class);

  @Autowired
  @Qualifier("openAiTemplate")
  private final RestTemplate restTemplate;
  @Value("${openai.api.url}")
  private String openAiUrl;
  @Value("${openai.model}")
  private String model;


  @Autowired
  public OpenAiApiService(RestTemplate restTemplate, NutrientService nutrientService) {
    this.restTemplate = restTemplate;
  }

  public List<String> mealImageAnalysis(ImageMealLogCreateRequest request){

    ImageContent imageContent = new ImageContent(request.getMealImage());
    TextContent textContent = new TextContent("너는 오로지 음식이 뭔지만 판단하는 음식이미지 분석 ai 모델이야. "
        + "이미지를 보고 어떤 음식이 있는지 우리한테 알려줘야해(재료는 제외). 사족은 빼고 어떤 음식이 있는지만 알려줘. 또한 너무 포괄적으로 얘기하지는 말되 그렇다고 모르는 거 있으면 대답에 포함하지말고. 3초 이내에");


    List<Content> list = new ArrayList<>();
    list.add(textContent);
    list.add(imageContent);



    ImageAnalysisOpenAiApiRequest input = new ImageAnalysisOpenAiApiRequest(model,list);
    OpenAiApiResponse openAiApiResponse = restTemplate.postForObject(openAiUrl, input,
        OpenAiApiResponse.class);

    String response = openAiApiResponse.getChoices().get(0).getMessage().getContent();
    String trim = response.trim();

    List<String> foodArray = List.of(trim.split(",\\s*"));
    logger.info("Food Array: {}", foodArray);  // 로그 출력
    return foodArray;
  }

  public FoodDataDTO getNutrientInfo(String food) {
    // 텍스트 기반 요청 내용
    String prompt = food + "에 대한 영양 정보를 1인분 기준으로 추정하여 아래와 같은 형식으로 반환해주세요. 각 항목은 `key=value` 형태로 출력하고, 각 항목은 쉼표(,)로 구분해주세요.\n"
        + "예시 형식:\n"
        + "foodName=짜장면, calories=800, protein=20, carbohydrate=130, fat=15, sugar=12, sodium=2000, dietaryFiber=5, calcium=40, saturatedFat=3, transFat=0, cholesterol=50, vitaminA=50, vitaminB1=0.5, vitaminC=2, vitaminD=null, vitaminE=null, magnesium=30, zinc=0.8, lactium=null, potassium=300, lArginine=null, omega3=null";

    // TextContent 객체 생성
    List<Message> messages = new ArrayList<>();
    messages.add(new Message("user", prompt));


    // TextAnalysisOpenAiApiRequest 객체 생성
    TextAnalysisOpenAiApiRequest input = new TextAnalysisOpenAiApiRequest(model, messages);

    // OpenAI API 호출
    OpenAiApiResponse openAiApiResponse = restTemplate.postForObject(openAiUrl, input, OpenAiApiResponse.class);

    // 응답 처리
    if (openAiApiResponse != null && openAiApiResponse.getChoices() != null) {
      String content = openAiApiResponse.getChoices().get(0).getMessage().getContent();
      content = content.trim();

      // 응답 문자열을 쉼표로 구분하여 파싱
      String[] nutrientArray = content.split(",");

      // FoodDataResponse 객체 생성
      FoodDataDTO foodDataResponse = new FoodDataDTO();

      // nutrientArray를 순회하며 각 key=value 데이터를 파싱하고 FoodDataResponse에 세팅
      for (String nutrient : nutrientArray) {
        String[] keyValue = nutrient.split("=");
        if (keyValue.length == 2) {
          String key = keyValue[0].trim();
          String value = keyValue[1].trim();

          if (value.equals("null")) {
            value = null;
          }

          switch (key) {
            case "foodName":
              foodDataResponse.setFoodName(value);
              break;
            case "calories":
              foodDataResponse.setCalories(value != null ? Double.parseDouble(value) : 0);
              break;
            case "protein":
              foodDataResponse.setProtein(value != null ? Double.parseDouble(value) : 0);
              break;
            case "carbohydrate":
              foodDataResponse.setCarbohydrate(value != null ? Double.parseDouble(value) : 0);
              break;
            case "fat":
              foodDataResponse.setFat(value != null ? Double.parseDouble(value) : 0);
              break;
            case "sodium":
              foodDataResponse.setSodium(value != null ? Double.parseDouble(value) : 0);
              break;
            case "dietaryFiber":
              foodDataResponse.setDietaryFiber(value != null ? Double.parseDouble(value) : 0);
              break;
            case "calcium":
              foodDataResponse.setCalcium(value != null ? Double.parseDouble(value) : 0);
              break;
            case "saturatedFat":
              foodDataResponse.setSaturatedFat(value != null ? Double.parseDouble(value) : 0);
              break;
            case "transFat":
              foodDataResponse.setTransFat(value != null ? Double.parseDouble(value) : 0);
              break;
            case "cholesterol":
              foodDataResponse.setCholesterol(value != null ? Double.parseDouble(value) : 0);
              break;
            case "vitaminA":
              foodDataResponse.setVitaminA(value != null ? Double.parseDouble(value) : 0);
              break;
            case "vitaminB1":
              foodDataResponse.setVitaminB1(value != null ? Double.parseDouble(value) : 0);
              break;
            case "vitaminC":
              foodDataResponse.setVitaminC(value != null ? Double.parseDouble(value) : 0);
              break;
            case "vitaminD":
              foodDataResponse.setVitaminD(value != null ? Double.parseDouble(value) : 0);
              break;
            case "vitaminE":
              foodDataResponse.setVitaminE(value != null ? Double.parseDouble(value) : 0);
              break;
            case "magnesium":
              foodDataResponse.setMagnesium(value != null ? Double.parseDouble(value) : 0);
              break;
            case "zinc":
              foodDataResponse.setZinc(value != null ? Double.parseDouble(value) : 0);
              break;
            case "lactium":
              foodDataResponse.setLactium(value != null ? Double.parseDouble(value) : 0);
              break;
            case "potassium":
              foodDataResponse.setPotassium(value != null ? Double.parseDouble(value) : 0);
              break;
            case "lArginine":
              foodDataResponse.setLArginine(value != null ? Double.parseDouble(value) : 0);
              break;
            case "omega3":
              foodDataResponse.setOmega3(value != null ? Double.parseDouble(value) : 0);
              break;
            default:
              break;
          }
        }
      }

      return foodDataResponse;
    }

    return null;
  }



}
