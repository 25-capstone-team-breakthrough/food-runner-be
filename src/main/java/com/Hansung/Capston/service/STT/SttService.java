package com.Hansung.Capston.service.STT;

import com.google.cloud.speech.v1.*;
import com.google.protobuf.ByteString;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@Service
public class SttService {
    public String transcribe(MultipartFile audioFile) throws IOException {
        if (audioFile.isEmpty()) {
            throw new IllegalArgumentException("audioFile is empty");
        }

        //오디오 파일을 byte array 로 변환
        byte[] audioBytes = audioFile.getBytes();

        //클라이언트 인스턴스 화
        try (SpeechClient speechClient = SpeechClient.create()) {
            //오디오 객체 생성
            ByteString audioData = ByteString.copyFrom(audioBytes);
            RecognitionAudio recognitionAudio = RecognitionAudio.newBuilder()
                    .setContent(audioData)
                    .build();

            //설정 객체 생성
            RecognitionConfig recognitionConfig =
                    RecognitionConfig.newBuilder()
                            .setEncoding(RecognitionConfig.AudioEncoding.LINEAR16)
                            .setSampleRateHertz(16000)
                            .setLanguageCode("ko-KR")
                            .setModel("latest_short")
                            .setUseEnhanced(true)
                            .build();

            // 오디오-텍스트 변환 수행
            RecognizeResponse response = speechClient.recognize(recognitionConfig, recognitionAudio);
            List<SpeechRecognitionResult> results = response.getResultsList();

            StringBuilder sb = new StringBuilder();
            for (SpeechRecognitionResult res : results) {
                sb.append(res.getAlternatives(0).getTranscript())
                        .append(" ");
            }
            return sb.toString().trim();
        }catch (Exception e) {
            throw new IOException(e);
        }

    }
}
