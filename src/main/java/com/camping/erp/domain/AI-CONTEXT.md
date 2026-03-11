<!-- Parent: ../../../../../../../.ai/AI-CONTEXT.md -->

# domain/

## 목적
비즈니스 로직과 엔티티를 포함하는 핵심 도메인 레이어. 각 하위 패키지는 하나의 도메인 영역(Bounded Context)을 담당한다.

## 주요 하위 패키지
| 패키지 | 설명 |
|--------|------|
| `user/` | 회원 관리 및 인증 (User, Role, Status) |
| `site/` | 캠핑 사이트 및 요금 정책 관리 (Zone, Site) |
| `reservation/` | 예약 시스템 및 일정 관리 (Reservation) |
| `payment/` | 결제 연동 및 환불 이력 관리 (Payment, Refund) |
| `board/` | 커뮤니티 및 콘텐츠 관리 (Notice, Gallery, Qna, Review, Image) |

## AI 작업 지침
- **엔티티 설계**: 모든 엔티티는 `BaseTimeEntity`를 상속받으며 `@Getter`, `@NoArgsConstructor(access = AccessLevel.PROTECTED)`, `@Builder`를 사용한다.
- **연관 관계**: 모든 연관관계는 `FetchType.LAZY`를 기본으로 하며, FK 명칭은 `table_id` 형식을 따른다.
- **Service 레이어**: 비즈니스 로직은 `Service`에서 처리하며, `@Transactional(readOnly = true)`를 기본으로 사용한다.

## 의존성
- 내부: `global/_core` (공통 예외, 응답 규격, 시간 관리)
- 외부: Spring Data JPA, Lombok
