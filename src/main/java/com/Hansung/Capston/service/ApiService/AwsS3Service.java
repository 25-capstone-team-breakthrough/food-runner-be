package com.Hansung.Capston.service.ApiService;

import com.amazonaws.HttpMethod;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.GeneratePresignedUrlRequest;
import com.amazonaws.services.s3.model.ObjectMetadata;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.UUID;

@Service
public class AwsS3Service {
  private final AmazonS3 amazonS3Client;
  private static final Logger logger = LoggerFactory.getLogger(AwsS3Service.class);
  @Value("${aws.s3.bucket}")
  private String bucketName;

  @Value("${aws.s3.inbody.folder}")
  private String inbodyFolder;

  @Value("${aws.s3.inbody.base-url}")
  private String inbodyBaseUrl;

  @Autowired
  public AwsS3Service(AmazonS3 amazonS3Client) {
    this.amazonS3Client = amazonS3Client;
  }

  public String generatePreSignedUrl(String fileName, String contentType) {
    String uniqueFileName = UUID.randomUUID().toString() + "-" + fileName;
    String objectKey = "meal-image/" + uniqueFileName;
    Date expiration = Date.from(LocalDateTime.now().plusMinutes(5).atZone(ZoneId.systemDefault()).toInstant());

    GeneratePresignedUrlRequest generatePresignedUrlRequest =
        new GeneratePresignedUrlRequest(bucketName, objectKey, HttpMethod.PUT)
            .withExpiration(expiration)
            .withContentType(contentType);

    URL preSignedUrl = amazonS3Client.generatePresignedUrl(generatePresignedUrlRequest);
    return preSignedUrl.toString();
  }

  public void deleteImageFromS3(String imageUrl) {
    try {
      String objectKey = extractObjectKeyFromUrl(imageUrl);

      amazonS3Client.deleteObject(bucketName, objectKey);
      logger.info("Deleted image from s3 bucket: " + objectKey);
    } catch (Exception e) {
      throw new RuntimeException("S3 이미지 삭제 실패", e);
    }
  }

  private String extractObjectKeyFromUrl(String imageUrl) {
    try {
      URL url = new URL(imageUrl);
      return url.getPath().substring(1);
    } catch (MalformedURLException e) {
      throw new RuntimeException("URL 형식 오류", e);
    }
  }

  /** Inbody 이미지 직접 업로드 → S3 URL 반환 */
  public String uploadInbodyImage(MultipartFile file) {
    try {
      // 1) 고유 키 생성: inbody-image/UUID-원본이름
      String uuid = UUID.randomUUID().toString();
      String original = file.getOriginalFilename();
      String key = String.format("%s/%s-%s", inbodyFolder, uuid, original);

      // 2) 메타데이터 세팅
      ObjectMetadata meta = new ObjectMetadata();
      meta.setContentType(file.getContentType());
      meta.setContentLength(file.getSize());

      // 3) S3에 업로드
      amazonS3Client.putObject(bucketName, key, file.getInputStream(), meta);

      // 4) 접근 가능한 URL 조합
      return inbodyBaseUrl + "/" + uuid + "-" + original;
    } catch (IOException e) {
      throw new RuntimeException("S3 업로드 실패", e);
    }
  }



}
