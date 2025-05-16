package com.Hansung.Capston.service.ApiService;


import com.google.cloud.vision.v1.*;
import com.google.protobuf.ByteString;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.util.Collections;

@Service

public class VisionService {
    private final ImageAnnotatorClient client;

    @Autowired
    public VisionService(ImageAnnotatorClient client) {
        this.client = client;
    }

    // ③ MultipartFile(업로드된 이미지)을 받아서 텍스트를 추출하는 메서드
    public String extractText(MultipartFile file) {
        try {
            ByteString imgBytes = ByteString.copyFrom(file.getBytes());
            Image img = Image.newBuilder().setContent(imgBytes).build();
            Feature feat = Feature.newBuilder()
                    .setType(Feature.Type.DOCUMENT_TEXT_DETECTION)
                    .build();
            AnnotateImageRequest request = AnnotateImageRequest.newBuilder()
                    .addFeatures(feat)
                    .setImage(img)
                    .build();
            BatchAnnotateImagesResponse resp =
                    client.batchAnnotateImages(Collections.singletonList(request));
            AnnotateImageResponse r = resp.getResponses(0);

            if (r.hasError()) {
                throw new RuntimeException("Vision API 에러: " + r.getError().getMessage());
            }
            // 문서 OCR 전체 텍스트 한 번에
            return r.getFullTextAnnotation().getText();
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }
}
