# ecloth-BE

## **요약**

- ‘오늘이 봄 날씨인가, 여름 날씨인가? 이럴 때는 무엇을 입어야 하지? 다른 사람은 뭘 입지?’
- 이러한 고민을 해결하기 위해, 오늘의 날씨에 맞는 옷차림 정보를 제공받고,
다른 사람들과 오늘의 룩을 공유할 수 있는 소셜 커뮤니티를 만들었습니다.
- Todoy Look을 포스팅하고, 사람들로부터 댓글과 좋아요를 받을 수 있습니다.
- 관심이 있는 사람들을 팔로우하고, 나를 팔로우한 사람들을 확인하세요.
- 대화를 나누고 싶은 사람이 있다면 1 : 1 채팅을 신청하여 말을 걸어볼 수 있습니다.

## **역할**

- Github Conventional commit, PR 리뷰, Code deploy와 Github Actions을 통한 배포 자동화 시도
- Github에 Markdown 언어를 사용하여 JMeter 부하테스트 결과 및 TroubleShooting 기록
- 자바 진영의 ORM인 JPA와 QueryDSL를 통해 join fetch로 포스트(게시판), 팔로우 목록 조회
- Stomp.js와 Spring Web Socket을 통해 채팅 기능의 Socket 통신 구현
- RDBMS로 MySQL(실행 환경), H2(개발 환경) 사용, NoSQL로 Redis와 MongoDB 사용
- JMeter를 통해 부하 테스트를 진행, 오류 72%, 496.9 TPS -> 오류 0%, 2193.9 TPS 로 개선
- AWS EC2(ubuntu), S3, RDS를 사용하여 클라우드 서버 배포
- Swagger를 통한 API Document 작성, Logback을 통한 로그 기록, Jasypt를 통한 인증키 암호화
             
## 스택
- Spring Security
- Spring Data JPA, QueryDSL
- Redis, MongoDB
- Spring Web Socket
- Swagger, Logback
- AWS EC2, RDS(MySQL), S3


## 아키텍처 구성도
![camping101](https://user-images.githubusercontent.com/107039546/233097879-d3c439f2-62ec-48e9-8e63-48d1caa5dd3a.jpg)

## ERD
![ecloth-erd drawio](https://user-images.githubusercontent.com/107039546/233263818-88562797-84b6-4b75-9d07-a69d024600d0.png)


## 트러블 슈팅 및 부하테스트 기록
[https://github.com/Ecloth/ecloth-BE/blob/main/GOEUN.md](https://github.com/Ecloth/ecloth-BE/blob/main/GOEUN.md)


## 디자인
<img width="720" alt="Untitled (3)" src="https://user-images.githubusercontent.com/107039546/233099339-e710a522-ec71-4978-8b78-2cb13c3a6588.png">
<img width="720" alt="Untitled (2)" src="https://user-images.githubusercontent.com/107039546/233099436-7fe97763-1c87-4684-aa60-2279eccea05d.png">
<img width="720" alt="Untitled (1)" src="https://user-images.githubusercontent.com/107039546/233099379-8193d360-1b7a-4107-bf61-085ca396731b.png">
<img width="720" alt="Untitled" src="https://user-images.githubusercontent.com/107039546/233099481-e533e29c-c188-4821-819a-0925c472194e.png">
