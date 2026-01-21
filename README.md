# mes-backend

MES(Manufacturing Execution System) 도메인을 기반으로  
**실제 현업에서 사용 가능한 백엔드 구조를 학습·구현하기 위해 제작한  
Spring Boot 기반 REST API 서버**입니다.

단순 CRUD 구현이 아닌,  
**인증·보안·도메인 분리·예외 처리 등 실무에서 요구되는 요소를 중심으로 설계**했습니다.

---

## 기술 스택
- Java 17
- Spring Boot
- Spring Security + JWT 기반 인증
- JPA (Hibernate)
- Gradle
- MariaDB / MySQL

---

## 핵심 설계 포인트

- **JWT 기반 인증/인가 구조**
  - 로그인 시 Access Token 발급
  - Security Filter를 통한 요청 검증
  - 인증 실패/권한 오류에 대한 공통 예외 처리

- **도메인 중심 패키지 구조**
  - controller / service / repository 역할 분리
  - 회원, 생산 오더, 판매 오더 도메인 분리

- **실무 기준 예외 처리**
  - GlobalExceptionHandler를 통한 공통 예외 응답
  - 클라이언트가 처리 가능한 에러 구조 설계

---

## 주요 기능

- 회원 관리
  - 로그인 및 JWT 인증 처리
- 생산 오더 관리
  - 생산 지시 등록 / 조회
- 판매 오더 관리
  - 주문 등록 / 조회
- Spring Security 기반 접근 제어

---

## 프로젝트 목적

- ERP / MES 백엔드 구조 이해
- Spring Security + JWT 인증 흐름 실습
- 실무형 REST API 설계 경험
- **백엔드 신입 개발자 포트폴리오 용도**
