# Phase 3 Reservation 도메인 확장 (Task 2: 예약 상태 자동 업데이트 스케줄러 구현)

**Date**: 2026-03-19
**Reporter**: parkcoding
**Task**: 2-2. 예약 상태 자동 업데이트 스케줄러 구현 및 학습 가이드

## 1. 전체 소스 코드 및 상세 설명

### 1-1. 설정 활성화 (ErpApplication.java)
시스템 전체에서 "정해진 시간에 일을 시키는 기능"을 켜는 스위치 역할을 합니다.
```java
@EnableScheduling // [학습포인트 1] 스케줄링 기능을 활성화합니다. 이 어노테이션이 없으면 @Scheduled가 작동하지 않습니다.
@EnableJpaAuditing
@SpringBootApplication
public class ErpApplication { ... }
```

### 1-2. 데이터 조회 기능 추가 (ReservationRepository.java)
DB에서 특정 조건(상태 + 날짜)에 맞는 데이터를 골라내는 능력을 추가합니다.
```java
public interface ReservationRepository extends JpaRepository<Reservation, Long> {
    /**
     * [학습포인트 2] 쿼리 메서드 (Query Method)
     * findByStatusAndCheckOutBefore: "상태(Status)가 일치하고(&) 체크아웃(CheckOut)이 특정 날짜보다 이전(Before)인" 데이터를 찾아라!
     * 스프링 데이터 JPA가 메서드 이름만 보고 자동으로 SQL 쿼리를 생성해줍니다.
     */
    List<Reservation> findByStatusAndCheckOutBefore(ReservationStatus status, LocalDate date);
}
```

### 1-3. 자동 관리자 생성 (ReservationScheduler.java)
실제로 밤마다 명부를 확인하고 도장을 찍는 '일꾼' 클래스입니다.
```java
@Slf4j // 로그(기록)를 남기기 위한 도구
@Component // 스프링이 이 객체를 직접 만들고 관리하도록 등록
@RequiredArgsConstructor // Repository를 자동으로 연결(주입)받기 위한 도구
public class ReservationScheduler {

    private final ReservationRepository reservationRepository;

    /**
     * [학습포인트 3] 서버 기동 시 즉시 실행
     * @EventListener(ApplicationReadyEvent.class): "서버가 완전히 켜져서 준비가 끝난 순간" 이 메서드를 실행하라는 뜻입니다.
     * 학원 실습처럼 서버를 껐다 켰다 하는 환경에서, 꺼져 있을 때 밀린 작업을 처리하기 위해 꼭 필요합니다.
     */
    @EventListener(ApplicationReadyEvent.class)
    public void onStartUpdate() {
        log.info("--- 서버 기동: 예약 상태 체크 시작 ---");
        autoUpdateReservationStatus();
    }

    /**
     * [학습포인트 4] 정기적인 자동 실행 (스케줄러)
     * @Scheduled(cron = "0 0 0 * * *"): 매일 0시 0분 0초에 실행하라는 알람 설정입니다.
     * @Transactional: DB 작업을 안전하게 묶어줍니다. 중간에 에러가 나면 모든 변경사항을 취소(Rollback)합니다.
     */
    @Transactional
    @Scheduled(cron = "0 0 0 * * *")
    public void autoUpdateReservationStatus() {
        LocalDate today = LocalDate.now(); // 오늘 날짜
        
        // 1. 상태가 '확정'이고, 체크아웃이 '오늘보다 이전'인 과거 예약들을 다 가져옵니다.
        List<Reservation> expiredReservations = reservationRepository.findByStatusAndCheckOutBefore(
                ReservationStatus.CONFIRMED, today
        );

        // 2. 하나씩 꺼내서 상태를 '이용 완료(COMPLETED)'로 바꿉니다.
        for (Reservation reservation : expiredReservations) {
            reservation.updateStatus(ReservationStatus.COMPLETED); // 엔티티 내부 메서드 호출
            log.info("예약 ID: {} 상태 변경 완료", reservation.getId());
        }
    }
}
```

## 2. 핵심 기술 및 공부가 필요한 개념 (Deep Dive)

### ① @Scheduled와 Cron 표현식
- **개념**: 특정한 시간 주기로 메서드를 실행할 때 사용합니다.
- **공부할 점**: `cron` 속성에 들어가는 6개의 숫자 의미 (`초 분 시 일 월 요일`). 예를 들어 `0 0/30 * * * *`는 30분마다 실행한다는 뜻입니다.

### ② 애플리케이션 이벤트 (@EventListener)
- **개념**: 스프링 시스템 내에서 일어나는 특정한 '사건(Event)'에 반응하는 기능입니다.
- **공부할 점**: `ApplicationReadyEvent` 외에도 서버가 꺼질 때(`ContextClosedEvent`) 등 다양한 사건에 맞춰 로직을 짤 수 있습니다.

### ③ JPA 쿼리 메서드 규칙
- **개념**: 인터페이스에 이름만 잘 지으면 JPA가 SQL을 대신 짜주는 마법 같은 기능입니다.
- **공부할 점**: `Before`, `After`, `Between`, `Containing`, `IsNull` 등 이름 뒤에 붙는 키워드들에 따라 쿼리가 어떻게 변하는지 공부하면 SQL을 직접 짤 일이 거의 없어집니다.

### ④ 더티 체킹 (Dirty Checking)
- **개념**: 이번 코드에서 `save()`를 따로 호출하지 않았는데도 DB가 업데이트되는 이유입니다.
- **공부할 점**: `@Transactional` 안에서 엔티티 객체의 값만 바꿔두면, 메서드가 끝날 때 JPA가 "어? 데이터가 바뀌었네?" 하고 알아서 `UPDATE` 쿼리를 날려줍니다. 이를 '변경 감지(Dirty Checking)'라고 합니다.

## 3. 비유로 보는 작업 내용
기존에는 관리자가 매일 아침 출근해서 "어제 누가 퇴실했지?" 하고 명부를 일일이 확인하며 도장을 찍어야 했다면, 이제는 **'스마트 관리 시스템'**을 도입한 것과 같습니다. 이 시스템은 서버가 켜지는 순간(출근 직후) 혹은 매일 새벽 0시에 스스로 명부를 훑어보고, 기간이 지난 사람들에게 '이용 완료' 도장을 자동으로 쾅쾅 찍어줍니다. 이제 관리자는 명부 정리에 손을 뗄 수 있게 되었습니다.
