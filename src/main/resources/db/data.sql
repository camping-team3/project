-- ==========================================================
-- 0. 블랙리스트 (blacklist_tb)
-- ==========================================================
CREATE TABLE IF NOT EXISTS blacklist_tb (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(255) NOT NULL UNIQUE,
    reason VARCHAR(500) NOT NULL,
    admin_memo VARCHAR(255),
    created_at TIMESTAMP,
    updated_at TIMESTAMP
);

INSERT INTO blacklist_tb (username, reason, created_at, updated_at) VALUES ('baduser', '불법 광고 및 커뮤니티 가이드 반복 위반 (관리자 테스트용)', NOW(), NOW());

-- ==========================================================
-- 1. 회원 정보 (user_tb)
-- ==========================================================
INSERT INTO user_tb (id, username, password, name, email, phone, role, status, penalty_count, created_at) VALUES 
(1, 'admin', '1234', '관리자', 'admin@camping.com', '010-1111-1111', 'ADMIN', 'ACTIVE', 0, NOW()),
(2, 'ssar', '1234', '박한별', 'ssar@nate.com', '010-2222-2222', 'USER', 'ACTIVE', 3, NOW()),
(3, 'cos', '1234', '홍길동', 'cos@nate.com', '010-3333-3333', 'USER', 'ACTIVE', 1, NOW()),
(4, 'love', '1234', '강사랑', 'love@nate.com', '010-4444-4444', 'USER', 'ACTIVE', 0, NOW()),
(5, 'guest', '1234', '이게스트', 'guest@nate.com', '010-5555-5555', 'USER', 'ACTIVE', 0, NOW()),
(6, 'olduser', '1234', '탈퇴사용자', 'old@nate.com', '010-6666-6666', 'USER', 'ANONYMOUS', 0, NOW()),
(7, 'user01', '1234', '김철수', 'user01@nate.com', '010-1001-1001', 'USER', 'ACTIVE', 0, DATEADD(DAY, -20, NOW())),
(8, 'user02', '1234', '이영희', 'user02@nate.com', '010-1002-1002', 'USER', 'ACTIVE', 0, DATEADD(DAY, -19, NOW())),
(9, 'user03', '1234', '박지성', 'user03@nate.com', '010-1003-1003', 'USER', 'ACTIVE', 0, DATEADD(DAY, -18, NOW())),
(10, 'user04', '1234', '손흥민', 'user04@nate.com', '010-1004-1004', 'USER', 'ACTIVE', 0, DATEADD(DAY, -17, NOW())),
(11, 'user05', '1234', '김연아', 'user05@nate.com', '010-1005-1005', 'USER', 'ACTIVE', 0, DATEADD(DAY, -16, NOW())),
(12, 'user06', '1234', '이강인', 'user06@nate.com', '010-1006-1006', 'USER', 'ACTIVE', 0, DATEADD(DAY, -15, NOW())),
(13, 'user07', '1234', '안정환', 'user07@nate.com', '010-1007-1007', 'USER', 'ACTIVE', 0, DATEADD(DAY, -14, NOW())),
(14, 'user08', '1234', '박찬호', 'user08@nate.com', '010-1008-1008', 'USER', 'ACTIVE', 0, DATEADD(DAY, -13, NOW())),
(15, 'user09', '1234', '서장훈', 'user09@nate.com', '010-1009-1009', 'USER', 'ACTIVE', 0, DATEADD(DAY, -12, NOW())),
(16, 'user10', '1234', '유재석', 'user10@nate.com', '010-1010-1010', 'USER', 'ACTIVE', 0, DATEADD(DAY, -11, NOW())),
(17, 'user11', '1234', '강호동', 'user11@nate.com', '010-1011-1011', 'USER', 'ACTIVE', 0, DATEADD(DAY, -10, NOW())),
(18, 'user12', '1234', '신동엽', 'user12@nate.com', '010-1012-1012', 'USER', 'ACTIVE', 0, DATEADD(DAY, -9, NOW())),
(19, 'user13', '1234', '이수근', 'user13@nate.com', '010-1013-1013', 'USER', 'ACTIVE', 0, DATEADD(DAY, -8, NOW())),
(20, 'user14', '1234', '은지원', 'user14@nate.com', '010-1014-1014', 'USER', 'ACTIVE', 0, DATEADD(DAY, -7, NOW())),
(21, 'user15', '1234', '김종민', 'user15@nate.com', '010-1015-1015', 'USER', 'ACTIVE', 0, DATEADD(DAY, -6, NOW()));

