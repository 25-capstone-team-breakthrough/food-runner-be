CREATE DATABASE foodrunner;
USE foodrunner;

-- ✅ 사용자 테이블 최적화
CREATE TABLE User (
    user_id CHAR(36) NOT NULL, -- UUID 자동 생성
    id VARCHAR(15) NOT NULL,  -- 아이디
    name VARCHAR(15) NOT NULL, -- 이름
    password VARCHAR(255) NOT NULL, -- 암호화된 비밀번호
    role VARCHAR(20) NOT NULL DEFAULT 'ROLE_USER',
    PRIMARY KEY (user_id)
);
--BMI 테이블
CREATE TABLE BMI (
    user_id CHAR(36) NOT NULL, -- User 테이블과 연결
    age INT, -- 나이
    gender VARCHAR(10), -- 성별
    height FLOAT, -- 키
    weight FLOAT, -- 몸무게
    PRIMARY KEY (user_id),
    FOREIGN KEY (user_id) REFERENCES User(user_id) ON DELETE CASCADE
);

-- 인바디 테이블 Inbody
CREATE TABLE Inbody (
inbody_id               INT AUTO_INCREMENT NOT NULL,  -- 인바디 고유 ID (기본 키)
user_id                 VARCHAR(15)       NOT NULL,   -- 사용자 ID (User 테이블의 FK)
body_water              FLOAT            NOT NULL,    -- 체수분
protein                 FLOAT            NOT NULL,    -- 단백질
minerals                FLOAT            NOT NULL,    -- 무기질
body_fat_amount         FLOAT            NOT NULL,    -- 체지방량
skeletal_muscle_mass    FLOAT            NOT NULL,    -- 골격근량
BMI                     FLOAT            NOT NULL,    -- BMI
body_fat_percentage     FLOAT            NOT NULL,    -- 체지방률
segmental_lean_analysis FLOAT            NOT NULL,    -- 부위별 근육 분석(실제로는 별도 테이블 고려 가능)
segmental_fat_analysis  FLOAT            NOT NULL,    -- 부위별 체지방 분획(실제로는 별도 테이블 고려 가능)
created_at              DATETIME         NOT NULL,    -- 데이터 생성 시간
PRIMARY KEY (inbody_id),
FOREIGN KEY (user_id) REFERENCES User(user_id)
);

-- 인바디 이미지 테이블 Inbody_Image
CREATE TABLE Inbody_Image (
picture_id  INT AUTO_INCREMENT NOT NULL, -- 인바디 사진 고유 ID (기본 키)
inbody_id   INT               NOT NULL,  -- 인바디 고유 ID (Inbody 테이블의 FK)
user_id     VARCHAR(15)       NOT NULL,  -- 사용자 ID (User 테이블의 FK)
file_path   VARCHAR(255)      NOT NULL,  -- 사진 파일 경로
created_at  DATETIME          NOT NULL,  -- 사진 업로드 시간
PRIMARY KEY (picture_id),
FOREIGN KEY (inbody_id) REFERENCES Inbody(inbody_id),
FOREIGN KEY (user_id)   REFERENCES User(user_id)
);



-- 운동 즐겨찾기 테이블 Exercise_save
CREATE TABLE Exercise_save(
    exercise_save INT AUTO_INCREMENT NOT NULL,  -- 즐겨찾기 고유 ID (PK)
    user_id               VARCHAR(36)       NOT NULL,   -- 사용자 ID (User 테이블 FK)
    exercise_id           INT               NOT NULL,   -- 운동 ID ()
	PRIMARY KEY (exercise_save),
    UNIQUE KEY user_exercise_unique (user_id, exercise_id),
    FOREIGN KEY (user_id) REFERENCES User(user_id) ON DELETE CASCADE
);

-- 운동 기록 테이블  Exercise_Log**
CREATE TABLE Exercise_Log (
    exercise_log_id INT AUTO_INCREMENT NOT NULL,        -- 운동 기록 고유 ID (PK)
    user_id         VARCHAR(15)       NOT NULL,         -- 사용자 ID (User 테이블 FK)
    exercise_id     INT               NOT NULL,         -- 운동 ID (Exercise_Data 테이블 FK)
    cal_burned      DECIMAL(10,1)     NOT NULL,                               -- 소모 칼로리
    created_at      DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (exercise_log_id),
    FOREIGN KEY (user_id)     REFERENCES User(user_id),
    FOREIGN KEY (exercise_id) REFERENCES Exercise_Data(exercise_id)
);


