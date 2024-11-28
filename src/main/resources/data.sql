-- 초기 멤버 삽입
INSERT IGNORE INTO member (member_id, member_email, member_pwd, member_nickname, member_role, member_phonenumber, member_sex, birth_day, created_at)
                VALUES (1, 'member1@mail.com', '$2a$10$rzfQCidiffIo75kbfSLF8umiGCacW2Vty1aJVtQIUmBBwXIN0Jwvq', '회원1', 'ROLE_USER', '010-0000-0000', 'MALE', "2000-03-09", NOW()),
                       (2, 'member2@mail.com', '$2a$10$rzfQCidiffIo75kbfSLF8umiGCacW2Vty1aJVtQIUmBBwXIN0Jwvq', '회원2', 'ROLE_USER', '010-0000-0000', 'FEMALE', "1999-05-14", NOW()),
                       (3, 'admin@mail.com', '$2a$10$rzfQCidiffIo75kbfSLF8umiGCacW2Vty1aJVtQIUmBBwXIN0Jwvq', '관리자', 'ROLE_ADMIN', '010-0000-0000', 'MALE', "2005-05-14",NOW()),
                       (4, 'member4@mail.com', '$2a$10$rzfQCidiffIo75kbfSLF8umiGCacW2Vty1aJVtQIUmBBwXIN0Jwvq', '회원4', 'ROLE_USER', '010-1111-1111', 'FEMALE', '1998-08-21', NOW()),
                       (5, 'member5@mail.com', '$2a$10$rzfQCidiffIo75kbfSLF8umiGCacW2Vty1aJVtQIUmBBwXIN0Jwvq', '회원5', 'ROLE_USER', '010-2222-2222', 'MALE', '1995-12-10', NOW()),
                       (6, 'member6@mail.com', '$2a$10$rzfQCidiffIo75kbfSLF8umiGCacW2Vty1aJVtQIUmBBwXIN0Jwvq', '회원6', 'ROLE_USER', '010-3333-3333', 'FEMALE', '2001-04-05', NOW());


-- 초기 사업자 삽입
INSERT IGNORE INTO business (business_id, business_email, business_pwd, business_name, business_num, business_role, created_at)
                VALUES (1, 'business1@gmail.com', '$2a$10$rzfQCidiffIo75kbfSLF8umiGCacW2Vty1aJVtQIUmBBwXIN0Jwvq', '사업자1', '111-11-12345', 'ROLE_BUSINESS', NOW()),
                       (2, 'business2@gmail.com', '$2a$10$rzfQCidiffIo75kbfSLF8umiGCacW2Vty1aJVtQIUmBBwXIN0Jwvq', '사업자2', '111-12-12345', 'ROLE_BUSINESS', NOW()),
                       (3, 'business3@gmail.com', '$2a$10$rzfQCidiffIo75kbfSLF8umiGCacW2Vty1aJVtQIUmBBwXIN0Jwvq', '사업자3', '111-33-12345', 'ROLE_BUSINESS', NOW());


-- 초기 사업장 삽입
-- 1번 사업자
INSERT IGNORE INTO workplace (workplace_id, business_id, workplace_name, workplace_description, workplace_address, workplace_longitude, workplace_latitude,
                              image_url, workplace_phone_number, workplace_start_time, workplace_end_time,  created_at)
                VALUES (1, 1, '타임유스터디카페 민락점', '의정부 민락동에 위치한 스터디룸', '경기 의정부시 용현로105번길 19 완빌딩', '37.7422130035087', '127.087201202098',
                        'https://search.pstatic.net/common/?src=https%3A%2F%2Fldb-phinf.pstatic.net%2F20231218_94%2F1702881416770IHqB6_JPEG%2Ffocus1.JPG', '010-2666-4762', '2024-06-08T09:00:00', '2024-06-08T23:00:00', NOW()),
                       (2, 1, '타임유스터디카페 망월사역점', '의정부 망월사역에 위치한 스터디룸', '경기 의정부시 평화로 170', '37.706967295248', '127.048476534971',
                        'https://search.pstatic.net/common/?src=https%3A%2F%2Fpup-review-phinf.pstatic.net%2FMjAyMzEwMTZfMjc2%2FMDAxNjk3MzgzNDI1NjQz.aEqLbREqc0WCz2b68vkjm_XYVvLVwgzgHraaYWyfvekg.lYdNs4Bwc7FP0GX5GYbW-GD7-kJja96OFoiQDIjTUWcg.JPEG%2Fupload_31fe1a883e90afdbbb1320bbc108003b.jpg%3Ftype%3Dw1500_60_sharpen', '0507-1344-2478', '2024-06-08T09:00:00', '2024-06-08T23:00:00', NOW());

