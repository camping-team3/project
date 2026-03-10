# 📑 ai-context.md (Project Source of Truth)

## 1. 프로젝트 현재 상태 (Current Milestone)

- **단계**: Phase 1 - 프로젝트 초기화 및 도메인 설계
- **진척도**: [||||--------] 35%
- **최근 업데이트**: ERD 설계 명세 확정 및 MVP 워크플로우 수립 완료
- **다음 목표**: JPA Entity 생성 및 연관관계 매핑

## 2. 프로젝트 맵 (File & Directory Manifest)

이 섹션은 실제 파일이 생성될 때마다 업데이트되어야 합니다.

### 2.1 Backend (`src/main/java/com/camping/erp`)

모든 백엔드 코드는 `com.camping.erp` 하위의 **도메인별 폴더**에 위치하며, 각 폴더 내에 해당 도메인의 Controller, Service, Repository, Entity를 모아서 관리한다.

- `domain/user/`: 회원 관련 (User, UserService, UserController, UserRepository)
- `domain/reservation/`: 예약 및 선점 락 관련 (Reservation, BookingService, ReservationRepository)
- `domain/site/`: 캠핑 사이트 및 구역 관리 관련 (Site, SiteService, SiteRepository)
- `domain/board/`: 공지사항, 갤러리, Q&A 관련 (Folder Created)
- `domain/payment/`: 결제 및 환불 승인 로직 관련 (Folder Created)
- `global/`: 공통 설정 및 유틸리티
  - `global/config/` : WebMvcConfig.java
  - `global/_core/` 시스템 핵심 인프라 (Technical Foundation)
    - `handler/`: 공통 예외 처리 (GlobalExceptionHandler)
    - `auth/`: 인증 관련(Interceptor)
    - `util/`: Resp.java

### 2.2 View (`src/main/resources/templates`)

- `layout/`: `header.mustache`, `footer.mustache` (공통 레이아웃)
- `main/`: 메인 화면 및 공용 페이지
- `index.mustache`: 메인 진입점 (layout 적용)
- `auth/`: 로그인, 회원가입 관련
- `res/`: 예약 프로세스 (Step 1~3)
- `mypage/`: 회원 전용 대시보드 및 내역
- `admin/`: 관리자 전용 대시보드 및 통계

## 3. 비즈니스 로직 매핑 테이블 (Functional Logic)

기존 HTML(01~37)과 신규 기능의 연결 고리입니다.

| 기능명        | 관련 Entity             | 핵심 로직                         | 기존 HTML Reference |
| ------------- | ----------------------- | --------------------------------- | ------------------- |
| **예약 선점** | `Room`, `Reservation`   | Redis TTL(5min) 기반 분산 락      | 21, 27              |
| **결제 처리** | `Payment`               | PortOne V2 서버사이드 검증        | 29                  |
| **취소 승인** | `Reservation`, `Refund` | 관리자 수동 승인 후 환불 연동     | 02, 05, 30          |
| **지연 평가** | N/A                     | 이용 일자 기준 실시간 상태 렌더링 | 04, 28              |

## 4. AI 코딩 가이드 (Coding Constraints)

- **응답 정책 (Response Strategy)**:
  - **Form 중심 개발**: AJAX 사용을 최소화하고, 대부분의 데이터 전송은 HTML `<form>` 태그를 통한 동기식 요청으로 처리한다.
  - **예외 처리**: 비즈니스 예외 발생 시 `GlobalExceptionHandler`를 통해 사용자에게 `alert` 창을 띄우고 `history.back()`을 수행하는 **Script 응답**을 기본으로 한다.
  - **AJAX 활용**: 중복 체크 등 페이지 이동 없이 처리가 필요한 경우에만 제한적으로 AJAX를 사용하며, 이때는 `Resp.java`를 활용하여 JSON 응답을 제공한다.
  - **PRG 패턴**: 상태 변경(`POST`) 성공 시 반드시 **Redirect**를 수행하여 중복 제출을 방지한다.
- **데이터 접근**: 모든 연관관계는 `LAZY`. 필요시 `fetch join` 명시.
- **보안**: `/admin/**` 접근 시 `AdminInterceptor` 통과 필수.
- **에러**: 결제 오류 시 `PaymentException` 발생 및 파일 로깅.

## 5. 작업 히스토리 (Work Log)

- `2026-03-10`: 
  - 세션 자동 초기화 루틴 도입 (`AI-GUIDE.md` 업데이트)
  - 백엔드 패키지 경로 오타 수정 (`caping` -> `camping`)
  - 도메인 폴더 구조 및 뷰 템플릿 누락 폴더 생성 완료
  - `workflow.md` 수립 (MVP 4단계 로드맵)
  - **Deep Interview**를 통한 ERD 설계 및 `.person/ERD.md` 명세 확정
  - **(Sync)**: 템플릿 폴더 구조 동기화 완료
  - **(Sync)**: `AI-CONTEXT.md` 상태 및 진척도 업데이트 (25% -> 35%)

---

## 🛠️ 운영 규칙: AI와 동기화하는 방법

새로운 개발 환경이나 세션에서 AI가 이 파일을 완벽히 활용하게 하려면 아래의 **'동기화 루틴'**을 지켜주세요.

1. **세션 시작 시**: Cursor 채팅창에 다음과 같이 입력합니다.

   > `@ai-context.md`를 읽고 현재 프로젝트의 진행 상황과 파일 구조, 코딩 규칙을 파악해줘. 이제 다음 단계인 [작업 내용]을 시작할 준비가 됐어?

2. **중요 파일 생성/수정 시**: AI에게 파일을 만들게 한 후 반드시 이렇게 지시하세요.

   > "방금 생성한 `ReservationController.java`와 관련된 내용을 `ai-context.md`의 **2.1 Backend**와 **3. 비즈니스 로직 매핑** 섹션에 업데이트해줘."

3. **작업 종료 시**: 퇴근 전이나 작업을 멈출 때 지시하세요.
   > "오늘 진행한 작업 내용을 `ai-context.md`의 **1. 프로젝트 현재 상태**와 **5. 작업 히스토리**에 요약해서 기록해줘."
