-- ==========================================================
-- 1. 회원 정보 (user_tb)
-- 역할: 서비스 이용자 및 관리자 계정 정보 관리
-- Enum [role]: ADMIN(관리자), USER(일반회원)
-- Enum [status]: ACTIVE(활동 중), ANONYMOUS(탈퇴/익명화됨)
-- ==========================================================
INSERT INTO user_tb (username, password, name, email, phone, role, status, created_at) VALUES 
('admin', '1234', '관리자', 'admin@camping.com', '010-1111-1111', 'ADMIN', 'ACTIVE', NOW()),
('ssar', '1234', '박한별', 'ssar@nate.com', '010-2222-2222', 'USER', 'ACTIVE', NOW()),
('cos', '1234', '홍길동', 'cos@nate.com', '010-3333-3333', 'USER', 'ACTIVE', NOW()),
('love', '1234', '강사랑', 'love@nate.com', '010-4444-4444', 'USER', 'ACTIVE', NOW()),
('guest', '1234', '이게스트', 'guest@nate.com', '010-5555-5555', 'USER', 'ACTIVE', NOW()),
('olduser', '1234', '탈퇴사용자', 'old@nate.com', '010-6666-6666', 'USER', 'ANONYMOUS', NOW());


-- ==========================================================
-- 2. 구역 정보 (zone_tb)
-- 역할: 캠핑장의 대분류 구역 및 시즌별 기본 요금 설정
-- normal_price: 비수기/평시 1박 요금
-- peak_price: 성수기/준성수기 1박 요금
-- ==========================================================
INSERT INTO zone_tb (name, normal_price, peak_price, created_at) VALUES 
('A구역(오토캠핑)', 50000, 80000, NOW()),
('B구역(글램핑)', 150000, 250000, NOW()),
('C구역(카라반)', 120000, 200000, NOW());


-- ==========================================================
-- 3. 사이트 정보 (site_tb)
-- 역할: 구역 내 실제 예약 가능한 개별 캠핑 사이트 위치
-- zone_id: 소속 구역 참조 (FK)
-- max_people: 해당 사이트의 최대 수용 가능 인원
-- ==========================================================
INSERT INTO site_tb (zone_id, site_name, max_people, created_at) VALUES 
(1, 'A-1', 4, NOW()), (1, 'A-2', 4, NOW()), (1, 'A-3', 6, NOW()),
(2, 'B-1', 2, NOW()), (2, 'B-2', 4, NOW()),
(3, 'C-1', 4, NOW());


-- ==========================================================
-- 4. 예약 정보 (reservation_tb)
-- 역할: 고객의 사이트 예약 일정 및 금액 관리
-- Enum [status]: 
--   - PENDING: 예약 신청(결제 대기)
--   - CONFIRMED: 예약 확정(결제 완료)
--   - CANCEL_REQ: 사용자 취소 요청(관리자 승인 대기)
--   - CANCEL_COMP: 취소 승인 완료(환불 처리됨)
-- ==========================================================
INSERT INTO reservation_tb (user_id, site_id, check_in, check_out, total_price, status, created_at) VALUES 
(2, 1, '2026-03-01', '2026-03-03', 100000, 'CONFIRMED', NOW()), -- 이용완료(과거)
(3, 4, '2026-03-10', '2026-03-12', 300000, 'CONFIRMED', NOW()), -- 현재 이용중
(4, 2, '2026-04-01', '2026-04-03', 100000, 'CONFIRMED', NOW()), -- 미래 예약
(5, 5, '2026-05-01', '2026-05-02', 150000, 'CANCEL_REQ', NOW()), -- 취소 요청 중
(2, 6, '2026-02-01', '2026-02-03', 240000, 'CANCEL_COMP', NOW()); -- 취소 완료된 건


-- ==========================================================
-- 5. 결제 정보 (payment_tb)
-- 역할: 포트원 API 연동을 통한 실제 결제 성공 이력
-- imp_uid: 포트원 결제 고유 번호
-- amount: 실제 결제된 금액
-- status: 결제 상태 (PAID, CANCELLED 등)
-- ==========================================================
INSERT INTO payment_tb (reservation_id, imp_uid, amount, status, pay_date, created_at) VALUES 
(1, 'imp_111111', 100000, 'PAID', '2026-02-20', NOW()),
(2, 'imp_222222', 300000, 'PAID', '2026-03-05', NOW()),
(3, 'imp_333333', 100000, 'PAID', '2026-03-10', NOW()),
(4, 'imp_444444', 150000, 'PAID', '2026-03-11', NOW());


