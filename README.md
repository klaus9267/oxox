# ⭕️ 반반 투표 서비스 OXOX ❌

- 기간 : 2024.07.03 ~ 2024.08.04
- 배포 URL : [oxox-one.vercel.app](https://oxox-one.vercel.app/)
- Test email : `test@email.com`
- Test pwd : `test password`

</br>

## 👉🏻 프로젝트 소개

- **OXOX는 조그만 고민들을 해결하기 위한 찬반투표 서비스입니다.**
- 사진과 함께 자신의 고민을 공유하고 이에 대한 투표 및 의견을 받을 수 있습니다.
- 댓글 작성 및 해당 댓글에 리액션을 통해 고민에 공감할 수 있습니다.
- 개인의 프로필 페이지에서 작성 / 참가한 투표들을 볼 수 있습니다.

</br>

## 💁 팀원 구성
| **김민호** | **홍유진** |
| :------: |  :------: |
| [<img src="https://github.com/user-attachments/assets/55422569-b255-47c7-9b40-d65f7b18f6b7" height=150 width=150> <br/> @klaus9267](https://github.com/klaus9267) <br/> 백엔드 개발 | [<img src="https://github.com/user-attachments/assets/ae04bc5a-6885-4a3c-abb9-3465754f427e" height=150 width=150> <br/> @ujeans](https://github.com/ujeans) <br/> 프런트엔드 개발 |


</br>

## 🛠️ 사용 기술

### BackEnd
- Java 17
- Spring Boot 3.3.1, Spring MVC, Spring Security
- JPA, H2, Redis, Flyway, QueryDsl
- Junit5, Mockite, Jacoco
- Gradle 8.x

### DevOps
- Ubuntu 24.04
- Jacoco
- SonarCloud
- Github Action, Docker
- Prometheus, Grafana
- AWS EC2, RDS, S3

## 아키텍쳐

![oxox drawio (1)](https://github.com/user-attachments/assets/3e008d0e-7fa9-49c8-a744-69aa17d903b0)

<br>

##  프로젝트 구조

<details><summary>
구조
</summary>

```
.
├── README.md
├── build.gradle
└── src
    ├── main
    │   ├── java
    │   │   └── kimandhong
    │   │       └── oxox
    │   │           ├── OxoxApplication.java
    │   │           ├── application
    │   │           │   ├── auth
    │   │           │   │   ├── JwtFilter.java
    │   │           │   │   ├── JwtUtil.java
    │   │           │   │   └── SecurityUtil.java
    │   │           │   ├── bulk
    │   │           │   │   ├── BulkController.java
    │   │           │   │   ├── BulkRepository.java
    │   │           │   │   ├── BulkService.java
    │   │           │   │   └── Sql.java
    │   │           │   ├── config
    │   │           │   │   ├── QueryDslConfig.java
    │   │           │   │   ├── RedisConfig.java
    │   │           │   │   ├── S3Config.java
    │   │           │   │   ├── SecurityConfig.java
    │   │           │   │   ├── SwaggerConfig.java
    │   │           │   │   └── WebConfig.java
    │   │           │   ├── handler
    │   │           │   │   ├── GlobalExceptionHandler.java
    │   │           │   │   └── error
    │   │           │   │       ├── CustomException.java
    │   │           │   │       ├── ErrorCode.java
    │   │           │   │       └── ErrorResponse.java
    │   │           │   └── s3
    │   │           │       ├── S3Service.java
    │   │           │       └── S3path.java
    │   │           └── domain
    │   │               ├── comment
    │   │               │   ├── CommentController.java
    │   │               │   ├── CommentRepository.java
    │   │               │   ├── CommentService.java
    │   │               │   ├── domain
    │   │               │   │   ├── Comment.java
    │   │               │   │   └── CommentOneToMany.java
    │   │               │   ├── dto
    │   │               │   │   ├── dtos...
    │   │               │   └── params
    │   │               │       └── CommentPaginationParam.java
    │   │               ├── common
    │   │               │   ├── TimeEntity.java
    │   │               │   └── swagger
    │   │               │       ├── SwaggerCreated.java
    │   │               │       ├── SwaggerNoContent.java
    │   │               │       └── SwaggerOK.java
    │   │               ├── post
    │   │               │   ├── PostController.java
    │   │               │   ├── PostService.java
    │   │               │   ├── domain
    │   │               │   │   ├── Post.java
    │   │               │   │   └── PostOneToMany.java
    │   │               │   ├── dto
    │   │               │   │   ├── dtos...
    │   │               │   ├── params
    │   │               │   │   ├── params...
    │   │               │   └── repository
    │   │               │       ├── PostCustomRepository.java
    │   │               │       └── PostRepository.java
    │   │               ├── profile
    │   │               │   ├── Profile.java
    │   │               │   ├── ProfileController.java
    │   │               │   ├── ProfileService.java
    │   │               │   ├── dto
    │   │               │   │   └── dtos...
    │   │               │   └── repository
    │   │               │       ├── ProfileCustomRepository.java
    │   │               │       └── ProfileRepository.java
    │   │               ├── reaction
    │   │               │   ├── ReactionController.java
    │   │               │   ├── ReactionRepository.java
    │   │               │   ├── ReactionService.java
    │   │               │   └── domain
    │   │               │       ├── Emoji.java
    │   │               │       └── Reaction.java
    │   │               ├── user
    │   │               │   ├── UserController.java
    │   │               │   ├── UserRepository.java
    │   │               │   ├── UserService.java
    │   │               │   ├── domain
    │   │               │   │   ├── User.java
    │   │               │   │   └── UserOneToMany.java
    │   │               │   └── dto
    │   │               │       ├── dtos...
    │   │               └── vote
    │   │                   ├── Vote.java
    │   │                   ├── VoteController.java
    │   │                   ├── VoteRepository.java
    │   │                   └── VoteService.java
    │   └── resources
    │       ├── application.yml
    │       ├── banner.txt
    │       └── db
    │           └── migration
    │               ├── V1__init.sql
    │               ├── ...

```
</details>



## 🏞️ 페이지별 기능

### [로그인]
- 소셜 구글 로그인
- Email, Password를 사용한 로그인
    - 회원가입된 정보와 다를 경우 경고창을 띄워주기
  
| 로그인 |
|----------|
|<img src="https://github.com/user-attachments/assets/c5b2cf26-b6e8-432c-abd7-64a684f88252" width="400" /> |


### [회원가입]
- 소셜 로그인
- Email, Password를 사용한 회원가입
    - 유효성 검사
        - 이메일 주소의 형식
        - 비밀번호 알파벳,숫자 포함 최소 6자
        - 닉네임 20자 제한
    - 유효성 검사가 통과된 다음 버튼 활성화

| 회원가입 |
|----------|
|<img src="https://github.com/user-attachments/assets/929dbd43-d696-4932-865a-b5cbf791d62e" width="400" /> |

 
### [초기화면]
- 무한스크롤이 적용된 게시글 리스트
    - 진행중인 게시글만 보여주기
- 로그인을 하지 않을 경우에는 로그인 이동 모달창 띄워주기

| 로그인 한 경우 | 로그인 안한 경우 | 
|----------| ---------- | 
|<img src="https://github.com/user-attachments/assets/24c644c8-4750-46a5-975d-3432fd371406" width="400" /> | <img src="https://github.com/user-attachments/assets/20b52268-b43b-4ab7-8eb3-66d58562fed6" height="760" /> |


### [게시글 작성]
- 제목, 내용, 사진을 입력하여 게시글을 작성
    - 제목, 내용 둘 다 작성되어야 공유 가능
    - 사진은 파일 선택 및 드래그 앤 드롭으로 업로드 가능

| 게시글 작성 |
|----------|
|<img src="https://github.com/user-attachments/assets/549cecf9-ec26-41f5-b8f0-d0ac7155d196" width="400" /> |

### [상세 게시글]
- 해당 게시글에 대한 정보 보여주기
    - 게시글의 찬/반 투표 비율을 보여주기
    - 투표 종료 타이머를 보여주기 (24시간이 지나면 투표는 마감)
- 투표하기 버튼을 클릭하면 투표를 할 수 있는 모달창이 띄워주기
- 로그인을 했을 경우 댓글 목록을 볼 수 있음

| 상세 게시글 |
|----------|
|<img src="https://github.com/user-attachments/assets/47c493b4-2bb7-48dc-9c11-7f5e154d0eef" width="400" /> |


### [투표]
- 찬성 또는 반대에 투표하기
- 이미 투표 했을 경우에도 재투표 가능
  
| 투표 |
|----------|
| <img src="https://github.com/user-attachments/assets/50d45dd6-4495-4634-8a46-e1ed089b6432" width="400" /> |


### [댓글]
- 댓글을 작성하기
- 댓글에 대한 리액션을 작성 및 수정
    - 리액션 종류에 따른 공감 횟수 볼 수 있음
    - 내 리액션은 강조돼서 표현

| 댓글 |
|----------|
|<img src="https://github.com/user-attachments/assets/20f7e85d-8065-4bc2-9e04-01e64828192e" width="400" /> |

### [프로필]
- 사용자의 정보를 보여주기
    - 닉네임을 수정할 수 있고 20글자를 초과할 수 없음
- 탭 메뉴를 통해 사용자가 작성한 게시글 또는 투표한 게시글 목록을 다르게 보여주기
- 로그아웃 아이콘을 클릭시 로그아웃

| 프로필 수정 | 게시글이 있을 경우 | 게시글이 없을 경우 |
|----------| ---------- | ---------- | 
| <img src="https://github.com/user-attachments/assets/43512a38-93ed-4d13-9b3c-a62ad96202af" width="400" /> | <img src="https://github.com/user-attachments/assets/84861d1a-d387-4d0b-aa5d-06ffbedee09e" width="400" /> | <img src="https://github.com/user-attachments/assets/fe065505-f304-4a31-bd12-edd3330b7fa3" width="400" /> |