-- ==========================================================
-- 2. 구역 정보 (zone_tb)
-- ==========================================================
INSERT INTO zone_tb (id, name, normal_price, peak_price, base_people, extra_person_fee, avg_rating, review_count, created_at) VALUES 
(1, 'A구역(오토캠핑)', 50000, 80000, 2, 10000, 0.0, 0, NOW()),
(2, 'B구역(글램핑)', 150000, 250000, 2, 20000, 0.0, 0, NOW()),
(3, 'C구역(카라반)', 120000, 200000, 2, 15000, 0.0, 0, NOW());

-- ==========================================================
-- 3. 사이트 정보 (site_tb)
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
-- 5. 예약 정보 (reservation_tb)
-- ==========================================================
INSERT INTO reservation_tb (id, user_id, site_id, check_in, check_out, total_price, people_count, visitor_name, visitor_phone, status, created_at) VALUES 
(1, 2, 1, '2026-03-01', '2026-03-03', 100000, 2, '박한별', '010-2222-2222', 'COMPLETED', NOW()),
(2, 3, 4, '2026-03-10', '2026-03-12', 300000, 2, '홍길동', '010-3333-3333', 'COMPLETED', NOW()),
(3, 4, 2, '2026-04-01', '2026-04-03', 120000, 4, '강사랑', '010-4444-4444', 'CONFIRMED', NOW()),
(4, 5, 5, '2026-05-01', '2026-05-02', 150000, 2, '이게스트', '010-5555-5555', 'CANCEL_REQ', NOW()),
(5, 2, 6, '2026-02-01', '2026-02-03', 240000, 2, '박한별', '010-2222-2222', 'CANCEL_COMP', NOW()),
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
(20, 12, 3, '2026-08-10', '2026-08-12', 100000, 2, '김종민', '010-1015-1015', 'CONFIRMED', NOW()),
(21, 3, 1, '2026-09-01', '2026-09-03', 100000, 2, '홍길동', '010-3333-3333', 'CHANGE_REQ', NOW()),
(22, 4, 4, '2026-09-10', '2026-09-12', 300000, 2, '강사랑', '010-4444-4444', 'CANCEL_REQ', NOW());

-- ==========================================================
-- 6. 결제 및 환불 정보
-- ==========================================================
INSERT INTO payment_tb (id, reservation_id, imp_uid, merchant_uid, amount, status, pay_date, created_at) VALUES 
(1, 1, 'imp_111111', 'ORD-20260220-001', 100000, 'PAID', '2026-02-20', NOW()),
(2, 2, 'imp_222222', 'ORD-20260305-001', 300000, 'PAID', '2026-03-05', NOW()),
(3, 3, 'imp_333333', 'ORD-20260310-001', 120000, 'PAID', '2026-03-10', NOW()),
(4, 4, 'imp_444444', 'ORD-20260311-001', 150000, 'PAID', '2026-03-11', NOW());

INSERT INTO refund_tb (id, reservation_id, reason, refund_amount, cancelled_at, created_at) VALUES
(1, 5, '개인 사정으로 인한 일정 변경 불가', 240000, NOW(), NOW());

