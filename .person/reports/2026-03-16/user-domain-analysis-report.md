# 📋 Phase 2: User 도메인 분석 보고서

**Reporter:** handikim
**Date:** 2026-03-16
**Target:** User Domain (Registration, Login, Admin Management)

---

## 1. 개요 (Overview)
본 보고서는 Phase 2에서 구현된 사용자(User) 도메인의 핵심 로직과 설계 의도를 분석합니다. 이 도메인은 서비스의 진입점인 인증(Authentication)과 인가(Authorization), 그리고 관리자(Admin)를 위한 사용자 관리 기능을 포함합니다.

## 2. 주요 기능 및 핵심 로직 (Core Logic)

### 2.1 회원가입 (Registration)
- **로직:** `UserRequest.JoinDTO` → `UserService.join()` → `UserRepository.save()`
- **핵심 기술:** 
  - **BCrypt 암호화:** 보안을 위해 비밀번호를 해싱하여 DB에 저장합니다.
  - **PRG 패턴:** `POST /join` 성공 후 `redirect:/login-form`으로 처리하여 새로고침 시 데이터 중복 전송(F5 문제)을 방지합니다.

### 2.2 로그인 및 인증 (Authentication)
- **로직:** `UserService.login()` → 비밀번호 검증 (`matches()`) → `HttpSession`에 `sessionUser` 저장
- **핵심 기술:**
  - **세션 기반 인증:** Mustache SSR(Server Side Rendering) 방식에 최적화된 인증 방식입니다. 템플릿 엔진에서 `{{sessionUser}}`로 직접 접근할 수 있어 구현이 간결합니다.

### 2.3 관리자 사용자 관리 (Admin Management)
- **로직:** `AdminUserController` → 페이징 조회 (`Pageable`) → 권한 변경 (`updateRole`)
- **핵심 기술:**
  - **Spring Data JPA Paging:** 대용량 회원 데이터를 효율적으로 처리하기 위해 페이징(Page) 기능을 사용했습니다.
  - **Interceptor (AOP):** `/admin/**` 경로의 모든 요청에 대해 `AdminInterceptor`가 작동하여 중복된 권한 체크 로직을 제거했습니다.

---

## 3. 설계 의도 및 선택 이유 (Design Decisions)

### 3.1 왜 JWT 대신 세션을 사용했나요?
이 프로젝트는 Mustache를 사용하는 **전통적인 서버 사이드 렌더링(SSR)** 프로젝트입니다. 
- **연동 편의성:** 세션은 서버에서 템플릿으로 데이터를 바로 넘기기 가장 편리합니다.
- **보안성:** 브라우저의 LocalStorage에 토큰을 저장하는 JWT보다, 서버 메모리에 데이터를 두는 세션이 현재 구조에서는 더 안전합니다.

### 3.2 왜 인터셉터(Interceptor)를 사용했나요?
보안 체크는 **비즈니스 로직(Service)**이나 **표현 계층(Controller)**의 핵심 업무가 아닌 '공통 관심사(Cross-cutting Concern)'입니다. 인터셉터를 통해 이를 분리함으로써:
- 컨트롤러 코드가 깔끔해집니다.
- 특정 경로(`/admin/**`)에 대한 보안 정책을 한곳에서 관리할 수 있습니다.

### 3.3 왜 BCrypt 암호화를 선택했나요?
단순 해시(MD5, SHA-256)는 연산 속도가 너무 빨라 '무차별 대입 공격(Brute-force)'에 취약합니다. BCrypt는 '워크 팩터(Work Factor)'를 통해 해싱 속도를 조절할 수 있고, 매번 다른 솔트(Salt)값을 생성하여 동일한 비밀번호도 다르게 저장되므로 보안성이 매우 높습니다.

---

## 4. 학습 포인트 (Key Takeaways)
1. **관심사의 분리:** DTO(데이터 전달), Service(비즈니스 로직), Controller(요청 제어), Interceptor(공통 보안)의 역할 분담을 이해합니다.
2. **보안의 기본:** 비밀번호는 절대 평문으로 저장하지 않으며, 권한 체크는 모든 요청의 입구(Interceptor)에서 수행되어야 합니다.
3. **UX 고려:** PRG 패턴을 활용하여 사용자 경험을 개선하고 데이터 무결성을 지킵니다.

---
*(이 보고서는 학습 목적으로 작성되었으며, 추후 고도화 단계에서 JWT나 OAuth2 도입 시 비교 자료로 활용될 수 있습니다.)*
