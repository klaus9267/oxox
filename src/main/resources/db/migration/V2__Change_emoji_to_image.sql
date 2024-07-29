-- emoji 컬럼 삭제
ALTER TABLE profiles DROP COLUMN emoji;

-- 새로운 image 컬럼 추가
ALTER TABLE profiles ADD COLUMN image VARCHAR(255);