-- 2번 사업자
INSERT IGNORE INTO workplace (workplace_id, business_id, workplace_name, workplace_description, workplace_address, workplace_longitude, workplace_latitude,
                              image_url, workplace_phone_number, workplace_start_time, workplace_end_time,  created_at)
                VALUES (3, 2, '영글 강남스터디룸', '논현역회의실 파티룸네이버페이', '서울 강남구 학동로1길 19 금성빌딩 5층', '37.5126594626812', '127.02157831586',
                        'https://search.pstatic.net/common/?src=https%3A%2F%2Fldb-phinf.pstatic.net%2F20240808_204%2F1723106102664gSDVA_JPEG%2FIMG_2165.jpeg', '0507-1489-0421', '2024-06-08T09:00:00', '2024-06-08T23:00:00', NOW());
-- 3번 사업자
INSERT IGNORE INTO workplace (workplace_id, business_id, workplace_name, workplace_description, workplace_address, workplace_longitude, workplace_latitude,
                              image_url, workplace_phone_number, workplace_start_time, workplace_end_time,  created_at)
                VALUES (4, 3, '옐로스톤 스터디룸', '강남역 도보 3분거리에 위치한 옐로스톤 스터디룸', '서울 강남구 강남대로94길 21', '37.4997243135104', '127.02896610336',
                        'https://search.pstatic.net/common/?src=https%3A%2F%2Fldb-phinf.pstatic.net%2F20150901_244%2F14410338887412ja3X_JPEG%2FSUBMIT_1416958175779_35966443.jpg', '0507-1383-8777', '2024-06-08T09:00:00', '2024-06-08T23:00:00', NOW()),
                       (5, 3, 'ENI스터디룸', '강남역 11번출구 영풍문구 뒤편 1층 GS편의점 2층 교대이층집이 위치한 건물입니다', '서울 강남구 강남대로94길 9 크리스탈빌딩 3층', '37.4994839547907', '127.028096922783',
                        'https://search.pstatic.net/common/?src=https%3A%2F%2Fnaverbooking-phinf.pstatic.net%2F20230911_219%2F1694409613700AmMFv_JPEG%2F1%25B9%25F8%25B9%25E62.jpg', '02-555-5284', '2024-06-08T09:00:00', '2024-06-08T23:00:00', NOW());


-- 초기 스터디룸 삽입
-- 1번 사업장
INSERT IGNORE INTO study_room (studyroom_id, workplace_id, studyroom_title, studyroom_price, studyroom_description, studyroom_capacity, studyroom_image_url, created_at)
  VALUES
    (1, 1, 'Room A', 7000, '조용하고 쾌적한 환경, 최대 4인 가능', 4, 'https://{s3주소}/roomA/', NOW()),
    (2, 1, 'Room B', 8000, '편안한 의자와 테이블 제공, 최대 6인', 6, 'https://{s3주소}/roomB/', NOW()),
    (3, 1, 'Room C', 6000, '작은 팀 프로젝트에 적합한 공간, 최대 3인', 3, 'https://{s3주소}/roomC/', NOW());

-- 2번 사업장
INSERT IGNORE INTO study_room (studyroom_id, workplace_id, studyroom_title, studyroom_price, studyroom_description, studyroom_capacity, studyroom_image_url, created_at)
  VALUES
    (4, 2, 'Quiet Room 1', 10000, '독립적인 학습 공간, 최대 8인', 8, 'https://{s3주소}/quiet1/', NOW()),
    (5, 2, 'Meeting Room', 15000, '소규모 회의에 적합한 공간, 최대 12인', 12, 'https://{s3주소}/meeting/', NOW());

-- 3번 사업장
INSERT IGNORE INTO study_room (studyroom_id, workplace_id, studyroom_title, studyroom_price, studyroom_description, studyroom_capacity, studyroom_image_url, created_at)
  VALUES
    (6, 3, 'Yellow Room A', 12000, '밝고 넓은 스터디룸, 최대 10인', 10, 'https://{s3주소}/yellowA/', NOW()),
    (7, 3, 'Yellow Room B', 9000, '아늑한 공간, 최대 6인', 6, 'https://{s3주소}/yellowB/', NOW()),
    (8, 3, 'Yellow Room C', 8000, '팀 학습에 최적화, 최대 5인', 5, 'https://{s3주소}/yellowC/', NOW()),
    (9, 3, 'Focus Room', 6000, '소규모 그룹 스터디에 적합, 최대 3인', 3, 'https://{s3주소}/focus/', NOW());

