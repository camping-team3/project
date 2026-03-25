# [기술 학습 보고서] 블랙리스트 시스템 메커니즘 및 로직 상세 분석

**작성자:** 김현지  
**날짜:** 2026-03-25  
**주제:** 강제 탈퇴 사유 저장 및 로그인 차단 피드백 시스템의 원리 학습

---

## 1. 시스템 핵심 흐름 (Core Workflow)

이 시스템은 **[관리자의 액션] → [DB 영속화] → [로그인 시 검증]**의 3단계로 작동합니다.

### Step 1: 관리자의 강제 탈퇴 처리 (Admin Action)
- **UI (`list.mustache`)**: 관리자가 리뷰 목록에서 부적절한 유저를 발견하고 `강제탈퇴` 버튼을 누릅니다. 이때 `SweetAlert2`가 뜨면서 **"탈퇴 사유"**를 입력받습니다.
- **Controller (`AdminController`)**: 입력받은 `userId`와 `reason`(사유)을 서버로 전달합니다.
- **Service (`UserService.expelUser`)**:
  1. 해당 유저의 상태(`status`)를 `ANONYMOUS`(탈퇴/마스킹)로 변경하여 일반적인 활동을 막습니다.
  2. **핵심**: 별도의 테이블인 `blacklist_tb`에 해당 유저의 `username`과 관리자가 직접 적은 `reason`을 저장합니다.

### Step 2: DB 저장 구조 (Persistence)
- **`user_tb`**: 유저의 상태만 바뀝니다. 유저 데이터 자체가 삭제되는 것이 아니므로 나중에 복구하거나 통계를 낼 때 유리합니다.
- **`blacklist_tb`**: 유저와 물리적 관계가 끊어진 **독립된 테이블**입니다. 유저가 탈퇴하더라도 "왜 차단되었는지" 기록은 영구적으로 남습니다.

### Step 3: 지능형 로그인 차단 (Login Feedback)
- 유저가 로그인을 시도할 때, `UserService.login` 메서드가 실행됩니다.
- **최우선 순위 체크**: 아이디/비밀번호를 맞추기 전, 가장 먼저 `blacklist_tb`에서 이 유저의 아이디가 있는지 조회합니다.
- **맞춤형 메시지 출력**: 블랙리스트에 있다면, 저장된 `reason`을 꺼내와서 `Exception400` 에러 메시지에 담아 던집니다.
  > "귀하는 **[관리자가 입력한 사유]**로 인해 이용이 제한되었습니다."

---

## 2. 공부할 포인트 (Learning Points)

### ① 도메인 격리 (Domain Isolation)
왜 `user_tb`에 `reason` 컬럼을 만들지 않고 별도의 `blacklist_tb`를 만들었을까요?
- **확장성**: 나중에 블랙리스트 해제 기능, 차단 기간 설정(`expired_at`) 등을 추가할 때 `user_tb`를 건드리지 않고도 쉽게 기능을 확장할 수 있습니다.
- **데이터 보존**: 유저가 완전히 삭제(Hard Delete)되더라도, 차단 기록은 남겨두어 동일한 아이디로 재가입하는 것을 방지할 수 있습니다.

### ② 예외 처리와 피드백 (Exception & Alert)
- 백엔드에서 `throw new Exception400("메시지")`를 던지면, 프론트엔드의 공통 에러 핸들러가 이를 가로채서 `alert()` 창으로 보여줍니다.
- 이를 통해 서버의 복잡한 로직 결과가 사용자에게 친절한 문구로 변환되는 과정을 이해할 수 있습니다.

---

## 3. 로직 확인용 코드 스니펫 (Code Review)

### [Service] 블랙리스트 저장 로직
```java
@Transactional
public void expelUser(Long id, String reason) {
    User user = userRepository.findById(id).orElseThrow(...);
    user.updateStatus(UserStatus.ANONYMOUS); // 유저 상태 변경

    // 블랙리스트 엔티티 생성 및 저장
    Blacklist blacklist = Blacklist.builder()
            .username(user.getUsername())
            .reason(reason != null ? reason : "관리자에 의한 강제 탈퇴")
            .build();
    blacklistRepository.save(blacklist);
}
```

### [Service] 로그인 시 사유 출력 로직
```java
public UserResponse.LoginDTO login(UserRequest.LoginDTO request) {
    // 블랙리스트 테이블에서 먼저 찾아서 사유를 에러 메시지에 포함!
    blacklistRepository.findByUsername(request.getUsername()).ifPresent(b -> {
        throw new Exception400("귀하는 다음 사유로 이용이 제한되었습니다: " + b.getReason());
    });
    // ... 일반 로그인 체크
}
```

---

## 4. 직접 테스트해보기 (Verification)

1. **테스트 데이터 확인**: 현재 `data.sql`에 `baduser`라는 아이디가 블랙리스트에 들어있습니다.
2. **로그인 시도**: 로그인 화면에서 아이디 `baduser`, 비밀번호 아무거나 입력 후 로그인을 눌러보세요.
3. **결과 확인**: "불법 광고 및 커뮤니티 가이드 반복 위반..." 이라는 메시지가 팝업으로 뜨는지 확인합니다.

---
**보고서 요약:** 이 시스템은 관리자의 의도(사유)를 **영구 저장소**에 보관했다가, 유저가 다시 접근할 때 **가장 먼저 꺼내어 보여주는** 보안 로직의 정석을 따르고 있습니다.