-- ✅ 영양제 데이터 테이블
CREATE TABLE Supplement_Data (
    supplement_id INT AUTO_INCREMENT PRIMARY KEY,
    company VARCHAR(255) NOT NULL,
    supplement_name VARCHAR(255) NOT NULL,
    distribution_period VARCHAR(50) NOT NULL,
    usage_method TEXT NOT NULL,
    preservation_period VARCHAR(50) NOT NULL,
    intake_information TEXT NOT NULL,
    main_function TEXT NOT NULL,
    base_standard TEXT NOT NULL
);

-- ✅ 영양제 기록 테이블
CREATE TABLE Supplement_Log (
    supplement_log_id INT AUTO_INCREMENT PRIMARY KEY,
    user_id VARCHAR(50) NOT NULL,
    supplement_id INT NOT NULL,
    type ENUM('search', 'image', '사용') NOT NULL,
    calories DECIMAL(10,2) DEFAULT 0,
    protein DECIMAL(10,2) DEFAULT 0,
    carbohydrate DECIMAL(10,2) DEFAULT 0,
    fat DECIMAL(10,2) DEFAULT 0,
    sugar DECIMAL(10,2) DEFAULT 0,
    sodium DECIMAL(10,2) DEFAULT 0,
    dietary_fiber DECIMAL(10,2) DEFAULT 0,
    date DATETIME NOT NULL,
    FOREIGN KEY (user_id) REFERENCES User(user_id),
    FOREIGN KEY (supplement_id) REFERENCES Supplement_Data(supplement_id)
);

-- ✅ 식재료 데이터 테이블
CREATE TABLE Ingredient_Data (
    ingredient_id INT AUTO_INCREMENT PRIMARY KEY,
    ingredient_name VARCHAR(255) NOT NULL,
    ingredient_nutrition TEXT NOT NULL,
    ingredient_cal DECIMAL(10,2) NOT NULL,
    ingredient_image VARCHAR(255)
);

-- ✅ 선호 식재료 테이블
CREATE TABLE Preferred_Ingredient (
    preingredient_id INT AUTO_INCREMENT PRIMARY KEY,
    ingredient_id INT NOT NULL,
    user_id VARCHAR(50) NOT NULL,
    FOREIGN KEY (ingredient_id) REFERENCES Ingredient_Data(ingredient_id),
    FOREIGN KEY (user_id) REFERENCES User(user_id)
);

-- ✅ 추천 식재료 테이블
CREATE TABLE Recommended_Ingredient (
    recommendation_ingredient_id INT AUTO_INCREMENT PRIMARY KEY,
    ingredient_id INT NOT NULL,
    user_id VARCHAR(50) NOT NULL,
    FOREIGN KEY (ingredient_id) REFERENCES Ingredient_Data(ingredient_id),
    FOREIGN KEY (user_id) REFERENCES User(user_id)
);

-- ✅ 레시피 데이터 테이블
CREATE TABLE Recipe_Data (
    recipe_id INT AUTO_INCREMENT PRIMARY KEY,
    recipe_name VARCHAR(255) NOT NULL,
    recipe_image VARCHAR(255) NOT NULL,
    ingredient TEXT NOT NULL,
    serving INT NOT NULL,
    recipe TEXT NOT NULL,
    recommend_count INT NOT NULL DEFAULT 0,
    related_recipe_1 INT NOT NULL,
    related_recipe_2 INT NOT NULL,
    related_recipe_3 INT NOT NULL,
    calories FLOAT NULL,
    protein FLOAT NULL,
    carbohydrate FLOAT NULL,
    fat FLOAT NULL,
    sugar FLOAT NULL,
    sodium FLOAT NULL,
    dietary_fiber FLOAT NULL,
    FOREIGN KEY (related_recipe_1) REFERENCES Recipe_Data(recipe_id),
    FOREIGN KEY (related_recipe_2) REFERENCES Recipe_Data(recipe_id),
    FOREIGN KEY (related_recipe_3) REFERENCES Recipe_Data(recipe_id)
);


-- ✅ 추천 식단 테이블
CREATE TABLE Recommended_Diet (
    recommended_diet_id INT AUTO_INCREMENT PRIMARY KEY,
    user_id VARCHAR(50) NOT NULL,
    recipe_id INT NOT NULL,
    type ENUM('breakfast', 'lunch', 'dinner') NOT NULL,
    date DATE NOT NULL,
    FOREIGN KEY (user_id) REFERENCES User(user_id),
    FOREIGN KEY (recipe_id) REFERENCES Recipe_Data(recipe_id)
);

