# 🚀 Backend Practice REST API

회원 인증 및 게시판 기능을 제공하는 RESTful API 서버입니다.
JWT를 적용하여 API 통신을 구현했으며, JPA를 활용한 객체 지향적인 데이터베이스 설계를 적용

## Tech Stack

### Backend (`backend-practice/`)
* **Language:** Java 17
* **Framework:** Spring Boot 4.0.3
* **Security:** JWT (JSON Web Token), Spring Security
* **Database:** PostgreSQL, Spring Data JPA
* **Build Tool:** Maven
* **Documentation:** Swagger UI (Springdoc OpenAPI)

### Frontend (`frontend/`)
* **Library:** React 19 (Vite)
* **HTTP Client:** Axios
* **Routing:** React Router DOM 7

---

## Prerequisites

* Java 17
* Docker (PostgreSQL은 Spring Boot Docker Compose로 자동 실행)
* Node.js (frontend)

---

## Getting Started

### Backend
```bash
cd backend-practice
./mvnw spring-boot:run    # http://localhost:8080
```

### Frontend
```bash
cd frontend
npm install
npm run dev               # http://localhost:3000
```

---

## Key Features

### 1. 보안 및 인증
* **JWT 기반 인증:** 세션을 사용하지 않는 Stateless 아키텍처
* **비밀번호 암호화:** `BCryptPasswordEncoder`를 이용한 단방향 암호화
* **권한 제어:** 회원 정보 수정/삭제 및 게시글 관리에 대한 본인 검증 로직
* **CORS 설정:** Spring Security 레벨에서 frontend(`localhost:3000`) 허용

### 2. 회원 관리
* 회원가입 및 로그인 (Access Token 발급)
* 회원 정보(이름, 비밀번호) 통합 수정 기능
* 회원 탈퇴 처리 (탈퇴 시 작성한 게시글의 작성자 이름을 '탈퇴한 사용자'로 표시)

### 3. 게시판 관리
* 게시글 생성, 전체 목록 조회, 상세 조회
* 게시글 수정 및 삭제 (작성자만 가능하도록 권한 검증)
* 회원(Member)과 게시글(Post) 간의 양방향 연관관계 매핑

### 4. Frontend (React)
* 로그인 / 회원가입 페이지
* 게시글 목록, 상세 조회, 작성, 수정, 삭제
* 마이페이지 (회원 정보 수정, 계정 삭제)
* Axios 인터셉터로 JWT 자동 첨부 및 인증 만료 시 자동 리다이렉트

---

## API Endpoints

| Method | Endpoint | Description | Auth |
|--------|----------|-------------|------|
| POST | `/auth/signup` | 회원가입 | X |
| POST | `/auth/login` | 로그인 (JWT 발급) | X |
| GET | `/members` | 전체 회원 조회 | O |
| GET | `/members/{id}` | 회원 단건 조회 | O |
| PUT | `/members/{id}` | 회원 정보 수정 | O |
| DELETE | `/members/{id}` | 회원 탈퇴 | O |
| POST | `/posts` | 게시글 생성 | O |
| GET | `/posts` | 전체 게시글 조회 | O |
| GET | `/posts/{id}` | 게시글 상세 조회 | O |
| PUT | `/posts/{id}` | 게시글 수정 | O |
| DELETE | `/posts/{id}` | 게시글 삭제 | O |

---

## Testing

```bash
cd backend-practice
./mvnw test                              # 전체 테스트
./mvnw test -Dtest=AuthServiceImplTest   # 인증 서비스 테스트
./mvnw test -Dtest=MemberServiceImplTest # 회원 서비스 테스트
./mvnw test -Dtest=PostServiceImplTest   # 게시글 서비스 테스트
```
