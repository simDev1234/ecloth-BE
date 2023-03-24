//package com.ecloth.beta.utill;
//
//import com.amazonaws.auth.DefaultAWSCredentialsProviderChain;
//import com.amazonaws.services.cloudfront.AmazonCloudFront;
//import com.amazonaws.services.cloudfront.AmazonCloudFrontClientBuilder;
//import com.amazonaws.services.cloudfront.model.*;
//import java.util.List;
//
//public class CloudFront {
//
//    public static void main(String[] args) {
//        String distributionId = "YourDistributionId";
//        AmazonCloudFront cloudFront = AmazonCloudFrontClientBuilder.standard()
//                .withCredentials(DefaultAWSCredentialsProviderChain.getInstance())
//                .build();
//
//        // CloudFront 배포 정보 가져오기
//        GetDistributionRequest getDistributionRequest = new GetDistributionRequest()
//                .withId(distributionId);
//        GetDistributionResult getDistributionResult = cloudFront.getDistribution(getDistributionRequest);
//        Distribution distribution = getDistributionResult.getDistribution();
//        System.out.println(distribution.toString());
//
//        // CloudFront 배포 업데이트
//        List<CacheBehavior> cacheBehaviors = distribution.getCacheBehaviors().getItems();
//        for (CacheBehavior cacheBehavior : cacheBehaviors) {
//            if (cacheBehavior.getPathPattern().equals("/images/*")) {
//                cacheBehavior.setMinTTL(3600L);
//                cacheBehavior.setMaxTTL(86400L);
//                UpdateDistributionRequest updateDistributionRequest = new UpdateDistributionRequest()
//                        .withId(distributionId)
//                        .withDistributionConfig(new DistributionConfig()
//                                .withCacheBehaviors(new CacheBehaviors().withItems(cacheBehavior)));
//                cloudFront.updateDistribution(updateDistributionRequest);
//                break;
//            }
//        }
//    }
//}