-- ✅ 식사 기록 테이블
CREATE TABLE Meal_Log (
    meal_id INT AUTO_INCREMENT PRIMARY KEY,
    user_id VARCHAR(50) NOT NULL,
    type ENUM('search', 'image') NOT NULL,
    calories DECIMAL(10,2) DEFAULT 0,
    protein DECIMAL(10,2) DEFAULT 0,
    carbohydrate DECIMAL(10,2) DEFAULT 0,
    fat DECIMAL(10,2) DEFAULT 0,
    sugar DECIMAL(10,2) DEFAULT 0,
    sodium DECIMAL(10,2) DEFAULT 0,
    dietary_fiber DECIMAL(10,2) DEFAULT 0,
    calcium DECIMAL(10,2) DEFAULT 0,
    saturated_fat DECIMAL(10,2) DEFAULT 0,
    trans_fat DECIMAL(10,2)DEFAULT 0,
    cholesterol DECIMAL(10,2) DEFAULT 0,
    vitamin_a DECIMAL(10,2) DEFAULT 0,
    vitamin_b1 DECIMAL(10,2) DEFAULT 0,
    vitamin_c DECIMAL(10,2) DEFAULT 0,
    vitamin_d DECIMAL(10,2) DEFAULT 0,
    vitamin_e DECIMAL(10,2) DEFAULT 0,
    magnesium DECIMAL(10,2) DEFAULT 0,
    zinc DECIMAL(10,2) DEFAULT 0,
    lactium DECIMAL(10,2) DEFAULT 0,
    potassium DECIMAL(10,2) DEFAULT 0,
    l_arginine DECIMAL(10,2) DEFAULT 0,
    omega3 DECIMAL(10,2) DEFAULT 0,
    date DATETIME NOT NULL,
    FOREIGN KEY (user_id) REFERENCES User(user_id)
);

-- ✅ 사진 식사 기록 테이블
CREATE TABLE Image_Meal_Log (
    image_meal_log_id INT AUTO_INCREMENT PRIMARY KEY,
    user_id VARCHAR(50) NOT NULL,
    meal_id INT NOT NULL,
    meal_name VARCHAR(255) NOT NULL,
    meal_image LONGBLOB NOT NULL,
    FOREIGN KEY (user_id) REFERENCES User(user_id),
    FOREIGN KEY (meal_id) REFERENCES Meal_Log(meal_id)
);


-- ✅ 요리 데이터 테이블
CREATE TABLE Food_Data (
                           food_id INT AUTO_INCREMENT PRIMARY KEY,
                           food_image VARCHAR(255),
                           food_name VARCHAR(255) not null,
                           food_company VARCHAR(255),
                           calories DECIMAL(10,2) DEFAULT 0,
                           protein DECIMAL(10,2) DEFAULT 0,
                           carbohydrate DECIMAL(10,2) DEFAULT 0,
                           fat DECIMAL(10,2) DEFAULT 0,
                           sugar DECIMAL(10,2) DEFAULT 0,
                           sodium DECIMAL(10,2) DEFAULT 0,
                           dietary_fiber DECIMAL(10,2) DEFAULT 0,
                           calcium DECIMAL(10,2) DEFAULT 0,
                           saturated_fat DECIMAL(10,2) DEFAULT 0,
                           trans_fat DECIMAL(10,2)DEFAULT 0,
                           cholesterol DECIMAL(10,2) DEFAULT 0,
                           vitamin_a DECIMAL(10,2) DEFAULT 0,
                           vitamin_b1 DECIMAL(10,2) DEFAULT 0,
                           vitamin_c DECIMAL(10,2) DEFAULT 0,
                           vitamin_d DECIMAL(10,2) DEFAULT 0,
                           vitamin_e DECIMAL(10,2) DEFAULT 0,
                           magnesium DECIMAL(10,2) DEFAULT 0,
                           zinc DECIMAL(10,2) DEFAULT 0,
                           lactium DECIMAL(10,2) DEFAULT 0,
                           potassium DECIMAL(10,2) DEFAULT 0,
                           l_arginine DECIMAL(10,2) DEFAULT 0,
                           omega3 DECIMAL(10,2) DEFAULT 0
);


-- ✅ 검색 식사 기록 테이블
CREATE TABLE Search_Meal_Log (
    search_meal_log_id INT AUTO_INCREMENT PRIMARY KEY,
	user_id VARCHAR(50) NOT NULL,
    meal_id INT NOT NULL,
    food_id INT NOT NULL,
    FOREIGN KEY (user_id) REFERENCES User(user_id),
    FOREIGN KEY (meal_id) REFERENCES Meal_Log(meal_id),
	FOREIGN KEY (food_id) REFERENCES Food_Data(food_id)
);

