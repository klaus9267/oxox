-- 기존 ENUM을 새로운 ENUM으로 변경
ALTER TABLE `reactions`
  CHANGE `emoji` `emoji` enum('SMILE','SHOCK','HEART','CRY','ANGRY','NONE') COLLATE utf8mb4_general_ci DEFAULT NULL;

-- 기존 데이터 값 변경을 위해 필요한 경우 업데이트 쿼리 작성
UPDATE `reactions` SET `emoji` = 'NONE' WHERE `emoji` IS NULL;

-- 기존 ENUM을 새로운 ENUM으로 변경
ALTER TABLE `reaction_counts`
  CHANGE `emoji` `emoji` enum('SMILE','SHOCK','HEART','CRY','ANGRY') COLLATE utf8mb4_general_ci NOT NULL;