-- ==========================================================
-- 7. 공지사항 및 QnA
-- ==========================================================
INSERT INTO notice_tb (id, title, content, is_top, created_at) VALUES 
(1, '2026년 봄 시즌 정식 오픈 안내', '정식 오픈합니다.', true, NOW()),
(2, '성수기 요금 안내', '성수기 요금이 적용됩니다.', false, NOW());

INSERT INTO qna_tb (id, user_id, title, content, category, hits, is_answered, created_at) VALUES 
(1, 2, '예약 취소 규정이 어떻게 되나요?', '취소 규정이 궁금합니다.', 'RESERVATION', 10, true, NOW()),
(2, 3, '주차 공간 문의', '주차 가능한가요?', 'FACILITY', 5, false, NOW()),
(3, 4, '장작 사용 가능한가요?', 'A구역에서 장작 사용이 가능한지 궁금합니다.', 'FACILITY', 15, false, DATEADD(MINUTE, 5, NOW())),
(4, 7, '반려동물 동반 가능한가요?', '강아지 데리고 가도 되나요?', 'ETC', 20, true, DATEADD(MINUTE, 10, NOW())),
(5, 8, '샤워실 이용 시간', '샤워실 이용 시간이 정해져 있나요?', 'FACILITY', 12, false, DATEADD(MINUTE, 15, NOW())),
(6, 9, '주변 맛집 추천 부탁드려요', '주변에 맛있는 식당이 있나요?', 'ETC', 8, true, DATEADD(MINUTE, 20, NOW())),
(7, 10, '캠핑카 진입 가능한가요?', '카라반 사이트 말고도 캠핑카 진입이 되나요?', 'FACILITY', 30, false, DATEADD(MINUTE, 25, NOW())),
(8, 11, '밤에 별이 잘 보이나요?', '야경이 어떤지 궁금합니다.', 'ETC', 45, true, DATEADD(MINUTE, 30, NOW())),
(9, 12, '수영장 개장 시기', '수영장은 언제 오픈하나요?', 'FACILITY', 22, false, DATEADD(MINUTE, 35, NOW())),
(10, 13, '매점 운영 시간', '매점은 몇 시까지 하나요?', 'ETC', 18, true, DATEADD(MINUTE, 40, NOW())),
(11, 14, '불멍 세트 대여 되나요?', '캠핑장에서 불멍 세트를 빌려주나요?', 'FACILITY', 50, false, DATEADD(MINUTE, 45, NOW())),
(12, 15, '전기 사용 관련 문의', '각 사이트마다 전기를 사용할 수 있나요?', 'ETC', 14, true, DATEADD(MINUTE, 50, NOW())),
(13, 2, '와이파이 되나요?', '전 구역에서 와이파이가 잡히나요?', 'FACILITY', 9, false, DATEADD(MINUTE, 55, NOW())),
(14, 3, '매너타임 시간 안내 부탁드려요', '매너타임은 몇 시부터인가요?', 'ETC', 11, true, DATEADD(MINUTE, 60, NOW())),
(15, 4, '주변 마트 위치', '가장 가까운 마트가 어디인가요?', 'ETC', 60, false, DATEADD(MINUTE, 65, NOW()));

INSERT INTO comment_tb (id, qna_id, admin_id, content, created_at) VALUES
(1, 1, 1, '공지사항의 취소/환불 규정을 확인해 주세요.', NOW()),
(2, 4, 1, '네, 목줄 착용 시 동반 가능합니다.', NOW()),
(3, 6, 1, '차로 10분 거리에 맛있는 산채비빔밥 집이 있습니다.', NOW()),
(4, 8, 1, '네, 지대가 높고 공기가 맑아 별이 아주 잘 보입니다.', NOW()),
(5, 10, 1, '매점은 오후 9시까지 운영합니다.', NOW()),
(6, 12, 1, '네, 각 사이트마다 개별 배전함이 설치되어 있습니다.', NOW()),
(7, 14, 1, '매너타임은 오후 10시부터 다음날 오전 8시까지입니다.', NOW());

