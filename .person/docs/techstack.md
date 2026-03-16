## 🛠️ 캠핑장 예약 시스템 기술 스택 및 구현 명세서

### 1. 백엔드 핵심 (Backend Core)

- **언어 및 프레임워크**: Java 21, Spring Boot 3.x
- **빌드 툴**: Gradle
- **아키텍처 패턴**: MVC (Model-View-Controller) 패턴 기반의 서버 사이드 렌더링(SSR).

### 2. 데이터베이스 및 데이터 접근 (Database & ORM)

- **메인 DB**: MySQL (회원, 예약, 게시판 등 영구 데이터 저장)
- **인메모리 DB**: Redis (HttpSession 클러스터링 및 5분 예약 선점 Lock TTL 관리)
- **ORM (객체 관계 매핑)**: Spring Data JPA
- 기본 CRUD는 `JpaRepository` 활용으로 개발 생산성 극대화.
- 연관 관계는 지연 로딩(`FetchType.LAZY`)을 기본으로 설정.
- N+1 성능 문제가 발생하는 조회 구간에서는 JQPL의 `fetch join`을 사용하여 쿼리 최적화.

### 3. 프론트엔드 및 템플릿 (Frontend & View)

- **템플릿 엔진**: Mustache
- 로직이 없는(Logic-less) 템플릿의 장점을 살려 뷰와 서버 로직을 명확히 분리.
- 헤더, 푸터, 네비게이션 바 등은 Partial(`{{> layout/header}}`) 기능을 사용해 모듈화.

- **UI 프레임워크**: Bootstrap 5 + Vanilla CSS (혼합)
- Bootstrap의 Grid 시스템과 유틸리티 클래스로 뼈대를 빠르게 구성.
- `common.css` 및 페이지별 개별 CSS로 세밀한 디자인 통일성(Forest Haven 테마) 부여.

### 4. 보안 및 유효성 검사 (Security & Validation)

- **인증/인가**: `HttpSession` + Spring `HandlerInterceptor`
- 관리자(`/admin/**`), 마이페이지(`/mypage/**`) 등의 접근을 URL 기반으로 철저히 차단 및 검증.

- **데이터 검증 (Hybrid Validation)**:
- **1차 (프론트엔드)**: HTML5 속성(`required`, `pattern`) 및 JavaScript를 통한 즉각적인 입력값 피드백 (UX 향상).
- **2차 (백엔드)**: Spring Boot의 `@Valid`와 `BindingResult`를 사용한 서버단 무결성 검증. 검증 실패 시 기존 폼(Mustache)으로 에러 메시지 반환.

- **비밀번호 암호화**: Spring Security의 `BCryptPasswordEncoder`를 활용한 단방향 해시 저장. (Security 전체 필터 체인 대신 암호화 모듈만 제한적 사용 권장)

### 5. 외부 API 및 스토리지 (Integration & Storage)

- **결제 연동**: 포트원 (PortOne, 구 아임포트) V2 연동
- 단일 API로 신용카드, 카카오페이 등 다양한 결제 수단 통합 구현.

- **파일 스토리지**: 로컬 파일 시스템 (Local Storage)
- 리뷰 이미지, Q&A 첨부파일, 갤러리 사진 등은 서버 내 지정된 디렉토리(`/src/main/resources/static/uploads` 또는 외부 마운트 폴더)에 저장. DB에는 텍스트 형태의 URL 경로만 저장.

---
