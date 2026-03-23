-- ==========================================================
-- 1. 회원 정보 (user_tb) - ID 명시
-- ==========================================================
INSERT INTO user_tb (id, username, password, name, email, phone, role, status, penalty_count, created_at) VALUES 
(1, 'admin', '1234', '관리자', 'admin@camping.com', '010-1111-1111', 'ADMIN', 'ACTIVE', 0, NOW()),
(2, 'ssar', '1234', '박한별', 'ssar@nate.com', '010-2222-2222', 'USER', 'ACTIVE', 3, NOW()),
(3, 'cos', '1234', '홍길동', 'cos@nate.com', '010-3333-3333', 'USER', 'ACTIVE', 1, NOW()),
(4, 'love', '1234', '강사랑', 'love@nate.com', '010-4444-4444', 'USER', 'ACTIVE', 0, NOW()),
(5, 'guest', '1234', '이게스트', 'guest@nate.com', '010-5555-5555', 'USER', 'ACTIVE', 0, NOW()),
(6, 'olduser', '1234', '탈퇴사용자', 'old@nate.com', '010-6666-6666', 'USER', 'ANONYMOUS', 0, NOW());


INSERT INTO user_tb (id, username, password, name, email, phone, role, status, penalty_count, created_at) VALUES 
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
(20, 12, 3, '2026-08-10', '2026-08-12', 100000, 2, '김종민', '010-1015-1015', 'CONFIRMED', NOW()),
(21, 4, 1, '2026-03-15', '2026-03-17', 100000, 2, '강사랑', '010-4444-4444', 'COMPLETED', NOW());

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

-- QnA 더미 데이터 추가 (ID 3~15번)
INSERT INTO qna_tb (id, user_id, title, content, category, hits, is_answered, created_at) VALUES 
(3, 4, '전기 사용 가능한가요?', 'A구역 전기 릴선 길이 얼마나 필요한가요?', 'FACILITY', 15, false, DATEADD(MINUTE, 5, NOW())),
(4, 7, '겨울철 눈이 오면 제설 해주나요?', '다음 주 예약인데 눈 소식이 있어서요.', 'ETC', 20, true, DATEADD(MINUTE, 10, NOW())),
(5, 8, '화장실 온수 잘 나오나요?', '아이들이랑 가는데 온수가 중요한가요.', 'FACILITY', 12, false, DATEADD(MINUTE, 15, NOW())),
(6, 9, '주변 마트 위치 문의', '장작이나 고기 살만한 곳이 근처에 있나요?', 'ETC', 8, true, DATEADD(MINUTE, 20, NOW())),
(7, 10, '글램핑 식기류 구비 현황', '따로 챙겨갈 식기가 있을까요?', 'FACILITY', 30, false, DATEADD(MINUTE, 25, NOW())),
(8, 11, '밤에 별이 잘 보이나요?', '아이들과 별 보러 가려고 합니다.', 'ETC', 45, true, DATEADD(MINUTE, 30, NOW())),
(9, 12, '사이트 크기 문의', '리빙쉘 텐트 피칭 가능한 사이즈인가요?', 'FACILITY', 22, false, DATEADD(MINUTE, 35, NOW())),
(10, 13, '소음 관련 문의', '매너타임 관리가 잘 되고 있는지 궁금합니다.', 'ETC', 18, true, DATEADD(MINUTE, 40, NOW())),
(11, 14, '불멍 장작 판매 하나요?', '매점에서 장작 얼마에 파나요?', 'FACILITY', 50, false, DATEADD(MINUTE, 45, NOW())),
(12, 15, '취소 위약금 관련', '갑자기 일이 생겨서 취소하려고 하는데...', 'ETC', 14, true, DATEADD(MINUTE, 50, NOW())),
(13, 2, '와이파이 속도 어떤가요?', '노트북 작업이 좀 필요한데 끊기지 않을까요?', 'FACILITY', 9, false, DATEADD(MINUTE, 55, NOW())),
(14, 3, '해충 방역 정기적으로 하나요?', '여름이라 벌레가 걱정되어서요.', 'ETC', 11, true, DATEADD(MINUTE, 60, NOW())),
(15, 4, '주말 예약 오픈 시간', '언제 다음 달 예약이 열리나요?', 'ETC', 60, false, DATEADD(MINUTE, 65, NOW()));