-- ==========================================================
-- 8. 리뷰 (review_tb)
-- ==========================================================
INSERT INTO review_tb (id, user_id, reservation_id, rating, content, ai_danger_score, is_reviewed, is_deleted, admin_reason, created_at) VALUES 
(2, 3, 2, 4, '조용하고 공기가 좋아서 힐링되었습니다.', 1, FALSE, FALSE, NULL, NOW()),
(3, 4, 3, 1, '사장님 진짜 불친절하네요. 개새끼들 다시는 안옴ㅡㅡ', 5, FALSE, FALSE, NULL, NOW()),
(4, 2, 18, 1, '사장님 참 대단하시네요. 텐트에서 물이 새는데 웃으면서 원래 그런 거라고 하시니 정말 존경스럽습니다. 장사 참 쉽게 하시네요?', 4, FALSE, FALSE, NULL, NOW()),
(5, 3, 19, 1, '시설이 거의 쓰레기장 수준이네요. 이런 곳에서 돈 받고 사람을 재우다니 양심이 있으신 건지 모르겠습니다. 절대 가지 마세요.', 4, FALSE, FALSE, NULL, NOW()),
(6, 4, 20, 1, '블로그 광고에 속았네요. 실제로는 엉망진창입니다. 여기 좋다고 하는 사람들은 다 알바인가 봐요? 기분만 잡치고 갑니다.', 3, FALSE, FALSE, NULL, NOW()),
(7, 7, 6, 5, '가족들과 좋은 추억 만들고 갑니다. 시설이 아주 깨끗하고 관리가 잘 되어 있네요. 강력 추천합니다!', 1, FALSE, FALSE, NULL, NOW()),
(8, 8, 7, 2, '풍경은 좋은데 화장실 청소 상태가 조금 아쉽네요. 다음에는 좀 더 신경 써주시면 좋겠습니다.', 2, FALSE, FALSE, NULL, NOW()),
(9, 9, 17, 1, '와 진짜 여기 사람 대접 못 받는 곳이네요. 예약 취소하고 싶은데 돈 아까워서 억지로 참았습니다. 다신 안 와요.', 4, FALSE, FALSE, NULL, NOW()),
(10, 10, 9, 3, '그냥 그래요. 딱히 나쁘지도 않지만 좋지도 않은? 근데 사장님이 손님을 좀 가려 받으시는 느낌이 드네요.', 3, FALSE, FALSE, NULL, NOW()),
(11, 11, 11, 1, '최악 중의 최악. 별점 1점도 아깝네요. 망해버렸으면 좋겠습니다 진짜로.', 5, FALSE, FALSE, NULL, NOW());


-- ==========================================================
-- 9. 갤러리 및 이미지 (gallery_tb, image_tb)
-- ==========================================================
INSERT INTO gallery_tb (id, title, category, shooting_date, content, view_count, created_at) VALUES
(1, '화창한 봄날의 캠핑장', '캠핑장 풍경', '2026-03-01', '봄 햇살 아래 캠핑장 전경입니다.', 128, NOW()),
(2, '깨끗하게 정돈된 사이트 공간', '캠핑장 풍경', '2026-03-05', '기초 공사가 완료된 A구역입니다.', 85, NOW()),
(3, '즐거운 가족 캠핑 기록', '캠핑장 풍경', '2026-03-10', '아이들과 함께한 소중한 시간!', 242, NOW()),
(4, '캠핑의 꽃 바베큐 파티', '캠핑 음식', '2026-03-12', '저녁 노을을 보며 즐기는 바베큐.', 56, NOW()),
(5, '밤하늘의 은하수', '특별한 순간', '2026-03-15', '카메라에 담긴 쏟아지는 별들.', 310, NOW()),
(6, '새벽 안개 속 캠핑장', '캠핑장 풍경', '2026-03-17', '조용한 새벽의 몽환적인 분위기.', 45, NOW()),
(7, '눈 내린 겨울 캠핑', '특별한 순간', '2026-02-20', '설경 속 낭만적인 텐트.', 120, NOW()),
(8, '가을 단풍 아래 캠핑', '캠핑장 풍경', '2025-11-05', '붉게 물든 산과 함께하는 가을 캠핑.', 156, NOW()),
(9, '캠핑 요리 열전', '캠핑 음식', '2026-01-10', '추운 겨울 따뜻한 어묵탕과 캠핑.', 89, NOW()),
(10, '봄을 기다리는 꽃망울', '특별한 순간', '2026-03-05', '캠핑장 곳곳에 핀 봄꽃들.', 34, NOW()),
(11, '바베큐와 함께하는 즐거운 저녁', '캠핑 음식', '2026-03-12', '가족들과 맛있는 고기를 구워 먹었습니다.', 210, NOW()),
(12, '텐트에서 바라본 아침 풍경', '특별한 순간', '2026-03-18', '새 아침을 여는 캠핑장의 평화.', 77, NOW());

