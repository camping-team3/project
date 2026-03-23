# 📋 Forest Haven ERP Project TODO

## 🎯 회원 정보 수정 기능 (Mypage Info Update)

- 마이페이지 내에서 사용자의 이름, 이메일, 전화번호를 수정할 수 있는 기능 구축
- 비밀번호 변경 기능을 포함하여 보안 강화를 위한 기존 비밀번호 확인 절차 구현
- 정보 수정 후 세션 정보를 동기화하여 즉각적인 사용자 경험 제공

---

## 🛠️ 개발 단계 및 태스크 (Implementation Plan)

### 1단계: DTO 및 엔티티 수정
- [x] **Task 1: UserRequest.UpdateDTO 추가 및 User 엔티티 업데이트 메서드 추가**
  - [x] `UserRequest` 내부에 `UpdateDTO` 정적 클래스 추가
  - [x] `User` 엔티티에 `updateInfo(name, email, phone)` 및 `updatePassword(newPassword)` 메서드 추가

### 2단계: 서비스 로직 구현
- [ ] **Task 2: UserService.update(id, request) 구현**
  - [ ] 기존 비밀번호 검증 (BCryptPasswordEncoder 활용)
  - [ ] 새 비밀번호 입력 시 일치 여부 확인 및 암호화 적용
  - [ ] 변경 감지(Dirty Checking)를 통한 정보 업데이트

### 3단계: 컨트롤러 구현
- [ ] **Task 3: UserController에 정보 수정 폼 이동 및 수정 처리 매핑 추가**
  - [ ] `GET /mypage/info-update-form`: 수정 폼으로 이동 (기존 정보 전달)
  - [ ] `POST /mypage/info-update`: 정보 수정 처리 및 세션 동기화

### 4단계: 템플릿 구현 및 연결
- [ ] **Task 4: info-update-form.mustache 생성 및 info.mustache 버튼 연결**
  - [ ] `src/main/resources/templates/mypage/info-update-form.mustache` 파일 생성
  - [ ] `src/main/resources/templates/mypage/info.mustache`의 수정 버튼 링크 연결
