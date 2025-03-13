CREATE DATABASE foodrunner;
USE foodrunner;

-- ✅ 1. 사용자 테이블 최적화
CREATE TABLE User (
    user_id VARCHAR(50) PRIMARY KEY NOT NULL,
    password VARCHAR(255) NOT NULL,
    age INT NOT NULL,
    gender ENUM('m', 'f') NOT NULL,
    height FLOAT NOT NULL,
    weight FLOAT NOT NULL
);

-- ✅ 2. 영양제 데이터 최적화
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

-- ✅ 3. 식재료 테이블 최적화
CREATE TABLE Ingredient_Data (
    ingredient_id INT AUTO_INCREMENT PRIMARY KEY,
    ingredient_name VARCHAR(255) NOT NULL,
    ingredient_nutrition TEXT NOT NULL,
    ingredient_cal DECIMAL(10,2) NOT NULL
);

CREATE TABLE Preferred_Ingredient (
    preingredient_id INT AUTO_INCREMENT PRIMARY KEY,
    ingredient_id INT NOT NULL,
    user_id VARCHAR(50) NOT NULL,
    FOREIGN KEY (ingredient_id) REFERENCES Ingredient_Data(ingredient_id),
    FOREIGN KEY (user_id) REFERENCES User(user_id)
);

-- ✅ 4. 레시피 테이블 최적화 (related_recipe 테이블 분리)
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


-- ✅ 5. 추천 식단 테이블 최적화
CREATE TABLE Recommended_Diet (
    recommended_diet_id INT AUTO_INCREMENT PRIMARY KEY,
    user_id VARCHAR(50) NOT NULL,
    recipe_id INT NOT NULL,
    type ENUM('breakfast', 'lunch', 'dinner') NOT NULL,
    date DATE NOT NULL,
    FOREIGN KEY (user_id) REFERENCES User(user_id),
    FOREIGN KEY (recipe_id) REFERENCES Recipe_Data(recipe_id)
);

-- ✅ 6. 식사 기록 테이블 최적화
CREATE TABLE Meal_Log (
    meal_id INT AUTO_INCREMENT PRIMARY KEY,
    user_id VARCHAR(50) NOT NULL,
    type ENUM('search', 'image', '식사') NOT NULL,
    calories DECIMAL(10,2) DEFAULT 0,
    protein DECIMAL(10,2) DEFAULT 0,
    carbohydrate DECIMAL(10,2) DEFAULT 0,
    fat DECIMAL(10,2) DEFAULT 0,
    sugar DECIMAL(10,2) DEFAULT 0,
    sodium DECIMAL(10,2) DEFAULT 0,
    dietary_fiber DECIMAL(10,2) DEFAULT 0,
    date DATETIME NOT NULL,
    FOREIGN KEY (user_id) REFERENCES User(user_id)
);

-- ✅ 7. 권장 영양소 테이블 최적화
CREATE TABLE Recommended_Nutrition (
    recommended_nutrition_id INT AUTO_INCREMENT PRIMARY KEY,
    user_id VARCHAR(50) NOT NULL,
    inbody_info BOOLEAN DEFAULT FALSE,
    calories_min DECIMAL(10,2) NOT NULL,
    calories_max DECIMAL(10,2) NOT NULL,
    protein_min DECIMAL(10,2) NOT NULL,
    protein_max DECIMAL(10,2) NOT NULL,
    carbohydrate_min DECIMAL(10,2) NOT NULL,
    carbohydrate_max DECIMAL(10,2) NOT NULL,
    fat_min DECIMAL(10,2) NOT NULL,
    fat_max DECIMAL(10,2) NOT NULL,
    sugar_min DECIMAL(10,2) NOT NULL,
    sugar_max DECIMAL(10,2) NOT NULL,
    sodium_min DECIMAL(10,2) NOT NULL,
    sodium_max DECIMAL(10,2) NOT NULL,
    dietary_fiber_min DECIMAL(10,2) NOT NULL,
    dietary_fiber_max DECIMAL(10,2) NOT NULL,
    calcium_min DECIMAL(10,2) NOT NULL,
    calcium_max DECIMAL(10,2) NOT NULL,
    omega3_min DECIMAL(10,2) NOT NULL,
    omega3_max DECIMAL(10,2) NOT NULL,
    FOREIGN KEY (user_id) REFERENCES User(user_id)
);

-- ✅ 8. 성능 최적화를 위한 인덱스 추가
CREATE INDEX idx_user_id ON User(user_id);
CREATE INDEX idx_recipe_id ON Recipe_Data(recipe_id);
CREATE INDEX idx_ingredient_id ON Ingredient_Data(ingredient_id);

