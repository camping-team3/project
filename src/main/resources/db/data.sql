-- ==========================================================
-- 1. 회원 정보 (user_tb) - ID 명시
-- ==========================================================
INSERT INTO user_tb (id, username, password, name, email, phone, role, status, created_at) VALUES 
(1, 'admin', '1234', '관리자', 'admin@camping.com', '010-1111-1111', 'ADMIN', 'ACTIVE', NOW()),
(2, 'ssar', '1234', '박한별', 'ssar@nate.com', '010-2222-2222', 'USER', 'ACTIVE', NOW()),
(3, 'cos', '1234', '홍길동', 'cos@nate.com', '010-3333-3333', 'USER', 'ACTIVE', NOW()),
(4, 'love', '1234', '강사랑', 'love@nate.com', '010-4444-4444', 'USER', 'ACTIVE', NOW()),
(5, 'guest', '1234', '이게스트', 'guest@nate.com', '010-5555-5555', 'USER', 'ACTIVE', NOW()),
(6, 'olduser', '1234', '탈퇴사용자', 'old@nate.com', '010-6666-6666', 'USER', 'ANONYMOUS', NOW());

INSERT INTO user_tb (id, username, password, name, email, phone, role, status, created_at) VALUES 
(7, 'user01', '1234', '김철수', 'user01@nate.com', '010-1001-1001', 'USER', 'ACTIVE', DATEADD(DAY, -20, NOW())),
(8, 'user02', '1234', '이영희', 'user02@nate.com', '010-1002-1002', 'USER', 'ACTIVE', DATEADD(DAY, -19, NOW())),
(9, 'user03', '1234', '박지성', 'user03@nate.com', '010-1003-1003', 'USER', 'ACTIVE', DATEADD(DAY, -18, NOW())),
(10, 'user04', '1234', '손흥민', 'user04@nate.com', '010-1004-1004', 'USER', 'ACTIVE', DATEADD(DAY, -17, NOW())),
(11, 'user05', '1234', '김연아', 'user05@nate.com', '010-1005-1005', 'USER', 'ACTIVE', DATEADD(DAY, -16, NOW())),
(12, 'user06', '1234', '이강인', 'user06@nate.com', '010-1006-1006', 'USER', 'ACTIVE', DATEADD(DAY, -15, NOW())),
(13, 'user07', '1234', '안정환', 'user07@nate.com', '010-1007-1007', 'USER', 'ACTIVE', DATEADD(DAY, -14, NOW())),
(14, 'user08', '1234', '박찬호', 'user08@nate.com', '010-1008-1008', 'USER', 'ACTIVE', DATEADD(DAY, -13, NOW())),
(15, 'user09', '1234', '서장훈', 'user09@nate.com', '010-1009-1009', 'USER', 'ACTIVE', DATEADD(DAY, -12, NOW())),
(16, 'user10', '1234', '유재석', 'user10@nate.com', '010-1010-1010', 'USER', 'ACTIVE', DATEADD(DAY, -11, NOW())),
(17, 'user11', '1234', '강호동', 'user11@nate.com', '010-1011-1011', 'USER', 'ACTIVE', DATEADD(DAY, -10, NOW())),
(18, 'user12', '1234', '신동엽', 'user12@nate.com', '010-1012-1012', 'USER', 'ACTIVE', DATEADD(DAY, -9, NOW())),
(19, 'user13', '1234', '이수근', 'user13@nate.com', '010-1013-1013', 'USER', 'ACTIVE', DATEADD(DAY, -8, NOW())),
(20, 'user14', '1234', '은지원', 'user14@nate.com', '010-1014-1014', 'USER', 'ACTIVE', DATEADD(DAY, -7, NOW())),
(21, 'user15', '1234', '김종민', 'user15@nate.com', '010-1015-1015', 'USER', 'ACTIVE', DATEADD(DAY, -6, NOW()));

-- ==========================================================
-- 2. 구역 정보 (zone_tb) - ID 명시
-- ==========================================================
INSERT INTO zone_tb (id, name, normal_price, peak_price, base_people, extra_person_fee, avg_rating, review_count, created_at) VALUES 
(1, 'A구역(오토캠핑)', 50000, 80000, 2, 10000, 0.0, 0, NOW()),
(2, 'B구역(글램핑)', 150000, 250000, 2, 20000, 0.0, 0, NOW()),
(3, 'C구역(카라반)', 120000, 200000, 2, 15000, 0.0, 0, NOW());

