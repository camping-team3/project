# BaseTimeEntity 유지 및 Enum 분리 보고서

**작성일**: 2026-03-12
**작업 브랜치**: dev

---

## 1. 변경 배경

### BaseTimeEntity 유지
`com.camping.erp.global.BaseTimeEntity`를 `@MappedSuperclass`로 정의하고, 모든 엔티티가 이를 상속받아 `createdAt`, `updatedAt` 필드를 공유하는 구조를 유지한다.

**유지 사유:**
- 12개 엔티티 모두 동일한 시간 필드를 필요로 하므로, 상속을 통한 중복 제거가 효과적
- `@EnableJpaAuditing` + `@MappedSuperclass` 조합은 Spring Data JPA의 표준 패턴
- 각 엔티티에서 반복적인 `@EntityListeners`, `@CreatedDate`, `@LastModifiedDate` 선언을 방지

### Enum 분리
`User.UserRole`, `User.UserStatus`, `Reservation.ReservationStatus` 등 내부 enum이 엔티티 클래스 안에 중첩 선언되어 있었다.

**분리 사유:**
- 다른 패키지에서 참조 시 `User.UserRole.ADMIN` 형태로 엔티티 의존이 생김
- enum만 필요한 곳에서도 엔티티 클래스 전체를 import해야 하는 구조
- 도메인 패키지 내 `enums` 하위 패키지로 분리하면 역할이 명확해짐

---

## 2. 변경 내역

### 2-1. BaseTimeEntity 구조

```java
// global/BaseTimeEntity.java
@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
@Getter
public abstract class BaseTimeEntity {
    @CreatedDate
    private LocalDateTime createdAt;

    @LastModifiedDate
    private LocalDateTime updatedAt;
}

// 각 엔티티에서 상속
public class User extends BaseTimeEntity { ... }
```

- 모든 엔티티(12개)가 `extends BaseTimeEntity`로 시간 필드 상속
- `@EnableJpaAuditing`은 `ErpApplication.java`에 선언

### 2-2. Enum 분리

| 기존 위치 | 새 위치 |
|-----------|---------|
| `User.UserRole` | `com.camping.erp.domain.user.enums.UserRole` |
| `User.UserStatus` | `com.camping.erp.domain.user.enums.UserStatus` |
| `Reservation.ReservationStatus` | `com.camping.erp.domain.reservation.enums.ReservationStatus` |

**import 변경이 필요했던 파일:**
- `AuthController.java`: `User.UserRole.ADMIN` → `UserRole.ADMIN`

---

## 3. 영향 범위

| 구분 | 상세 |
|------|------|
| DB 스키마 | 변경 없음 (컬럼명, 타입 동일) |
| data.sql | 변경 없음 (enum 값은 문자열로 삽입되므로 영향 없음) |
| JPA Auditing | 동작 동일 (`@EnableJpaAuditing` + `BaseTimeEntity` 조합 유지) |
| 빌드 | `compileJava` 성공 확인 |

---

## 4. 패키지 구조 (변경 후)

```
domain/
├── user/
│   ├── User.java
│   ├── UserRepository.java
│   ├── MypageController.java
│   └── enums/
│       ├── UserRole.java
│       └── UserStatus.java
├── reservation/
│   ├── Reservation.java
│   ├── ReservationController.java
│   └── enums/
│       └── ReservationStatus.java
├── ...
global/
├── BaseTimeEntity.java
├── auth/
├── config/
├── handler/
├── filter/
└── util/
```