-- 4번 사업장
INSERT IGNORE INTO study_room (studyroom_id, workplace_id, studyroom_title, studyroom_price, studyroom_description, studyroom_capacity, studyroom_image_url, created_at)
  VALUES
    (10, 4, 'Deluxe Room', 20000, '최고급 시설, 최대 15인', 15, 'https://{s3주소}/deluxe/', NOW()),
    (11, 4, 'Standard Room', 10000, '실속형 스터디룸, 최대 6인', 6, 'https://{s3주소}/standard/', NOW());

-- 5번 사업장
INSERT IGNORE INTO study_room (studyroom_id, workplace_id, studyroom_title, studyroom_price, studyroom_description, studyroom_capacity, studyroom_image_url, created_at)
  VALUES
    (12, 5, 'Modern Room A', 8000, '모던한 인테리어, 최대 4인', 4, 'https://{s3주소}/modernA/', NOW()),
    (13, 5, 'Modern Room B', 11000, '프리미엄 공간, 최대 8인', 8, 'https://{s3주소}/modernB/', NOW()),
    (14, 5, 'Small Group Room', 6000, '소규모 모임에 적합, 최대 3인', 3, 'https://{s3주소}/smallgroup/', NOW()),
    (15, 5, 'Team Room', 13000, '팀 프로젝트에 이상적, 최대 10인', 10, 'https://{s3주소}/team/', NOW());


-- 사업장별 리뷰 데이터 삽입
INSERT IGNORE INTO review (member_id, workplace_id, review_content, review_rating, created_at) VALUES
-- Workplace 1
(1, 1, 'Excellent service!', 4.8, NOW()),
(2, 1, 'Very satisfied.', 4.5, NOW()),
(3, 1, 'Good experience.', 4.0, NOW()),
(4, 1, 'Decent place.', 3.7, NOW()),
(5, 1, 'Highly recommend.', 4.9, NOW()),
(1, 1, 'Friendly staff.', 4.6, NOW()),
(2, 1, 'Clean and organized.', 4.7, NOW()),

-- Workplace 2
(3, 2, 'Not bad.', 3.8, NOW()),
(4, 2, 'Could be better.', 3.5, NOW()),
(5, 2, 'Great environment.', 4.2, NOW()),
(1, 2, 'Loved the place.', 4.8, NOW()),
(2, 2, 'Will visit again.', 4.7, NOW()),
(3, 2, 'Affordable and clean.', 4.3, NOW()),
(4, 2, 'Noisy but okay.', 3.9, NOW()),
(5, 2, 'Very welcoming.', 4.6, NOW()),

-- Workplace 3
(1, 3, 'Best place ever.', 5.0, NOW()),
(2, 3, 'Good value.', 4.4, NOW()),
(3, 3, 'Slow service.', 3.1, NOW()),
(4, 3, 'Friendly staff.', 4.5, NOW()),
(5, 3, 'Loved the atmosphere.', 4.9, NOW()),
(1, 3, 'Clean and spacious.', 4.7, NOW()),
(2, 3, 'Affordable prices.', 4.2, NOW()),
(3, 3, 'Service needs improvement.', 3.3, NOW()),

-- Workplace 4
(4, 4, 'Great location.', 4.5, NOW()),
(5, 4, 'Very professional.', 4.7, NOW()),
(1, 4, 'Good experience overall.', 4.1, NOW()),
(2, 4, 'Not up to the mark.', 3.2, NOW()),
(3, 4, 'Pleasant stay.', 4.6, NOW()),
(4, 4, 'Friendly staff.', 4.3, NOW()),
(5, 4, 'Will visit again.', 4.9, NOW()),
(1, 4, 'Affordable and clean.', 4.4, NOW()),
(2, 4, 'Loved the environment.', 4.8, NOW()),

-- Workplace 5
(3, 5, 'Not as expected.', 3.4, NOW()),
(4, 5, 'Great value for money.', 4.5, NOW()),
(5, 5, 'Clean and tidy.', 4.6, NOW()),
(1, 5, 'Service was slow.', 3.0, NOW()),
(2, 5, 'Friendly staff.', 4.2, NOW()),
(3, 5, 'Good location.', 4.4, NOW()),
(4, 5, 'Highly recommend!', 4.8, NOW()),
(5, 5, 'Good pricing.', 4.1, NOW()),
(1, 5, 'Will come back.', 4.7, NOW()),
(2, 5, 'Amazing place.', 4.9, NOW());