INSERT INTO comment_tb (id, qna_id, admin_id, content, created_at) VALUES 
(1, 1, 1, 'A구역만 소형견 가능합니다.', NOW()),
(2, 4, 1, '네, 제설 장비가 상시 대기 중입니다.', NOW()),
(3, 6, 1, '차로 10분 거리에 하나로마트가 있습니다.', NOW()),
(4, 8, 1, '네, 지대가 높고 공기가 맑아 아주 잘 보입니다.', NOW()),
(5, 10, 1, '순찰을 돌며 철저히 관리하고 있습니다.', NOW()),
(6, 12, 1, '공지사항의 환불 규정을 확인 부탁드립니다.', NOW()),
(7, 14, 1, '매주 수요일 전문 업체 방역을 실시합니다.', NOW());

-- ==========================================================
-- 9. 이용 후기 (review_tb) - 1예약 1리뷰 원칙 준수
-- ==========================================================
INSERT INTO review_tb (id, user_id, reservation_id, rating, content, ai_danger_score, is_reviewed, is_deleted, admin_reason, created_at) VALUES 
(1, 2, 1, 5, '정말 좋았습니다. 다음에도 꼭 오고 싶어요!', 0, FALSE, FALSE, NULL, NOW()),
(2, 3, 2, 4, '조용하고 공기가 좋아서 힐링되었습니다.', 0, FALSE, FALSE, NULL, NOW()),
(3, 4, 21, 1, '사장님 진짜 불친절하네요. 개새끼들 다시는 안옴ㅡㅡ', 5, FALSE, FALSE, NULL, NOW());


-- ==========================================================
-- 10. 갤러리 (gallery_tb) - 데이터 확장 (총 12개)
-- ==========================================================
INSERT INTO gallery_tb (id, title, category, shooting_date, content, view_count, created_at) VALUES 
(1, '안개 낀 숲속의 아침', '캠핑장 전경', '2026-03-01', '숲속 아침 풍경입니다.', 128, NOW()),
(2, '글램핑 내부 시설 공개', '캠핑장 전경', '2026-03-05', '깨끗하고 아늑합니다.', 85, NOW()),
(3, '주말 숲속 캠핑 기록', '캠핑장 전경', '2026-03-10', '친구들과 함께한 즐거운 숲속 캠핑!', 242, NOW()),
(4, '호숫가 텐트 설치 완료', '이용 후기', '2026-03-12', '호수 뷰가 정말 예술이네요.', 56, NOW()),
(5, '밤하늘 은하수와 캠핑', '특별 이벤트', '2026-03-15', '밤하늘에 쏟아지는 별들을 보며 힐링했습니다.', 310, NOW()),
(6, '아침 안개 속의 캠핑장', '캠핑장 전경', '2026-03-17', '신비로운 분위기의 캠핑장 모습입니다.', 45, NOW()),
(7, '겨울 왕국 글램핑', '특별 이벤트', '2026-02-20', '눈 덮인 캠핑장의 낭만을 즐겨보세요.', 120, NOW()),
(8, '가을 단풍 캠핑', '캠핑장 전경', '2025-11-05', '붉게 물든 단풍과 함께하는 캠핑.', 156, NOW()),
(9, '캠핑장의 밤 풍경', '캠핑장 전경', '2026-01-10', '조명이 켜진 캠핑장의 아름다운 밤.', 89, NOW()),
(10, '봄 맞이 사이트 정비', '특별 이벤트', '2026-03-05', '새로운 시즌을 준비하는 모습입니다.', 34, NOW()),
(11, '가족과 함께하는 바비큐', '이용 후기', '2026-03-12', '가족들과 맛있는 고기를 구워 먹었어요.', 210, NOW()),
(12, '숲속의 작은 도서관', '특별 이벤트', '2026-03-18', '숲에서 즐기는 독서 시간.', 77, NOW());

