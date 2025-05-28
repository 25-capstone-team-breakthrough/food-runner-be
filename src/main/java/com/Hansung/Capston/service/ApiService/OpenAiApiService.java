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

  public List<String> mealImageAnalysis(String mealImage) {
    ImageAnalysisOpenAiApiRequest input = getImageAnalysisOpenAiApiRequest(mealImage);
    OpenAiApiResponse openAiApiResponse = restTemplate.postForObject(openAiUrl, input, OpenAiApiResponse.class);

    String response = Objects.requireNonNull(openAiApiResponse).getChoices().getFirst().getMessage().getContent();
    logger.info("음식 분석 llm response: {}", response);
    String trim = response.trim();

    if (trim.equalsIgnoreCase("NONE:null")) {
      return null;
    }

    List<String> foodArray = Arrays.stream(trim.split(",\\s*"))
        .map(s -> s.replaceAll("\\.$", "").trim())
        .collect(Collectors.toList());

    logger.info("Food Array: {}", foodArray);
    return foodArray;
  }

  private ImageAnalysisOpenAiApiRequest getImageAnalysisOpenAiApiRequest(String mealImage) {
    ImageContent imageContent = new ImageContent(mealImage);
    TextContent textContent = new TextContent(
        "너는 오로지 음식이 뭔지만 판단하는 음식이미지 분석 ai 모델이야. "
            + "이미지를 보고 어떤 음식이 얼마나 있는지 우리한테 알려줘야 해. 재료가 아닌 요리 이름으로 말해줘야 해. "
            + "사족은 빼고 어떤 음식이 있는지만 알려줘. 또한 너무 포괄적으로 얘기하지는 말고, 모르면 대답에 포함하지 마. "
            + "반드시 '음식이름:그램' 형식으로만 답하고, 음식 간 구분은 무조건 쉼표(,)만 사용해. "
            + "만약 아무 음식도 식별되지 않으면 정확하게 다음처럼 대답해: NONE:null"
            + "예시: 김치찌개:500, 김밥:100"
    );


    List<Content> list = new ArrayList<>();
    list.add(textContent);
    list.add(imageContent);

    return new ImageAnalysisOpenAiApiRequest(model,list);
  }

  public FoodData getNutrientInfo(String food) {
    log.info("음식 분석 시작: {}", food);

    // 텍스트 기반 요청 내용
    String prompt = "너는 전문 영양소 분석가이자 식품 데이터 전문가야. 사용자가 입력한 음식에 대해 100g 기준의 영양 정보를 추정해서 제공해야 해. "
        + "일반적인 식사류뿐만 아니라 초콜릿, 케이크, 도넛, 쿠키, 아이스크림 같은 디저트류나 커피, 콜라 같은 음료도 정확하게 다룰 수 있어야 해."
        + "\n\n🧾 응답 형식은 반드시 아래 예시처럼 `key=value` 형태로 출력하고, 항목 간 쉼표(,)로만 구분해야 해. 다른 텍스트나 설명은 절대 넣지 마."
        + "\n\n📌 예시 (100g 기준):\n"
        + "foodName=초콜릿, calories=550, protein=6.8, carbohydrate=60, fat=35, sugar=45, sodium=25, dietaryFiber=3, calcium=20, saturatedFat=22, transFat=0.5, cholesterol=5, vitaminA=null, vitaminB1=0.02, vitaminC=null, vitaminD=null, vitaminE=0.4, magnesium=75, zinc=1.5, lactium=null, potassium=290, lArginine=null, omega3=null"
        + "\n\n❗️주의사항:\n- 반드시 100g 기준이어야 하며, 1인분 기준으로 추정하지 말 것\n- 값이 없으면 `null`로 작성\n- 응답은 한 줄로만 작성"
        + "\n\n사용자 입력 음식: " + food;



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
      당신은 전문적인 식품 영양사이자 음식량 추정 전문가입니다. 아래 조건을 정확히 이해하고 따르십시오.
      
      ==== 목표 ====
      음식 이름을 보고, 일반적인 성인 기준으로 1인분 섭취량을 **정확히 g 단위의 숫자 하나**로 추정합니다.
      
      ==== 지켜야 할 규칙 ====
      1. 반드시 요리가 완성된 상태의 전체량을 기준으로 추정합니다.
         - 예: 김치찌개 → 국물 포함된 완성된 김치찌개 한 그릇
         - 예: 비빔밥 → 고기/채소/밥 포함 전체
      2. 식당 혹은 가정식 기준의 보통 성인을 위한 일반적인 양을 기준으로 합니다.
         - 어린이, 체중 감량 식단, 운동식단은 고려하지 마십시오.
      3. 반드시 숫자만 출력하십시오. 단위(g), 설명, 쉼표, 마침표, 공백도 모두 포함하지 마십시오.
         - 예: 출력 → `250`
         - 잘못된 출력 → `250g`, `약 250`, `250 그램`, `250.`
      4. 숫자는 대략적인 평균이더라도 반드시 구체적인 정수로 출력합니다. 소수점은 포함하지 마십시오.
      5. 잘 모르겠더라도 반드시 일반적인 평균 기준을 추정해서 정수 하나를 출력하십시오.
      
      ==== 음식 이름 ====
      %s
      
      ==== 출력 형식 ====
      반드시 아래 예시처럼 **숫자 하나만** 출력하세요:
      예시: 180
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
