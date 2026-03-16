# MES Backend API Server

MES(Manufacturing Execution System) 백엔드 API 서버입니다.  
Spring Boot 기반의 백엔드 구조로 생산/작업 흐름 및 관련 도메인 API를 제공하도록 설계된 프로젝트입니다.

---

## 📌 프로젝트 소개

- 생산/작업 관련 도메인 API 구현 준비
- Spring Boot 기반 백엔드 아키텍처 구성
- 향후 프론트엔드 연동 및 비즈니스 로직 확장 예정

---

## 🛠 기술 스택

### Backend
- Java
- Spring Boot
- Spring Web
- Spring Data JPA
- (필요 시 JWT / Spring Security 연동 예정)

### Database
- MySQL / MariaDB (연동 예정)

### Dev Tools
- Git
- GitHub
- IntelliJ / VS Code
- Postman (API 테스트)

---

## 🚀 로컬 실행 방법

### 1. 저장소 클론

```bash
git clone https://github.com/Nara-Park-513/mes-backend.git
cd mes-backend
2. 환경 설정

application.yml 또는 application.properties에 데이터베이스 및 기타 환경 설정을 추가합니다.

예시:

spring.datasource.url=jdbc:mysql://localhost:3306/mesdb
spring.datasource.username=your_username
spring.datasource.password=your_password
3. 의존성 설치 및 빌드
./gradlew clean build
4. 실행
./gradlew bootRun

기본 실행 포트는 http://localhost:8080 입니다.

📌 구현 예정 기능

생산 / 작업 도메인 API 구현

스케줄 및 리포트 API

사용자 및 권한 관리 연동

프론트엔드와의 REST API 연동

📜 License

본 프로젝트는 학습 및 포트폴리오 목적의 백엔드 서버입니다
