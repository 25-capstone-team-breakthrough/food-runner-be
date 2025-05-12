CREATE DATABASE foodrunner;
USE foodrunner;

-- ✅ 사용자 테이블 최적화
CREATE TABLE user (
                      user_id CHAR(36) NOT NULL, -- UUID 자동 생성
                      id VARCHAR(15) NOT NULL,  -- 아이디
                      name VARCHAR(15) NOT NULL, -- 이름
                      password VARCHAR(255) NOT NULL, -- 암호화된 비밀번호
                      role VARCHAR(20) NOT NULL DEFAULT 'ROLE_USER',
                      PRIMARY KEY (user_id)
);
-- BMI 테이블
CREATE TABLE bmi (
                     user_id CHAR(36) NOT NULL, -- User 테이블과 연결
                     age INT, -- 나이
                     gender VARCHAR(10), -- 성별
                     height FLOAT, -- 키
                     weight FLOAT, -- 몸무게
                     PRIMARY KEY (user_id),
                     FOREIGN KEY (user_id) REFERENCES user(user_id) ON DELETE CASCADE
);

-- 인바디 테이블 Inbody
CREATE TABLE inbody (
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
                        FOREIGN KEY (user_id) REFERENCES user(user_id)
);

-- 인바디 이미지 테이블 Inbody_Image
CREATE TABLE inbody_image (
                              picture_id  INT AUTO_INCREMENT NOT NULL, -- 인바디 사진 고유 ID (기본 키)
                              inbody_id   INT               NOT NULL,  -- 인바디 고유 ID (Inbody 테이블의 FK)
                              user_id     VARCHAR(15)       NOT NULL,  -- 사용자 ID (User 테이블의 FK)
                              file_path   VARCHAR(255)      NOT NULL,  -- 사진 파일 경로
                              created_at  DATETIME          NOT NULL,  -- 사진 업로드 시간
                              PRIMARY KEY (picture_id),
                              FOREIGN KEY (inbody_id) REFERENCES inbody(inbody_id),
                              FOREIGN KEY (user_id)   REFERENCES user(user_id)
);



-- 운동 즐겨찾기 테이블 Exercise_save
CREATE TABLE exercise_save(
                              exercise_save INT AUTO_INCREMENT NOT NULL,  -- 즐겨찾기 고유 ID (PK)
                              user_id               VARCHAR(36)       NOT NULL,   -- 사용자 ID (User 테이블 FK)
                              exercise_id           INT               NOT NULL,   -- 운동 ID ()
                              PRIMARY KEY (exercise_save),
                              UNIQUE KEY user_exercise_unique (user_id, exercise_id),
                              FOREIGN KEY (user_id) REFERENCES user(user_id) ON DELETE CASCADE
);




-- ✅ 영양제 데이터 테이블
CREATE TABLE supplement_data (
                                 sup_id INT AUTO_INCREMENT PRIMARY KEY,
                                 sup_name VARCHAR(255),
                                 usage_method TEXT,
                                 preservation_period VARCHAR(50),
                                 intake_information TEXT,
                                 main_function TEXT,
                                 sup_company VARCHAR(255),
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
                                 sup_image TEXT
);

-- ✅ 영양제 기록 테이블
CREATE TABLE supplement_log (
                                supplement_log_id INT AUTO_INCREMENT PRIMARY KEY,
                                user_id VARCHAR(50) NOT NULL,
                                sup_id INT NOT NULL,
                                date DATETIME NOT NULL,
                                FOREIGN KEY (user_id) REFERENCES user(user_id),
                                FOREIGN KEY (sup_id) REFERENCES supplement_data(sup_id)
);

-- ✅ 식재료 데이터 테이블
CREATE TABLE ingredient_data (
                                 ingredient_id INT AUTO_INCREMENT PRIMARY KEY,
                                 ingredient_name VARCHAR(255) NOT NULL,
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
                                 ash DECIMAL(10, 2) DEFAULT 0,
                                 ingredient_image TEXT
);

-- ✅ 선호 식재료 테이블
CREATE TABLE preferred_ingredient (
                                      preingredient_id INT AUTO_INCREMENT PRIMARY KEY,
                                      ingredient_id INT NOT NULL,
                                      user_id VARCHAR(50) NOT NULL,
                                      FOREIGN KEY (ingredient_id) REFERENCES ingredient_data(ingredient_id),
                                      FOREIGN KEY (user_id) REFERENCES user(user_id)
);

-- ✅ 추천 식재료 테이블
CREATE TABLE recommended_ingredient (
                                        recommendation_ingredient_id INT AUTO_INCREMENT PRIMARY KEY,
                                        ingredient_id INT NOT NULL,
                                        user_id VARCHAR(50) NOT NULL,
                                        FOREIGN KEY (ingredient_id) REFERENCES ingredient_data(ingredient_id),
                                        FOREIGN KEY (user_id) REFERENCES user(user_id)
);

-- ✅ 레시피 데이터 테이블
CREATE TABLE recipe_data (
                             recipe_id BIGINT PRIMARY KEY,
                             recipe_name VARCHAR(255),
                             recipe_image VARCHAR(255),
                             ingredients TEXT,
                             serving VARCHAR(25),
                             recipe TEXT,
                             recommend_count INT,
                             related_recipe_1 BIGINT,
                             related_recipe_2 bigint,
                             related_recipe_3 BigINT,
                             calories DECIMAL(10,2) DEFAULT 0,
                             protein DECIMAL(10,2) DEFAULT 0,
                             carbohydrate DECIMAL(10,2) DEFAULT 0,
                             fat DECIMAL(10,2) DEFAULT 0,
                             FOREIGN KEY (related_recipe_1) REFERENCES recipe_data(recipe_id),
                             FOREIGN KEY (related_recipe_2) REFERENCES recipe_data(recipe_id),
                             FOREIGN KEY (related_recipe_3) REFERENCES recipe_data(recipe_id)
);


