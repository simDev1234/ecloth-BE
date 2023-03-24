//package com.ecloth.beta.utill;
//
//import com.amazonaws.AmazonClientException;
//import com.amazonaws.SdkClientException;
//import com.amazonaws.services.s3.AmazonS3;
//import com.amazonaws.services.s3.AmazonS3Client;
//import com.amazonaws.services.s3.model.CannedAccessControlList;
//import com.amazonaws.services.s3.model.ObjectMetadata;
//import com.amazonaws.services.s3.model.PutObjectRequest;
//import lombok.RequiredArgsConstructor;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.stereotype.Component;
//import org.springframework.web.multipart.MultipartFile;
//
//import java.io.File;
//import java.io.IOException;
//import java.util.UUID;
//
//@Component
//@RequiredArgsConstructor
//public class S3Uploader {
//
//    @Value("${cloud.aws.s3.bucket}")
//    private String bucket;
//    private final AmazonS3 amazonS3;
//    private static final String S3_BUCKET_NAME = "your_bucket_name";
//    private static final String S3_BUCKET_FOLDER = "your_bucket_folder";
//
//    public String uploadFileAndGetURL(MultipartFile multipartFile) {
//
//        try {
//
//            String originalFileName = multipartFile.getOriginalFilename();
//            String extension = getExtension(originalFileName);
//            String contentType = getContentType(extension);
//            String changedFilename = UUID.randomUUID().toString().replace("-", "") + "." + extension;
//
//            ObjectMetadata metadata = new ObjectMetadata();
//            metadata.setContentType(contentType);
//
//            amazonS3.putObject(new PutObjectRequest(bucket, changedFilename, multipartFile.getInputStream(), metadata)
//                    .withCannedAcl(CannedAccessControlList.PublicRead));
//            return amazonS3.getUrl(bucket, changedFilename).toString();
//
//        } catch(IOException | SdkClientException e) {
//
//        } catch(Exception e) {
//
//        }
//
//        return "";
//    }
//
//    private String getExtension(String fileName) {
//        String[] fileNameParts = fileName.split("\\.");
//        String extension = fileNameParts[fileNameParts.length - 1];
//        return extension;
//    }
//
//    private String getContentType(String extension){
//
//        String contentType = "";
//
//        switch (extension) {
//            case "jpeg" : contentType = "image/jpeg"; break;
//            case "png"  : contentType = "image/png"; break;
//            case "txt"  : contentType = "text/txt"; break;
//            case "csv"  : contentType = "text/csv"; break;
//        }
//
//        return contentType;
//    }
//
//
//    public static void main(String[] args) {
//        AmazonS3 s3Client = new AmazonS3Client(new ProfileCredentialsProvider());
//        try {
//            String filePath = "path/to/your/image/file.jpg";
//            File file = new File(filePath);
//            String fileName = file.getName();
//
//            ObjectMetadata metadata = new ObjectMetadata();
//            metadata.setContentLength(file.length());
//            metadata.setContentType("image/jpeg");
//
//            PutObjectRequest putObjectRequest = new PutObjectRequest(S3_BUCKET_NAME, S3_BUCKET_FOLDER + "/" + fileName, file);
//            putObjectRequest.setMetadata(metadata);
//
//            s3Client.putObject(putObjectRequest);
//            System.out.println("Successfully uploaded image to S3.");
//        } catch (AmazonClientException e) {
//            e.printStackTrace();
//        }
//    }
//}
//
//
////@Slf4j
////@RequiredArgsConstructor
////@Service
////public class S3Uploader {
////
////    public static final String CLOUD_FRONT_DOMAIN_NAME = ${CLOUD_FRONT_DOMAIN_NAME};
////
////    private final AmazonS3Client amazonS3Client;
////
////
////    @Value("${cloud.aws.s3.bucket}")
////    private String bucket;
////
////
////    public String upload(MultipartFile multipartFile, String dirName) throws IOException {
////        System.out.println(multipartFile);
////
////        File uploadFile = convert(multipartFile)
////                .orElseThrow(() -> new IllegalArgumentException("MultipartFile -> File로 전환이 실패했습니다."));
////
////        System.out.println("upload1 "+ uploadFile);
////
////        return upload(uploadFile, dirName);
////    }
////
////    private String upload(File uploadFile, String dirName) {
////
////        SimpleDateFormat date = new SimpleDateFormat("yyyymmddHHmmss");
////        String orgName = uploadFile.getName();
////        if(orgName.length()>30) orgName = orgName.substring(0,30);
////        String fileName = dirName + "/" + date.format(new Date()) + "-" + orgName;
////
////        System.out.println("upload2 "+ fileName);
////        String uploadImageUrl = putS3(uploadFile, fileName);
////        removeNewFile(uploadFile);
////        return fileName;
////    }
////
////    public void delete(String currentFilePath){
////        if ("".equals(currentFilePath) == false && currentFilePath != null) {
////            boolean isExistObject = amazonS3Client.doesObjectExist(bucket, currentFilePath);
////
////            if (isExistObject == true) {
////                amazonS3Client.deleteObject(bucket, currentFilePath);
////            }
////        }
////    }
////
////    private String putS3(File uploadFile, String fileName) {
////        amazonS3Client.putObject(new PutObjectRequest(bucket, fileName, uploadFile).withCannedAcl(CannedAccessControlList.PublicRead));
////        return amazonS3Client.getUrl(bucket, fileName).toString();
////    }
////
////    private void removeNewFile(File targetFile) {
////        targetFile.delete();
////    }
////
////    private Optional<File> convert(MultipartFile file) throws IOException {
////        File convertFile = new File(file.getOriginalFilename());
////        if(convertFile.createNewFile()) {
////            try (FileOutputStream fos = new FileOutputStream(convertFile)) {
////                fos.write(file.getBytes());
////            }
////            return Optional.of(convertFile);
////        }
////
////        return Optional.empty();
////    }
////}
