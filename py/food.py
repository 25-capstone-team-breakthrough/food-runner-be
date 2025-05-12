from selenium import webdriver
from selenium.webdriver.chrome.options import Options
from selenium.webdriver.common.by import By
from selenium.webdriver.support.ui import WebDriverWait
from selenium.webdriver.support import expected_conditions as EC
import pandas as pd
import time

# 데이터 로드
df = pd.read_csv("c:/work/농수산물 정제.csv", encoding="cp949")
df["image_url"] = ""

# 브라우저 옵션
options = Options()
# options.add_argument("--headless")  # 테스트 시 보이게
driver = webdriver.Chrome(options=options)

for index, row in df.iterrows():
    food_name = row["식품명"]
    search_url = f"https://kr.freepik.com/search?format=search&query={food_name}&type=photo"

    print(f"{index+1}번째: '{food_name}' 이미지 검색 중...")
    driver.get(search_url)

    try:
        # 명시적 대기: 이미지 요소 로드될 때까지 최대 5초 대기
        img_element = WebDriverWait(driver, 5).until(
            EC.presence_of_element_located((By.CSS_SELECTOR, "figure img"))
        )

        # src 또는 data-src 중 하나 추출
        img_url = img_element.get_attribute("src")
        if not img_url or img_url.startswith("data:"):
            img_url = img_element.get_attribute("data-src")

        df.at[index, "image_url"] = img_url
        print(f" → 이미지 URL: {img_url}")
    except Exception as e:
        print(f"❌ {food_name} 이미지 없음: {e}")
        df.at[index, "image_url"] = None

    time.sleep(1)

driver.quit()

# 결과 저장
df.to_csv("c:/work/농수산물 정제url.csv", index=False, encoding="utf-8-sig")
print("✅ 저장 완료: seafood_with_images.csv")
