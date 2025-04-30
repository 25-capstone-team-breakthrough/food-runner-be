package com.Hansung.Capston.service;

import com.amazonaws.HttpMethod;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.GeneratePresignedUrlRequest;
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

  @Value("${aws.s3.bucketName}")
  private String bucketName;

  public AwsS3Service(AmazonS3 amazonS3Client) {
    this.amazonS3Client = amazonS3Client;
  }

  public URL generatePreSignedUrl(String fileName, String contentType) {
    String uniqueFileName = UUID.randomUUID().toString() + "-" + fileName;
    Date expiration = Date.from(LocalDateTime.now().plusMinutes(5).atZone(ZoneId.systemDefault()).toInstant());

    GeneratePresignedUrlRequest generatePresignedUrlRequest =
        new GeneratePresignedUrlRequest(bucketName, uniqueFileName, HttpMethod.PUT)
            .withExpiration(expiration)
            .withContentType(contentType);

    return amazonS3Client.generatePresignedUrl(generatePresignedUrlRequest);
  }
}
