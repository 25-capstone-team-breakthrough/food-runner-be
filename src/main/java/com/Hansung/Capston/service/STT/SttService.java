package com.Hansung.Capston.service.STT;

import com.google.cloud.speech.v1.*;
import com.google.protobuf.ByteString;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

@Service
public class SttService {

    private byte[] convertToWav(byte[] inputBytes) throws IOException {
        ProcessBuilder pb = new ProcessBuilder(
                "ffmpeg",
                "-i", "pipe:0",          // stdin으로 원본 입력
                "-f", "wav",             // 출력 컨테이너 WAV
                "-ar", "16000",          // 샘플레이트 16kHz
                "-ac", "1",              // Mono
                "-acodec", "pcm_s16le",  // Linear PCM 16bit
                "pipe:1"                 // stdout으로 출력
        );
        Process process = pb.start();
        // ffmpeg stdin에 M4A 데이터를 전달
        try (OutputStream os = process.getOutputStream()) {
            os.write(inputBytes);
        }
        // ffmpeg stdout에서 변환된 WAV 데이터 읽기
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        try (InputStream is = process.getInputStream()) {
            byte[] buffer = new byte[4096];
            int len;
            while ((len = is.read(buffer)) != -1) {
                baos.write(buffer, 0, len);
            }
        }
        return baos.toByteArray();
    }
    public String transcribe(MultipartFile audioFile) throws IOException {
        if (audioFile.isEmpty()) {
            throw new IllegalArgumentException("오디오 파일이 비어 있습니다.");
        }
        byte[] wavBytes = convertToWav(audioFile.getBytes());
        ByteString audioData = ByteString.copyFrom(wavBytes);
        RecognitionAudio recognitionAudio = RecognitionAudio.newBuilder()
                .setContent(audioData).build();

        RecognitionConfig config = RecognitionConfig.newBuilder()
                .setEncoding(RecognitionConfig.AudioEncoding.LINEAR16)
                .setSampleRateHertz(16000)
                .setLanguageCode("ko-KR")
                .setModel("latest_short")
                .setUseEnhanced(true)
                .build();

        try (SpeechClient speechClient = SpeechClient.create()) {
            RecognizeResponse response = speechClient.recognize(config, recognitionAudio);
            StringBuilder sb = new StringBuilder();
            for (SpeechRecognitionResult res : response.getResultsList()) {
                sb.append(res.getAlternatives(0).getTranscript()).append(" ");
            }
            return sb.toString().trim();
        } catch (Exception e) {
            throw new IOException(e);
        }
    }
}