-- ✅ 추천 식단 테이블
CREATE TABLE recommended_recipe (
                                  recommended_recipe_id INT AUTO_INCREMENT PRIMARY KEY,
                                  user_id VARCHAR(50) NOT NULL,
                                  recipe_id bigint NOT NULL,
                                  type ENUM('breakfast', 'lunch', 'dinner') NOT NULL,
                                  date DATE NOT NULL,
                                  FOREIGN KEY (user_id) REFERENCES user(user_id),
                                  FOREIGN KEY (recipe_id) REFERENCES recipe_data(recipe_id)
);

-- ✅ 식사 기록 테이블
CREATE TABLE meal_log (
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
                          FOREIGN KEY (user_id) REFERENCES user(user_id)
);

-- ✅ 사진 식사 기록 테이블
CREATE TABLE image_meal_log (
                                image_meal_log_id INT AUTO_INCREMENT PRIMARY KEY,
                                meal_id INT NOT NULL,
                                meal_name VARCHAR(255) NOT NULL,
                                meal_image TEXT NOT NULL,
                                FOREIGN KEY (meal_id) REFERENCES meal_log(meal_id)
);


-- ✅ 요리 데이터 테이블
CREATE TABLE food_data (
                           food_id INT AUTO_INCREMENT PRIMARY KEY,
                           food_image TEXT,
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
CREATE TABLE search_meal_log (
                                 search_meal_log_id INT AUTO_INCREMENT PRIMARY KEY,
                                 meal_id INT NOT NULL,
                                 food_id INT NOT NULL,
                                 food_name CHAR(50),
                                 food_image TEXT NOT NULL,
                                 FOREIGN KEY (meal_id) REFERENCES meal_log(meal_id)
);

-- ✅ 영양소 기록 테이블
CREATE TABLE nutrition_log (
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
                               FOREIGN KEY (user_id) REFERENCES user(user_id)
);


-- ✅ 권장 영양소 테이블
CREATE TABLE recommended_nutrition (
                                       recommended_nutrition_id INT AUTO_INCREMENT PRIMARY KEY,
                                       user_id VARCHAR(50) NOT NULL,
                                       nutrition_type VARCHAR(20) NOT NULL,  -- ← enum NutritionType 저장 (MIN, MAX)

                                       inbody_info BOOLEAN DEFAULT FALSE,
                                       calories DECIMAL(10,2) DEFAULT 0,
                                       protein DECIMAL(10,2) DEFAULT 0,
                                       carbohydrate DECIMAL(10,2) DEFAULT 0,
                                       fat DECIMAL(10,2) DEFAULT 0,
                                       sugar DECIMAL(10,2) DEFAULT 0,
                                       saturated_fat DECIMAL(10,2) DEFAULT 0,
                                       trans_fat DECIMAL(10,2) DEFAULT 0,
                                       cholesterol DECIMAL(10,2) DEFAULT 0,
                                       sodium DECIMAL(10,2) DEFAULT 0,
                                       dietary_fiber DECIMAL(10,2) DEFAULT 0,
                                       calcium DECIMAL(10,2) DEFAULT 0,
                                       vitamin_a DECIMAL(10,2) DEFAULT 0,
                                       vitamin_b1 DECIMAL(10,2) DEFAULT 0,
                                       vitamin_c DECIMAL(10,2) DEFAULT 0,
                                       vitamin_d DECIMAL(10,2) DEFAULT 0,
                                       vitamin_e DECIMAL(10,2) DEFAULT 0,
                                       magnesium DECIMAL(10,2) DEFAULT 0,
                                       zinc DECIMAL(10,2) DEFAULT 0,
                                       potassium DECIMAL(10,2) DEFAULT 0,
                                       l_arginine DECIMAL(10,2) DEFAULT 0,
                                       omega3 DECIMAL(10,2) DEFAULT 0,
                                       lactium DECIMAL(10,2) DEFAULT 0,

                                       FOREIGN KEY (user_id) REFERENCES user(user_id)
);


CREATE TABLE preferred_food (
                                prefood_id INT AUTO_INCREMENT PRIMARY KEY,
                                food_id INT NOT NULL,
                                user_id VARCHAR(50) NOT NULL,
                                FOREIGN KEY (food_id) REFERENCES food_data(food_id),
                                FOREIGN KEY (user_id) REFERENCES user(user_id)
);

CREATE TABLE preferred_supplement (
                                      presupplement_id INT AUTO_INCREMENT PRIMARY KEY,
                                      sup_id INT NOT NULL,
                                      user_id VARCHAR(50) NOT NULL,
                                      FOREIGN KEY (sup_id) REFERENCES supplement_data(sup_id),
                                      FOREIGN KEY (user_id) REFERENCES user(user_id)
);



-- ✅ 성능 최적화를 위한 인덱스 추가
CREATE INDEX idx_user_id ON user(user_id);
CREATE INDEX idx_recipe_id ON recipe_data(recipe_id);
CREATE INDEX idx_ingredient_id ON ingredient_data(ingredient_id);
CREATE INDEX idx_meal_id ON meal_log(meal_id);
