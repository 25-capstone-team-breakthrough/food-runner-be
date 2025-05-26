package com.Hansung.Capston.service.ApiService;

import com.Hansung.Capston.common.DayOfWeek;
import com.Hansung.Capston.common.DietType;
import com.Hansung.Capston.dto.Api.OpenAiApi.*;
import com.Hansung.Capston.entity.Diet.Food.FoodData;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@Slf4j
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
  public OpenAiApiService(RestTemplate restTemplate) {
    this.restTemplate = restTemplate;
  }

  public List<String> mealImageAnalysis(String mealImage){

    ImageContent imageContent = new ImageContent(mealImage);
    TextContent textContent = new TextContent("너는 오로지 음식이 뭔지만 판단하는 음식이미지 분석 ai 모델이야. "
        + "이미지를 보고 어떤 음식이 얼마나 있는지 우리한테 알려줘야해(재료는 제외). 사족은 빼고 어떤 음식이 있는지만 알려줘. 또한 너무 포괄적으로 얘기하지는 말되 그렇다고 모르는 거 있으면 대답에 포함하지말고. 3초 이내에"
        + "형식은 음식이름:g 단위로 나타낸 양이야. ex) 김치찌개:500. 음식의 구분은 무조건 쉼표(,)로 다른 특수문자는 사용하지마");


    List<Content> list = new ArrayList<>();
    list.add(textContent);
    list.add(imageContent);



    ImageAnalysisOpenAiApiRequest input = new ImageAnalysisOpenAiApiRequest(model,list);
    OpenAiApiResponse openAiApiResponse = restTemplate.postForObject(openAiUrl, input,
        OpenAiApiResponse.class);

    String response = openAiApiResponse.getChoices().get(0).getMessage().getContent();
    String trim = response.trim();

    List<String> foodArray = Arrays.stream(trim.split(",\\s*"))
        .map(s -> s.replaceAll("\\.$", "").trim()) // 맨 끝 마침표만 제거
        .collect(Collectors.toList());
    logger.info("Food Array: {}", foodArray);  // 로그 출력
    return foodArray;
  }

  public FoodData getNutrientInfo(String food) {
    // 텍스트 기반 요청 내용
    String prompt = food + "에 대한 영양 정보를 100g 기준으로 추정하여 아래와 같은 형식으로 반환해주세요. 각 항목은 `key=value` 형태로 출력하고, 각 항목은 쉼표(,)로 구분해주세요.\n"
        + "예시 형식:\n"
        + "foodName=짜장면, calories=800, protein=20, carbohydrate=130, fat=15, sugar=12, sodium=2000, dietaryFiber=5, calcium=40, saturatedFat=3, transFat=0, cholesterol=50, vitaminA=50, vitaminB1=0.5, vitaminC=2, vitaminD=null, vitaminE=null, magnesium=30, zinc=0.8, lactium=null, potassium=300, lArginine=null, omega3=null"
        + "만약에 사진에 음식이 없으면 foodName = null";

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
      FoodData foodDataResponse = new FoodData();

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
              if(value.equals("null")) {
                return null;
              }
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

  public String getRecommendedRecipes(List<String> recipes) {

    String prompt = String.join(", ", recipes) +
        "\n\nBased on ONLY these exact recipe names listed above (no modifications, no additions, no removals), and **you must use the recipe names precisely as they are provided, including all whitespace and characters**," +
        " generate a 7-day meal plan assigning them to breakfast, lunch, and dinner." +
        " You must use the recipe names EXACTLY as they appear above — do not change anything." +
        " Each day must follow this format:\n" +
        "breakfast1,breakfast2|lunch|dinner-breakfast|lunch1,lunch2|dinner-...(7 days total)\n" +
        "At most 2 recipes per meal. If there are 2, one must be a simple side dish like rice or kimchi." +
        " Return ONLY the raw text in that format. No explanations, greetings, or extra lines.";


    // TextContent 객체 생성
    List<Message> messages = new ArrayList<>();
    messages.add(new Message("user", prompt));


    // TextAnalysisOpenAiApiRequest 객체 생성
    TextAnalysisOpenAiApiRequest input = new TextAnalysisOpenAiApiRequest(model, messages);

    // OpenAI API 호출
    OpenAiApiResponse openAiApiResponse = restTemplate.postForObject(openAiUrl, input, OpenAiApiResponse.class);

    String content = openAiApiResponse.getChoices().get(0).getMessage().getContent();

    log.info("✅ LLM 반환 식단 추천 결과:\n{}", content);

    return content;
  }

  // Jackson ObjectMapper 자동 주입
  @Autowired
  private ObjectMapper objectMapper;
  //인바디 이미지 업로드시 인바디 텍스트  분석
  public Map<String, Float> extractInbodyMetrics(String ocrText) {
    // 1) 프롬프트 생성
    String systemPrompt = """
      You are an InBody numeric extractor.
                    Only use the raw OCR text provided in the user prompt.
                    Output ONLY a JSON object with these keys:
                      bodyWater, protein, minerals, bodyFatAmount,
                      weight, skeletalMuscleMass, bmi, bodyFatPercentage.
                    Values must be floats or null if not found.
                    No extra keys or commentary.
    """;

    String user = "OCR Text:\n```" + ocrText + "```";

    List<Message> msgs = List.of(
            new Message("system", systemPrompt),
            new Message("user",   user)
    );

    TextAnalysisOpenAiApiRequest req =
            new TextAnalysisOpenAiApiRequest(model, msgs,0.0);
    OpenAiApiResponse resp =
            restTemplate.postForObject(openAiUrl, req, OpenAiApiResponse.class);

    String json = resp.getChoices().get(0).getMessage().getContent()
            .replaceAll("^```.*?\\n", "")
            .replaceAll("```$", "")
            .trim();

    try {
      return objectMapper.readValue(
              json, new TypeReference<Map<String,Float>>(){}
      );
    } catch (Exception ex) {
      throw new RuntimeException("OpenAI 파싱 실패:\n" + json, ex);
    }
  }


  //운동 종류
  public List<String> recommendExercisesBySegments(List<String> segments, int count) {
    String targetStr = String.join(", ", segments);
    String userPrompt = String.format(
            "다음 부위에 적합한 운동 %d가지를 쉼표로 구분하여 알려줘. 부위: %s. 운동 이름만 출력해줘.",
            count, targetStr
    );
    List<Message> messages = List.of(
            new Message("system", "You are a fitness recommendation assistant."),
            new Message("user", userPrompt)
    );
    TextAnalysisOpenAiApiRequest req = new TextAnalysisOpenAiApiRequest(model, messages);
    OpenAiApiResponse resp = restTemplate.postForObject(openAiUrl, req, OpenAiApiResponse.class);
    String content = resp.getChoices().get(0).getMessage().getContent().trim();
    return Arrays.stream(content.split(",\\s*"))
            .map(String::trim)
            .collect(Collectors.toList());
  }

  //운동카테고리 분류
  public Map<String, String> categorizeExercises(List<String> exercises,
                                                 List<String> categories) {
    String exList = String.join(", ", exercises);
    String catList = String.join(", ", categories);
    String prompt = String.format(
            "아래 운동들을 다음 카테고리 중 하나로 분류해주세요. " +
                    "운동: %s\n카테고리: %s\n" +
                    "JSON 형식으로 {\"운동명\":\"카테고리\"} 형태로 반환해주세요.",
            exList, catList
    );

    List<Message> messages = List.of(
            new Message("system", "You are an exercise categorization assistant."),
            new Message("user", prompt)
    );
    TextAnalysisOpenAiApiRequest req = new TextAnalysisOpenAiApiRequest(model, messages);
    OpenAiApiResponse resp = restTemplate.postForObject(openAiUrl, req, OpenAiApiResponse.class);
    String content = resp.getChoices().get(0).getMessage().getContent().trim();

    content = content.replaceAll("(?s)```json\\s*(\\{.*?\\})\\s*```", "$1");
    content = content.replace("```json", "").replace("```", "").trim();

    try {

      return objectMapper.readValue(content, new TypeReference<Map<String,String>>(){});
    } catch (IOException e) {
      throw new RuntimeException("운동 분류 실패: " + content, e);
    }
  }


  public String getRecommendedRecipesForSpecificMeal(List<String> recipes, DayOfWeek dayOfWeek, DietType dietType) {
    String prompt = String.join(", ", recipes) +
            "\n\nBased on ONLY these exact recipe names listed above (no modifications, no additions, no removals), and **you must use the recipe names precisely as they are provided, including all whitespace and characters**," +
            " generate a single meal recommendation for " + dayOfWeek + "'s " + dietType + "." +
            " You must use the recipe names EXACTLY as they appear above — do not change anything." +
            " The response must follow this format:\n" +
            "recipe1,recipe2" +
            "At most 2 recipes per meal. If there are 2, one must be a simple side dish like rice or kimchi." +
            " Return ONLY the raw text in that format. No explanations, greetings, or extra lines.";

    List<Message> messages = new ArrayList<>();
    messages.add(new Message("user", prompt));

    TextAnalysisOpenAiApiRequest input = new TextAnalysisOpenAiApiRequest(model, messages);
    OpenAiApiResponse openAiApiResponse = restTemplate.postForObject(openAiUrl, input, OpenAiApiResponse.class);
    String content = openAiApiResponse.getChoices().get(0).getMessage().getContent();

    log.info("✅ LLM 반환 특정 끼니 추천 결과 ({} {}):\n{}", dayOfWeek, dietType, content);
    return content;
  }
}