-- ==========================================================
-- 3. 사이트 정보 (site_tb) - ID 명시 및 정확한 FK 참조
-- ==========================================================
INSERT INTO site_tb (id, zone_id, site_name, max_people, is_available, avg_rating, review_count, created_at) VALUES 
(1, 1, 'A-1', 4, true, 0.0, 0, NOW()), 
(2, 1, 'A-2', 4, true, 0.0, 0, NOW()), 
(3, 1, 'A-3', 6, true, 0.0, 0, NOW()),
(4, 2, 'B-1', 2, true, 0.0, 0, NOW()), 
(5, 2, 'B-2', 4, false, 0.0, 0, NOW()), 
(6, 3, 'C-1', 4, true, 0.0, 0, NOW());

-- ==========================================================
-- 4. 성수기 정보 (season_tb)
-- ==========================================================
INSERT INTO season_tb (id, name, start_date, end_date, created_at) VALUES 
(1, '2026 여름 성수기', '2026-07-01', '2026-08-31', NOW()),
(2, '2026 단풍 시즌', '2026-10-15', '2026-11-15', NOW());

-- ==========================================================
-- 5. 예약 정보 (reservation_tb) - ID 명시 및 상태 동기화
-- ==========================================================
INSERT INTO reservation_tb (id, user_id, site_id, check_in, check_out, total_price, people_count, visitor_name, visitor_phone, status, created_at) VALUES 
(1, 2, 1, '2026-03-01', '2026-03-03', 100000, 2, '박한별', '010-2222-2222', 'COMPLETED', NOW()),
(2, 3, 4, '2026-03-10', '2026-03-12', 300000, 2, '홍길동', '010-3333-3333', 'COMPLETED', NOW()),
(3, 4, 2, '2026-04-01', '2026-04-03', 120000, 4, '강사랑', '010-4444-4444', 'CONFIRMED', NOW()),
(4, 5, 5, '2026-05-01', '2026-05-02', 150000, 2, '이게스트', '010-5555-5555', 'CANCEL_REQ', NOW()),
(5, 2, 6, '2026-02-01', '2026-02-03', 240000, 2, '박한별', '010-2222-2222', 'CANCEL_COMP', NOW());

INSERT INTO reservation_tb (id, user_id, site_id, check_in, check_out, total_price, people_count, visitor_name, visitor_phone, status, created_at) VALUES 
(6, 7, 1, '2026-06-01', '2026-06-03', 100000, 2, '김철수', '010-1001-1001', 'CONFIRMED', DATEADD(DAY, -10, NOW())),
(7, 8, 2, '2026-06-05', '2026-06-07', 100000, 2, '이영희', '010-1002-1002', 'CONFIRMED', DATEADD(DAY, -9, NOW())),
(8, 9, 3, '2026-06-10', '2026-06-12', 100000, 2, '박지성', '010-1003-1003', 'PENDING', DATEADD(DAY, -8, NOW())),
(9, 10, 4, '2026-06-15', '2026-06-17', 300000, 2, '손흥민', '010-1004-1004', 'CONFIRMED', DATEADD(DAY, -7, NOW())),
(10, 11, 5, '2026-06-20', '2026-06-22', 300000, 2, '김연아', '010-1005-1005', 'CANCEL_REQ', DATEADD(DAY, -6, NOW())),
(11, 12, 6, '2026-06-25', '2026-06-27', 240000, 2, '이강인', '010-1006-1006', 'CONFIRMED', DATEADD(DAY, -5, NOW())),
(12, 13, 1, '2026-07-01', '2026-07-03', 160000, 4, '안정환', '010-1007-1007', 'CONFIRMED', DATEADD(DAY, -4, NOW())),
(13, 14, 2, '2026-07-05', '2026-07-07', 100000, 2, '박찬호', '010-1008-1008', 'PENDING', DATEADD(DAY, -3, NOW())),
(14, 15, 3, '2026-07-10', '2026-07-12', 100000, 2, '서장훈', '010-1009-1009', 'CONFIRMED', DATEADD(DAY, -2, NOW())),
(15, 7, 4, '2026-07-15', '2026-07-17', 500000, 4, '유재석', '010-1010-1010', 'CONFIRMED', DATEADD(DAY, -1, NOW())),
(16, 8, 5, '2026-07-20', '2026-07-22', 300000, 2, '강호동', '010-1011-1011', 'CANCEL_COMP', NOW()),
(17, 9, 6, '2026-07-25', '2026-07-27', 240000, 2, '신동엽', '010-1012-1012', 'CONFIRMED', NOW()),
(18, 10, 1, '2026-08-01', '2026-08-03', 160000, 4, '이수근', '010-1013-1013', 'CONFIRMED', NOW()),
(19, 11, 2, '2026-08-05', '2026-08-07', 100000, 2, '은지원', '010-1014-1014', 'PENDING', NOW()),
(20, 12, 3, '2026-08-10', '2026-08-12', 100000, 2, '김종민', '010-1015-1015', 'CONFIRMED', NOW());

