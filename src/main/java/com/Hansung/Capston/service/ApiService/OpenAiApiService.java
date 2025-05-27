package com.Hansung.Capston.service.ApiService;

import com.Hansung.Capston.common.DayOfWeek;
import com.Hansung.Capston.common.DietType;
import com.Hansung.Capston.dto.Api.OpenAiApi.*;
import com.Hansung.Capston.entity.Diet.Food.FoodData;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.theokanning.openai.completion.chat.ChatCompletionRequest;
import java.util.HashMap;
import java.util.Objects;
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

    ImageAnalysisOpenAiApiRequest input = getImageAnalysisOpenAiApiRequest(
        mealImage);
    OpenAiApiResponse openAiApiResponse = restTemplate.postForObject(openAiUrl, input,
        OpenAiApiResponse.class);

    String response = Objects.requireNonNull(openAiApiResponse).getChoices().getFirst().getMessage().getContent();
    logger.info("음식 분석 llm response: {}", response);
    String trim = response.trim();

    List<String> foodArray = Arrays.stream(trim.split(",\\s*"))
        .map(s -> s.replaceAll("\\.$", "").trim()) // 맨 끝 마침표만 제거
        .collect(Collectors.toList());
    logger.info("Food Array: {}", foodArray);  // 로그 출력
    return foodArray;
  }

  private ImageAnalysisOpenAiApiRequest getImageAnalysisOpenAiApiRequest(String mealImage) {
    ImageContent imageContent = new ImageContent(mealImage);
    TextContent textContent = new TextContent("너는 오로지 음식이 뭔지만 판단하는 음식이미지 분석 ai 모델이야. "
        + "이미지를 보고 어떤 음식이 얼마나 있는지 우리한테 알려줘야해. 재료가 아닌 요리 이름으로 말해줘야해. 사족은 빼고 어떤 음식이 있는지만 알려줘. 또한 너무 포괄적으로 얘기하지는 말되 그렇다고 모르는 거 있으면 대답에 포함하지말고. 3초 이내에"
        + "형식은 음식이름:g 단위로 나타낸 양이야. 음식의 구분은 무조건 쉼표(,)로 다른 특수문자는 사용하지마. 무조건 아래 형식처럼만 답변해."
        + "ex) 김치찌개:500,김밥:100");

    List<Content> list = new ArrayList<>();
    list.add(textContent);
    list.add(imageContent);

    return new ImageAnalysisOpenAiApiRequest(model,list);
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
    OpenAiApiResponse response = restTemplate.postForObject(openAiUrl, input, OpenAiApiResponse.class);

    // 응답 처리
    if (response != null && response.getChoices() != null) {
      String content = response.getChoices().get(0).getMessage().getContent();
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

  @Autowired
  private ObjectMapper objectMapper;
  //인바디 이미지 업로드시 인바디 텍스트  분석
  public Map<String, Float> extractInbodyMetrics(String ocrText) {
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
    String content = Objects.requireNonNull(resp).getChoices().getFirst().getMessage().getContent().trim();
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
    String content = Objects.requireNonNull(resp).getChoices().getFirst().getMessage().getContent().trim();

    content = content.replaceAll("(?s)```json\\s*(\\{.*?\\})\\s*```", "$1");
    content = content.replace("```json", "").replace("```", "").trim();

    try {

      return objectMapper.readValue(content, new TypeReference<Map<String,String>>(){});
    } catch (IOException e) {
      throw new RuntimeException("운동 분류 실패: " + content, e);
    }
  }

  //음성 stt -> 운동기록으로 매핑시키게 해준다.
  public Map<String, Object> parseExercise(String transcript) {
    String systemPrompt = """
            You are an assistant that extracts exercise log details.
            From the user-provided transcript, output ONLY a JSON object with these fields:
              - exerciseName: name of the exercise (String)
              - sets: number of sets (int), if applicable
              - weight: weight in kg (double), if applicable
              - reps: number of repetitions per set (int), if applicable
              - time: duration in minutes (int), if applicable
              - distance: distance in kilometers (double), if applicable
            Do not include any extra keys or commentary.
            Example Inputs and Outputs:
              Input: "데드리프트 3세트 10kg 10회"
              Output: {"exerciseName":"데드리프트","sets":3,"weight":10.0,"reps":10}

              Input: "런닝 3km 10분"
              Output: {"exerciseName":"런닝","distance":3.0,"time":10}
            """;

    String userPrompt = "Transcript: \"" + transcript + "\"";

    List<Message> messages = Arrays.asList(
            new Message("system", systemPrompt),
            new Message("user", userPrompt)
    );

    TextAnalysisOpenAiApiRequest request =
            new TextAnalysisOpenAiApiRequest(model, messages);

    OpenAiApiResponse response = restTemplate.postForObject(
            openAiUrl, request, OpenAiApiResponse.class
    );


    String content = response.getChoices().get(0).getMessage().getContent().trim();

    content = content
            .replaceAll("(?s)^```.*?\\n", "")
            .replaceAll("```$", "")
            .trim();

    try {
      return objectMapper.readValue(
              content, new TypeReference<Map<String, Object>>() {
              }
      );
    } catch (IOException e) {
      log.error("운동 파싱 실패: {}", content, e);
      throw new RuntimeException("Failed to parse exercise JSON: " + content, e);
    }
  }

  public Map<DietType, List<String>> getMealTypeRecipeCandidates(List<String> allRecipeNamesForLLM) {
    Map<DietType, List<String>> mealTypeCandidates = new HashMap<>();

    // 아침 식사용 레시피 후보 요청
    String breakfastPrompt = String.join(", ", allRecipeNamesForLLM) +
        "\n\n위에 나열된 정확한 레시피 이름만을 기반으로 (수정, 추가, 삭제 금지), **제공된 레시피 이름을 공백과 모든 문자를 포함하여 정확히 사용해야 합니다.**" +
        " 아침 식사에 적합한 레시피 이름을 최소 14개, 최대 20개 생성해주세요." +
        " 가볍고 간단한 요리를 우선적으로 추천합니다. 각 레시피 이름은 쉼표로 구분해주세요. 만약 레시피에 밥이나 김치와 같은 간단한 반찬이 필요하다면, 메인 요리 뒤에 쉼표와 공백으로 포함시켜주세요 (예: '떡국, 밥')." +
        " 설명, 인사말, 추가 줄 없이 쉼표로 구분된 레시피 이름만 반환하세요.";
    List<String> breakfastRecipes = callOpenAiApiAndParseList(breakfastPrompt);
    mealTypeCandidates.put(DietType.breakfast, breakfastRecipes);
    log.info("✅ LLM 반환 아침 식단 후보 ({}개):\n{}", breakfastRecipes.size(), breakfastRecipes);


    // 점심 식사용 레시피 후보 요청
    String lunchPrompt = String.join(", ", allRecipeNamesForLLM) +
        "\n\n위에 나열된 정확한 레시피 이름만을 기반으로 (수정, 추가, 삭제 금지), **제공된 레시피 이름을 공백과 모든 문자를 포함하여 정확히 사용해야 합니다.**" +
        " 점심 식사에 적합한 레시피 이름을 최소 14개, 최대 20개 생성해주세요." +
        " 균형 잡힌 주 요리를 우선적으로 추천합니다. 각 레시피 이름은 쉼표로 구분해주세요. 만약 레시피에 밥이나 김치와 같은 간단한 반찬이 필요하다면, 메인 요리 뒤에 쉼표와 공백으로 포함시켜주세요 (예: '김치찌개, 밥')." +
        " 설명, 인사말, 추가 줄 없이 쉼표로 구분된 레시피 이름만 반환하세요.";
    List<String> lunchRecipes = callOpenAiApiAndParseList(lunchPrompt);
    mealTypeCandidates.put(DietType.lunch, lunchRecipes);
    log.info("✅ LLM 반환 점심 식단 후보 ({}개):\n{}", lunchRecipes.size(), lunchRecipes);

    // 저녁 식사용 레시피 후보 요청
    String dinnerPrompt = String.join(", ", allRecipeNamesForLLM) +
        "\n\n위에 나열된 정확한 레시피 이름만을 기반으로 (수정, 추가, 삭제 금지), **제공된 레시피 이름을 공백과 모든 문자를 포함하여 정확히 사용해야 합니다.**" +
        " 저녁 식사에 적합한 레시피 이름을 최소 14개, 최대 20개 생성해주세요." +
        " 약간 더 푸짐하거나 든든한 요리를 우선적으로 추천합니다. 각 레시피 이름은 쉼표로 구분해주세요. 만약 레시피에 밥이나 김치와 같은 간단한 반찬이 필요하다면, 메인 요리 뒤에 쉼표와 공백으로 포함시켜주세요 (예: '삼겹살, 밥, 쌈채소')." +
        " 설명, 인사말, 추가 줄 없이 쉼표로 구분된 레시피 이름만 반환하세요.";
    List<String> dinnerRecipes = callOpenAiApiAndParseList(dinnerPrompt);
    mealTypeCandidates.put(DietType.dinner, dinnerRecipes);
    log.info("✅ LLM 반환 저녁 식단 후보 ({}개):\n{}", dinnerRecipes.size(), dinnerRecipes);

    return mealTypeCandidates;
  }

  private List<String> callOpenAiApiAndParseList(String prompt) {
    List<Message> messages = new ArrayList<>();
    messages.add(new Message("user", prompt));

    TextAnalysisOpenAiApiRequest input = new TextAnalysisOpenAiApiRequest(model, messages);
    OpenAiApiResponse openAiApiResponse = restTemplate.postForObject(openAiUrl, input, OpenAiApiResponse.class);
    String content = openAiApiResponse.getChoices().get(0).getMessage().getContent();

    // 쉼표로 분리하여 리스트로 반환 (trim() 처리 포함)
    return Arrays.stream(content.split(","))
        .map(String::trim)
        .filter(s -> !s.isEmpty()) // 빈 문자열 제거
        .collect(Collectors.toList());
  }

  public int estimateServingGram(String foodName) {
    if(foodName.isEmpty()){
      return 0;
    }
    String prompt = String.format("""
      당신은 음식량을 정확히 추정하는 전문가입니다. 아래 음식에 대해 일반적인 성인 기준 1인분 섭취량을 **정확하게 g 단위**로 추정해주세요.
      
      - 음식 이름: %s
      - 식당에서 제공되거나 가정식 기준의 일반적인 1인분 양을 기준으로 하세요.
      - 반찬 같은 사이드 디시들은 1회 제공량을 기준으로 하세요.
      - 재료가 아닌 요리 완성품 전체 기준으로 추정하세요.
      - 숫자만 반환하세요. 단위(g), 설명, 마침표 등은 절대 포함하지 마세요.
      - 예시: 250
      """, foodName);


    List<Message> messages = new ArrayList<>();
    messages.add(new Message("user", prompt));

    TextAnalysisOpenAiApiRequest request = new TextAnalysisOpenAiApiRequest(model, messages);

    OpenAiApiResponse response = restTemplate.postForObject(
        openAiUrl, request, OpenAiApiResponse.class
    );

    if (response != null && response.getChoices() != null) {
      String content = response.getChoices().get(0).getMessage().getContent().trim();
      log.info(foodName +"의 1인분 양 : "+content);
      return Integer.parseInt(content);
    } else {
      return 100;
    }
  }

}