-- ✅ 영양소 기록 테이블
CREATE TABLE Nutrition_Log (
    nutrition_log_id INT AUTO_INCREMENT PRIMARY KEY,
    user_id VARCHAR(50) NOT NULL,
    calories DECIMAL(10,2) DEFAULT 0,
    protein DECIMAL(10,2) DEFAULT 0,
    carbohydrate DECIMAL(10,2) DEFAULT 0,
    fat DECIMAL(10,2) DEFAULT 0,
    sugar DECIMAL(10,2) DEFAULT 0,
    sodium DECIMAL(10,2) DEFAULT 0,
    dietary_fiber DECIMAL(10,2) DEFAULT 0,
    calcium DECIMAL(10,2) DEFAULT 0,
    saturated_fat DECIMAL(10,2) DEFAULT 0,
    trans_fat DECIMAL(10,2)DEFAULT 0,
    cholesterol DECIMAL(10,2) DEFAULT 0,
    vitamin_a DECIMAL(10,2) DEFAULT 0,
    vitamin_b1 DECIMAL(10,2) DEFAULT 0,
    vitamin_c DECIMAL(10,2) DEFAULT 0,
    vitamin_d DECIMAL(10,2) DEFAULT 0,
    vitamin_e DECIMAL(10,2) DEFAULT 0,
    magnesium DECIMAL(10,2) DEFAULT 0,
    zinc DECIMAL(10,2) DEFAULT 0,
    lactium DECIMAL(10,2) DEFAULT 0,
    potassium DECIMAL(10,2) DEFAULT 0,
    l_arginine DECIMAL(10,2) DEFAULT 0,
    omega3 DECIMAL(10,2) DEFAULT 0,
    date DATETIME NOT NULL,
    FOREIGN KEY (user_id) REFERENCES User(user_id)
);


-- ✅ 권장 영양소 테이블
CREATE TABLE Recommended_Nutrition (
    recommended_nutrition_id INT AUTO_INCREMENT PRIMARY KEY,
    user_id VARCHAR(50) NOT NULL,
    inbody_info BOOLEAN DEFAULT FALSE,
        calories DECIMAL(10,2) DEFAULT 0,
    protein DECIMAL(10,2) DEFAULT 0,
    carbohydrate DECIMAL(10,2) DEFAULT 0,
    fat DECIMAL(10,2) DEFAULT 0,
    sugar DECIMAL(10,2) DEFAULT 0,
    sodium DECIMAL(10,2) DEFAULT 0,
    dietary_fiber DECIMAL(10,2) DEFAULT 0,
    calcium DECIMAL(10,2) DEFAULT 0,
    saturated_fat DECIMAL(10,2) DEFAULT 0,
    trans_fat DECIMAL(10,2)DEFAULT 0,
    cholesterol DECIMAL(10,2) DEFAULT 0,
    vitamin_a DECIMAL(10,2) DEFAULT 0,
    vitamin_b1 DECIMAL(10,2) DEFAULT 0,
    vitamin_c DECIMAL(10,2) DEFAULT 0,
    vitamin_d DECIMAL(10,2) DEFAULT 0,
    vitamin_e DECIMAL(10,2) DEFAULT 0,
    magnesium DECIMAL(10,2) DEFAULT 0,
    zinc DECIMAL(10,2) DEFAULT 0,
    lactium DECIMAL(10,2) DEFAULT 0,
    potassium DECIMAL(10,2) DEFAULT 0,
    l_arginine DECIMAL(10,2) DEFAULT 0,
    omega3 DECIMAL(10,2) DEFAULT 0,
    FOREIGN KEY (user_id) REFERENCES User(user_id)
);

CREATE TABLE Preferred_Food (
    prefood_id INT AUTO_INCREMENT PRIMARY KEY,
    food_id INT NOT NULL,
    user_id VARCHAR(50) NOT NULL,
    FOREIGN KEY (food_id) REFERENCES Food_Data(food_id),
    FOREIGN KEY (user_id) REFERENCES User(user_id)
);

CREATE TABLE Preferred_Supplement (
    presupplement_id INT AUTO_INCREMENT PRIMARY KEY,
    supplement_id INT NOT NULL,
    user_id VARCHAR(50) NOT NULL,
    FOREIGN KEY (supplement_id) REFERENCES Supplement_Data(supplement_id),
    FOREIGN KEY (user_id) REFERENCES User(user_id)
);



-- ✅ 성능 최적화를 위한 인덱스 추가
CREATE INDEX idx_user_id ON User(user_id);
CREATE INDEX idx_recipe_id ON Recipe_Data(recipe_id);
CREATE INDEX idx_ingredient_id ON Ingredient_Data(ingredient_id);
CREATE INDEX idx_meal_id ON Meal_Log(meal_id);