-- ==========================================================
-- 6. 환불 정보 (refund_tb)
-- 역할: 예약 취소 시 위약금 제외 후 환불된 상세 이력
-- refund_amount: 실제 고객에게 환불된 금액
-- cancelled_at: 관리자가 취소 승인을 한 일시
-- ==========================================================
INSERT INTO refund_tb (reservation_id, reason, refund_amount, cancelled_at, created_at) VALUES 
(5, '개인 사정으로 인한 일정 변경 불가', 240000, NOW(), NOW());


-- ==========================================================
-- 7. 공지사항 (notice_tb)
-- 역할: 관리자가 전체 사용자에게 전달하는 공지글
-- ==========================================================
INSERT INTO notice_tb (title, content, created_at) VALUES 
('2026년 봄 시즌 정식 오픈 안내', '안녕하세요. 캠핑장이 3월부터 정식 오픈합니다. 많은 이용 바랍니다.', NOW()),
('성수기 요금 및 예약 제한 기간 안내', '7월 15일부터 8월 20일까지 성수기 요금이 적용되며 2박 이상 우선 예약됩니다.', NOW()),
('캠핑장 내 매너타임 준수 안내', '밤 10시부터는 매너타임입니다. 타인을 위해 고성방가를 자제해주세요.', NOW());


-- ==========================================================
-- 8. 포토 갤러리 (gallery_tb)
-- 역할: 캠핑장 시설 및 풍경 사진 홍보 게시판
-- ==========================================================
INSERT INTO gallery_tb (title, content, created_at) VALUES 
('A구역 산책로의 아침 풍경', '피톤치드 가득한 A구역의 산책길입니다.', NOW()),
('B구역 럭셔리 글램핑 텐트 내부', '호텔급 침구류와 개별 화장실을 갖춘 내부 모습입니다.', NOW());


-- ==========================================================
-- 9. Q&A 및 관리자 답변 (qna_tb, comment_tb)
-- 역할: 고객의 문의사항과 관리자의 공식 답변 관리
-- is_answered: 답변 완료 여부 (true/false)
-- ==========================================================
INSERT INTO qna_tb (user_id, title, content, is_answered, created_at) VALUES 
(2, '반려견 동반 입실이 가능한가요?', '10kg 미만 소형견 1마리와 동반하고 싶은데 A구역 가능할까요?', true, NOW()),
(3, '주차 공간이 사이트 바로 옆인가요?', '텐트 바로 옆에 차를 댈 수 있는지 궁금합니다.', false, NOW());

INSERT INTO comment_tb (qna_id, admin_id, content, created_at) VALUES 
(1, 1, '안녕하세요! A구역은 사이트당 1마리(소형견)에 한해 동반이 가능합니다. 목줄 착용은 필수입니다.', NOW());


-- ==========================================================
-- 10. 이용 후기 (review_tb)
-- 역할: 실제 이용 완료 고객의 별점 및 후기
-- rating: 별점 (1~5)
-- user_id, reservation_id: 작성자와 실제 이용 내역 참조 (FK)
-- ==========================================================
INSERT INTO review_tb (user_id, reservation_id, rating, content, created_at) VALUES 
(2, 1, 5, '화장실과 개수대가 너무 깨끗해서 좋았어요! 사장님도 정말 친절하십니다.', NOW());


-- ==========================================================
-- 11. 이미지 관리 (image_tb)
-- 역할: 갤러리 및 리뷰에 첨부된 다중 이미지 파일 정보
-- gallery_id: 갤러리 게시글 참조 (Nullable)
-- review_id: 리뷰 게시글 참조 (Nullable)
-- ==========================================================
INSERT INTO image_tb (gallery_id, review_id, file_path, file_name, created_at) VALUES 
(1, null, '/upload/gallery/', 'morning_forest_01.jpg', NOW()),
(1, null, '/upload/gallery/', 'morning_forest_02.jpg', NOW()),
(2, null, '/upload/gallery/', 'glamping_inside.jpg', NOW()),
(null, 1, '/upload/review/', 'my_camping_pic.jpg', NOW());
