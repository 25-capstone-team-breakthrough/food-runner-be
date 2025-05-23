package com.Hansung.Capston.service.STT;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;

@Service
public class AuthService {

    private static final String AUTH_URL = "https://openapi.vito.ai/v1/authenticate";

    @Value("${vito.api.client-id}")
    private String clientId;

    @Value("${vito.api.client-secret}")
    private String clientSecret;

    /**
     * RTZR STT OpenAPI로부터 JWT 토큰을 발급받아 반환합니다.
     */
    public String getJwtToken() throws IOException {
        // 1) 커넥션 설정
        HttpURLConnection conn = (HttpURLConnection) new URL(AUTH_URL).openConnection();
        conn.setRequestMethod("POST");
        conn.setRequestProperty("accept", "application/json");
        conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
        conn.setDoOutput(true);

        // 2) form-data body 작성
        String payload = "client_id=" + clientId + "&client_secret=" + clientSecret;
        byte[] out = payload.getBytes(StandardCharsets.UTF_8);
        try (OutputStream os = conn.getOutputStream()) {
            os.write(out);
        }

        // 3) 응답 읽기
        InputStream is = (conn.getResponseCode() / 100 == 2)
                ? conn.getInputStream()
                : conn.getErrorStream();
        try (Scanner scanner = new Scanner(is).useDelimiter("\\A")) {
            return scanner.hasNext() ? scanner.next() : "";
        }
    }
}

