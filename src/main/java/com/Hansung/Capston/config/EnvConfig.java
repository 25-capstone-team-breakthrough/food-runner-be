package com.Hansung.Capston.config;

import io.github.cdimascio.dotenv.Dotenv;
import jakarta.annotation.PostConstruct;
import org.springframework.core.io.ClassPathResource;

import java.io.File;
import java.io.IOException;

public class EnvConfig {
    @PostConstruct
    public void loadEnv() {
        try {
            // resources 폴더 내 .env 파일의 실제 경로 찾기
            File envFile = new ClassPathResource(".env").getFile();

            Dotenv dotenv = Dotenv.configure()
                    .directory(envFile.getParent()) // .env 파일이 위치한 디렉토리 설정
                    .load();

            // 환경 변수 설정
            System.setProperty("SECRET_KEY", dotenv.get("SECRET_KEY"));
            System.setProperty("EXPIRATION_HOURS", dotenv.get("EXPIRATION_HOURS"));
            System.setProperty("ISSUER", dotenv.get("ISSUER"));

        } catch (IOException e) {
            throw new RuntimeException("Failed to load .env file from resources", e);
        }
    }
}
