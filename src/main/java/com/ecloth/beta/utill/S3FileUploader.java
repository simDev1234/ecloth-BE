package com.ecloth.beta.utill;

import java.io.InputStream;
import java.util.UUID;

import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

@Component
public class S3FileUploader {
    @Value("{cloud.aws.credentials.access.key}")
    private static String accessKey;

    @Value("{cloud.aws.credentials.secret-key}")
    private static String secretKey;

    private static final String REGION_NAME = "ap-northeast-2"; // 사용하고자 하는 리전 정보
    private static final String BUCKET_NAME = "weatheroutfit"; // S3 버킷 이름

    public static String uploadImageToS3AndGetURL(MultipartFile multipartFile) throws Exception {
        // AWS S3 클라이언트 생성
        AmazonS3 s3client = AmazonS3ClientBuilder.standard()
                .withRegion(Regions.fromName(REGION_NAME))
                .withCredentials(new AWSStaticCredentialsProvider(new BasicAWSCredentials(accessKey, secretKey)))
                .build();

        String originalFileName = multipartFile.getOriginalFilename();
        String extension = getExtension(originalFileName);
        String contentType = multipartFile.getContentType();
        String changedFilename = UUID.randomUUID().toString().replace("-", "") + "." + extension;

        ObjectMetadata metadata = new ObjectMetadata();
        metadata.setContentType(contentType);

        // Convert MultipartFile to InputStream
        InputStream inputStream = multipartFile.getInputStream();

        PutObjectRequest putObjectRequest = new PutObjectRequest(BUCKET_NAME, changedFilename, inputStream, metadata)
                .withCannedAcl(CannedAccessControlList.PublicRead);

        try {
            s3client.putObject(putObjectRequest);
        } catch (Exception e) {
            throw new Exception("S3 파일 업로드 실패", e); // 예외 정보를 출력하기 위해 새로운 예외를 생성하고 예외를 다시 던집니다.
        }

        // S3 URL 생성
        String url = String.format("https://%s.s3.%s.amazonaws.com/%s", BUCKET_NAME, REGION_NAME, changedFilename);
        return url;
    }

    public static void deleteObjectFromS3(String imageUrl) throws Exception {
        // AWS S3 클라이언트 생성
        AmazonS3 s3client = AmazonS3ClientBuilder.standard()
                .withRegion(Regions.fromName(REGION_NAME))
                .withCredentials(new AWSStaticCredentialsProvider(new BasicAWSCredentials(accessKey, secretKey)))
                .build();

        String[] urlParts = imageUrl.split("/");
        String filename = urlParts[urlParts.length - 1];

        try {
            s3client.deleteObject(BUCKET_NAME, filename);
        } catch (Exception e) {
            throw new Exception("S3 파일 삭제 실패", e); // 예외 정보를 출력하기 위해 새로운 예외를 생성하고 예외를 다시 던집니다.
        }
    }



    private static String getExtension(String fileName) {
        String[] fileNameParts = fileName.split("\\.");
        String extension = fileNameParts[fileNameParts.length - 1];
        return extension;
    }

    private static String getContentType(String extension) {
        String contentType = "";

        switch (extension) {
            case "jpeg":
            case "jpg":
                contentType = "image/jpeg";
                break;
            case "png":
                contentType = "image/png";
                break;
            case "gif":
                contentType = "image/gif";
                break;
        }

        return contentType;
    }
}

