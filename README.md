# ecloth-BE

## 개요
``````
- 소개 : 
  오늘의 날씨에 맞는 옷차림을 알려주는 소셜 커뮤니티로,
  인스타그램과 같이 여러개의 이미지를 포스팅할 수 있고, 댓글/대댓글을 달 수 있으며,
  회원 프로필을 통해 1 : 1 채팅을 신청하여 대화할 수 있는 공간입니다.
  
- 담당 작업(고은) : 
  1. Github Conventional commit, PR 리뷰
  2. JPA와 QueryDSL를 통해 join fetch로 포스트(게시판), 팔로우 목록 조회
  3. Spring Web Socket과 MongoDB를 통해 채팅 기능 구현
  4. JMeter를 통해 순간 부하 테스트 진행 : 오류 72%, 496.9 TPS -> 오류 0%, 2193.9 TPS 로 개선
  5. AWS EC2(ubuntu), S3, RDS를 사용하여 클라우드 서버 배포
  6. Swagger를 통한 API Document 작성, Logback을 통한 로그 기록, Jasypt를 통한 인증키 암호화
  
- 개선 방향 :
  1. 페이징 목록 조회 응답 포맷을 제너릭 클래스로 통일할 필요 있음
  2. 회원 프로필 조회와 같이 자주 사용하는 API는 별도로 분리할 필요 있음
  3. 1 : 1 채팅 외에 1 : N 채팅 또한 가능하게 추가 필요
  4. S3 서버와 CloundFront를 연동하여 S3를 CDN 서버로 사용할 필요
``````

## 스택
- Spring Security
- Spring Data JPA, QueryDSL
- Redis, MongoDB
- Spring Web Socket
- Swagger, Logback
- AWS EC2, RDS(MySQL), S3

## 구성원
- 백엔드 : 최고은, 이윤지, 송한별
- 프론트 : 서유림, 장기철

## 아키텍처 구성도
![camping101](https://user-images.githubusercontent.com/107039546/233097879-d3c439f2-62ec-48e9-8e63-48d1caa5dd3a.jpg)

## 트러블 슈팅 및 부하테스트 기록
[https://github.com/Ecloth/ecloth-BE/blob/main/GOEUN.md](https://github.com/Ecloth/ecloth-BE/blob/main/GOEUN.md)