-- ==========================================================
-- 6. 결제 및 환불 정보
-- ==========================================================
INSERT INTO payment_tb (id, reservation_id, imp_uid, amount, status, pay_date, created_at) VALUES 
(1, 1, 'imp_111111', 100000, 'PAID', '2026-02-20', NOW()),
(2, 2, 'imp_222222', 300000, 'PAID', '2026-03-05', NOW()),
(3, 3, 'imp_333333', 120000, 'PAID', '2026-03-10', NOW()),
(4, 4, 'imp_444444', 150000, 'PAID', '2026-03-11', NOW());

INSERT INTO refund_tb (id, reservation_id, reason, refund_amount, cancelled_at, created_at) VALUES
(1, 5, '개인 사정으로 인한 일정 변경 불가', 240000, NOW(), NOW());

-- ==========================================================
-- 7. 공지사항 (notice_tb)
-- ==========================================================
INSERT INTO notice_tb (id, title, content, is_top, created_at) VALUES 
(1, '2026년 봄 시즌 정식 오픈 안내', '정식 오픈합니다.', true, NOW()),
(2, '성수기 요금 안내', '성수기 요금이 적용됩니다.', false, NOW()),
(3, '매너타임 준수 안내', '밤 10시부터 매너타임입니다.', false, NOW());

-- ==========================================================
-- 8. Q&A 및 답변 (qna_tb, comment_tb)
-- ==========================================================
INSERT INTO qna_tb (id, user_id, title, content, category, hits, is_answered, created_at) VALUES 
(1, 2, '반려견 동반 입실?', '가능할까요?', 'FACILITY', 10, true, NOW()),
(2, 3, '주차 공간 문의', '텐트 옆 주차 가능한가요?', 'FACILITY', 5, false, NOW());

INSERT INTO comment_tb (id, qna_id, admin_id, content, created_at) VALUES 
(1, 1, 1, 'A구역만 소형견 가능합니다.', NOW());

-- ==========================================================
-- 9. 이용 후기 (review_tb) - 1예약 1리뷰 원칙 준수
-- ==========================================================
INSERT INTO review_tb (id, user_id, reservation_id, rating, content, created_at) VALUES 
(1, 2, 1, 5, '정말 좋았습니다. 다음에도 꼭 오고 싶어요!', NOW()),
(2, 3, 2, 4, '조용하고 공기가 좋아서 힐링되었습니다.', NOW());


-- ==========================================================
-- 10. 갤러리 (gallery_tb) - 누락 데이터 추가
-- ==========================================================
INSERT INTO gallery_tb (id, title, category, shooting_date, content, view_count, created_at) VALUES 
(1, '안개 낀 숲속의 아침', '캠핑장 전경', '2026-03-01', '숲속 아침 풍경입니다.', 0, NOW()),
(2, '글램핑 내부 시설 공개', '캠핑장 전경', '2026-03-05', '깨끗하고 아늑합니다.', 0, NOW());

-- ==========================================================
-- 11. 이미지 관리 (image_tb)
-- 역할: 갤러리, 리뷰, 공지사항 및 구역/사이트 이미지 파일 정보
-- ==========================================================
INSERT INTO image_tb (id, gallery_id, review_id, notice_id, zone_id, site_id, file_path, file_name, created_at) VALUES 
(1, null, null, null, 1, null, '/upload/zone/', 'zone_a_main.jpg', NOW()),
(2, null, 1, null, null, null, '/upload/review/', 'my_pic.jpg', NOW()),
(3, 1, null, null, null, null, '/upload/gallery/', 'morning_forest_01.jpg', NOW()),
(4, 1, null, null, null, null, '/upload/gallery/', 'morning_forest_02.jpg', NOW()),
(5, 2, null, null, null, null, '/upload/gallery/', 'glamping_inside.jpg', NOW()),
(6, null, 1, null, null, null, '/upload/review/', 'my_camping_pic.jpg', NOW());
