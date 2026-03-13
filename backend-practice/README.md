# 🚀 Backend Practice REST API

회원 인증 및 게시판 기능을 제공하는 RESTful API 서버입니다.
JWT를 적용하여 API 통신을 구현했으며, JPA를 활용한 객체 지향적인 데이터베이스 설계를 적용

* **Language:** Java 17
* **Framework:** Spring Boot 3.x
* **Security:** JWT (JSON Web Token)
* **Database:** PostgreSQL, Spring Data JPA
* **Build Tool:** Maven
* **Documentation:** Swagger UI (Springdoc OpenAPI)

---

## Key Features

### 1. 보안 및 인증 
* **JWT 기반 인증:** 세션을 사용하지 않는 Stateless 아키텍처
* **비밀번호 암호화:** `BCryptPasswordEncoder`를 이용한 단방향 암호화
* **권한 제어:** 회원 정보 수정/삭제 및 게시글 관리에 대한 본인 검증 로직 

### 2. 회원 관리
* 회원가입 및 로그인 (Access Token 발급)
* 회원 정보(이름, 비밀번호) 통합 수정 기능
* 회원 탈퇴 처리 (탈퇴 시 작성한 게시글의 작성자 이름을 '탈퇴한 사용자'로 표시)

### 3. 게시판 관리
* 게시글 생성, 전체 목록 조회, 상세 조회
* 게시글 수정 및 삭제 (작성자만 가능하도록 권한 검증)
* 회원(Member)과 게시글(Post) 간의 양방향 연관관계 매핑