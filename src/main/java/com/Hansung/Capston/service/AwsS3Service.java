package com.Hansung.Capston.service;

import com.amazonaws.HttpMethod;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.GeneratePresignedUrlRequest;
import java.net.MalformedURLException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

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

  @Autowired
  public AwsS3Service(AmazonS3 amazonS3Client) {
    this.amazonS3Client = amazonS3Client;
  }

  public String generatePreSignedUrl(String fileName, String contentType) {
    String uniqueFileName = UUID.randomUUID().toString() + "-" + fileName;
    Date expiration = Date.from(LocalDateTime.now().plusMinutes(5).atZone(ZoneId.systemDefault()).toInstant());

    GeneratePresignedUrlRequest generatePresignedUrlRequest =
        new GeneratePresignedUrlRequest(bucketName, uniqueFileName, HttpMethod.PUT)
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

}