INSERT INTO image_tb (id, gallery_id, review_id, notice_id, zone_id, site_id, file_path, file_name, created_at) VALUES
(1, null, null, null, 1, null, '/images/', 'Camping_map.png', NOW()),
(3, 1, null, null, null, null, '/images/', '1.png', NOW()),
(4, 1, null, null, null, null, '/images/', '2.png', NOW()),
(5, 2, null, null, null, null, '/images/', '3.jpg', NOW()),
(7, 3, null, null, null, null, '/images/', '4.jpg', NOW()),
(8, 3, null, null, null, null, '/images/', '5.jpg', NOW()),
(9, 4, null, null, null, null, '/images/', '6.jpg', NOW()),
(10, 5, null, null, null, null, '/images/', 'camping_review1.jpg', NOW()),
(11, 6, null, null, null, null, '/images/', 'camping_review2.jpg', NOW()),
(12, 7, null, null, null, null, '/images/', 'camping_review3.jpg', NOW()),
(13, 8, null, null, null, null, '/images/', '1.png', NOW()),
(14, 9, null, null, null, null, '/images/', '2.png', NOW()),
(15, 10, null, null, null, null, '/images/', '3.jpg', NOW()),
(16, 11, null, null, null, null, '/images/', '4.jpg', NOW()),
(17, 12, null, null, null, null, '/images/', '5.jpg', NOW());

-- [리뷰 전용 이미지 연결]
INSERT INTO image_tb (gallery_id, review_id, notice_id, zone_id, site_id, file_path, file_name, created_at) VALUES 
(null, 2, null, null, null, '/images/', 'camping_review1.jpg', NOW()),
(null, 3, null, null, null, '/images/', 'camping_review2.jpg', NOW()),
(null, 4, null, null, null, '/images/', 'camping_review3.jpg', NOW());

-- ==========================================================
-- 10. 예약 변경/취소 요청
-- ==========================================================
INSERT INTO reservation_change_request (id, reservation_id, new_check_in, new_check_out, new_site_id, new_people_count, status, old_total_price, new_total_price, settlement_type, is_refunded, created_at) VALUES
(1, 21, '2026-09-05', '2026-09-07', 3, 4, 'PENDING', 100000, 100000, 'NONE', FALSE, NOW());

INSERT INTO reservation_cancel_request (id, reservation_id, reason, refund_bank, refund_account, refund_account_holder, refund_amount, status, is_refunded, created_at) VALUES
(1, 22, '개인 사정으로 인한 취소 요청입니다.', '우리은행', '110-123-456789', '강사랑', 300000, 'PENDING', FALSE, NOW()),
(2, 4, '갑작스러운 출장으로 취소합니다.', '신한은행', '112-223-334455', '이게스트', 150000, 'PENDING', FALSE, NOW()),
(3, 10, '가족 건강 문제로 예약 취소합니다.', '국민은행', '223-334-445566', '김연아', 300000, 'PENDING', FALSE, NOW());
