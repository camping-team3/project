# Report: 마이페이지 홈 데이터 연동 및 예약 내역 리스트 구현

- **Date**: 2026-03-24
- **Reporter**: parkcoding (Gemini CLI)
- **Task**: 마이페이지 홈(/mypage) 화면 구현 및 예약 데이터 연동

## 📝 작업 요약
사용자가 자신의 예약 현황을 한눈에 파악할 수 있도록 마이페이지 홈 화면을 완성했습니다. '진행 중인 예약' 통계와 '최근 예약 내역' 리스트를 백엔드 데이터와 실시간으로 연동했습니다.

## 🛠️ 변경 사항
1. **Repository (ReservationRepository.java)**:
   - 특정 유저의 상태별 예약 건수를 집계하는 `countByUserIdAndStatusIn` 추가.
   - 진행 중인 예약과 최근 1개월 내의 예약 내역을 통합 조회하는 `findRecentMypageReservations` 쿼리 메서드 구현.
2. **Service (ReservationService.java)**:
   - 마이페이지 홈용 통합 DTO(`MypageHomeDTO`)를 생성하고 반환하는 `getMypageHomeData` 메서드 구현.
3. **Controller (UserController.java)**:
   - `/mypage` 진입 시 `ReservationService`를 호출하여 필요한 데이터를 Model에 주입.
   - 데이터 유무에 따른 `isNoReservations` 플래그 추가.
4. **View (home.mustache)**:
   - 상단 요약 카드 데이터 치환.
   - 최근 예약 내역 리스트 루프 구현 및 상태 배지 적용.
   - 내역 부재 시 '예약 유도' Empty State 디자인 적용.

## ✅ 검증 결과
- **통계 정확도**: 사용자의 현재 예약 상태(대기, 확정, 변경/취소 요청)가 정확히 카운트되어 표시됩니다.
- **목록 필터링**: 진행 중인 예약은 모두 표시되며, 완료/취소된 예약은 최근 1개월 이내의 건만 목록에 나타납니다.
- **UI/UX**: 내역 클릭 시 해당 예약의 상세 페이지로 정상 이동하며, 내역이 없을 때의 안내 문구가 친절하게 표시됩니다.