-- ==========================================================
-- 11. 이미지 관리 (image_tb)
-- 역할: 갤러리, 리뷰, 공지사항 및 구역/사이트 이미지 파일 정보
-- ==========================================================
INSERT INTO image_tb (id, gallery_id, review_id, notice_id, zone_id, site_id, file_path, file_name, created_at) VALUES 
(1, null, null, null, 1, null, '/upload/', 'zone_a_main.jpg', NOW()),
(2, null, 1, null, null, null, '/upload/', 'my_pic.jpg', NOW()),
(3, 1, null, null, null, null, '/upload/', '4adc36ff-bbfc-4c3c-86d8-d5f6c60a63da_wesley-shen-2l2EslhTaOM-unsplash.jpg', NOW()),
(4, 1, null, null, null, null, '/upload/', 'f52dd56a-3b53-473d-b4ea-63c8c085be2f_chris-lynch-jKj_ujtUe6Q-unsplash.jpg', NOW()),
(5, 2, null, null, null, null, '/upload/', '1df7d7fc-2f49-4f29-a27b-71f68cbfb104_wesley-shen-2l2EslhTaOM-unsplash.jpg', NOW()),
(6, null, 1, null, null, null, '/upload/', 'my_camping_pic.jpg', NOW()),
(7, 3, null, null, null, null, '/upload/', '22918b6d-6245-41ac-b492-589b1623f6ef_wesley-shen-2l2EslhTaOM-unsplash.jpg', NOW()),
(8, 3, null, null, null, null, '/upload/', '300d603c-5a5c-4ce1-96b5-2d3e3692b513_wesley-shen-2l2EslhTaOM-unsplash.jpg', NOW()),
(9, 4, null, null, null, null, '/upload/', '33c9f42d-9b09-45d9-b6fb-220f3070148a_stills_by_suki-autumn-8356402_1280.jpg', NOW()),
(10, 5, null, null, null, null, '/upload/', '365920b1-efaf-46f6-9eae-1a9ec9c13a24_wesley-shen-2l2EslhTaOM-unsplash.jpg', NOW()),
(11, 6, null, null, null, null, '/upload/', '38edae3d-238d-45af-8c62-8524e01afbb0_wesley-shen-2l2EslhTaOM-unsplash.jpg', NOW()),
(12, 7, null, null, null, null, '/upload/', '46a41582-0851-4f46-b8c6-b28e47046ce3_wesley-shen-2l2EslhTaOM-unsplash.jpg', NOW()),
(13, 8, null, null, null, null, '/upload/', '0497ed2c-5881-4db2-bde8-979039c2c197_chris-lynch-jKj_ujtUe6Q-unsplash.jpg', NOW()),
(14, 9, null, null, null, null, '/upload/', '07925048-e0ee-43c5-806f-7dbfd97389e8_chris-lynch-jKj_ujtUe6Q-unsplash.jpg', NOW()),
(15, 10, null, null, null, null, '/upload/', '13d7c6a8-6b3f-4d44-ba8a-69c3f470c202_stills_by_suki-autumn-8356402_1280.jpg', NOW()),
(16, 11, null, null, null, null, '/upload/', '16d44a91-4b62-4083-bdc3-f84154ed2352_stills_by_suki-autumn-8356402_1280.jpg', NOW()),
(17, 12, null, null, null, null, '/upload/', '28edcc83-74d8-4fb8-868c-8d005c70717a_stills_by_suki-autumn-8356402_1280.jpg', NOW());

-- ==========================================================
-- 12. 예약 변경 및 취소 요청 테스트 데이터 (Task 4-2 검증용)
-- ==========================================================

-- [12-1] 예약 변경 요청 건 추가 (ID: 21)
-- 기존: 홍길동(ID: 3), A-1 사이트(ID: 1), 2026-09-01 ~ 09-03
INSERT INTO reservation_tb (id, user_id, site_id, check_in, check_out, total_price, people_count, visitor_name, visitor_phone, status, created_at) VALUES 
(21, 3, 1, '2026-09-01', '2026-09-03', 100000, 2, '홍길동', '010-3333-3333', 'CHANGE_REQ', NOW());

-- 변경 요청 상세 (ID: 1)
-- 새 정보: A-3 사이트(ID: 3), 2026-09-05 ~ 09-07, 4명
INSERT INTO reservation_change_request (id, reservation_id, new_check_in, new_check_out, new_site_id, new_people_count, status, created_at) VALUES 
(1, 21, '2026-09-05', '2026-09-07', 3, 4, 'PENDING', NOW());


-- [12-2] 예약 취소 요청 건 추가 (ID: 22)
-- 기존: 강사랑(ID: 4), B-1 사이트(ID: 4), 2026-09-10 ~ 09-12
INSERT INTO reservation_tb (id, user_id, site_id, check_in, check_out, total_price, people_count, visitor_name, visitor_phone, status, created_at) VALUES 
(22, 4, 4, '2026-09-10', '2026-09-12', 300000, 2, '강사랑', '010-4444-4444', 'CANCEL_REQ', NOW());

-- 취소 요청 상세 (ID: 1)
-- 사유: 갑작스러운 출장 일정으로 취소 요청합니다.
INSERT INTO reservation_cancel_request (id, reservation_id, reason, refund_bank, refund_account, refund_account_holder, status, created_at) VALUES 
(1, 22, '갑작스러운 출장 일정으로 취소 요청합니다.', '신한은행', '110-123-456789', '강사랑', 'PENDING', NOW());

