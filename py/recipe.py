import requests, json
from bs4 import BeautifulSoup
import pandas as pd
import time
import chardet

# 파일 경로에 대한 경고 해결을 위해 raw string을 사용
file_path = r"c:/work/recipe_test.csv"

# 파일 인코딩 감지
with open(file_path, 'rb') as f:
    result = chardet.detect(f.read())  # 파일 인코딩 감지
    encoding = result['encoding']

# 데이터 로드 (CSV에서 RCP_SNO 컬럼을 사용)
df = pd.read_csv(file_path, encoding=encoding)
df["instructions"] = ""  # 새로운 컬럼 'instructions' 추가

# 레시피 찾는 함수
def food_info(food_id):
    '''
    This function gives you food information for the given input.

    PARAMETERS
        - food_id(str): ID of the food to look for (from URL)
    RETURN
        - res(dict): dictionary containing food info
            - res['ingredients'](str): ingredients to make the food
            - res['recipe'](list[str]): contain recipe in order
    '''
    new_url = f'https://www.10000recipe.com/recipe/{food_id}'
    new_response = requests.get(new_url)
    if new_response.status_code == 200:
        html = new_response.text
        soup = BeautifulSoup(html, 'html.parser')
    else : 
        print("HTTP response error :", new_response.status_code)
        return

    food_info = soup.find(attrs={'type':'application/ld+json'})
    result = json.loads(food_info.text)
    ingredient = ','.join(result['recipeIngredient'])
    recipe = [result['recipeInstructions'][i]['text'] for i in range(len(result['recipeInstructions']))]
    for i in range(len(recipe)):
        recipe[i] = f'{i+1}. ' + recipe[i]
    
    res = {
        'ingredients': ingredient,
        'recipe': recipe
    }

    return res

# 각 레시피 페이지에 대해 조리 방법 추출
for index, row in df.iterrows():
    recipe_id = row["RCP_SNO"]

    print(f"{index+1}번째: '{recipe_id}' 레시피 검색 중...")

    try:
        # food_info 함수 호출하여 조리 방법 및 재료 추출
        res = food_info(recipe_id)
        
        if res:
            # 추출된 조리 방법을 DataFrame에 추가
            df.at[index, "instructions"] = "\n".join(res['recipe'])  # 레시피 단계를 줄바꿈으로 연결
            print(f" → 조리 방법: {res['recipe']}")
        else:
            print(f"❌ {recipe_id} 조리 방법 추출 실패")
            df.at[index, "instructions"] = None  # 추출 실패 시 None으로 설정
        
    except Exception as e:
        print(f"❌ {recipe_id} 조리 방법 추출 실패: {e}")
        df.at[index, "instructions"] = None  # 실패 시 None

    time.sleep(1)  # 웹사이트에 과도한 요청을 보내지 않도록 1초 대기

# 결과를 새로운 CSV 파일로 저장
df.to_csv("c:/work/recipe정제.csv", index=False, encoding="utf-8-sig")
print("✅ 저장 완료: recipe정제.csv")
