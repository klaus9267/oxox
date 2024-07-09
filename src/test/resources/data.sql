INSERT INTO users (email, password) VALUES
('test1@email.com', '$2a$10$g3Baj0zdh/y09G.rwdYIV.TKVncMiwf3Uw152Ji4MlSqsgz13ooLG'),
('test2@email.com', '$2a$10$g3Baj0zdh/y09G.rwdYIV.TKVncMiwf3Uw152Ji4MlSqsgz13ooLG');

INSERT INTO profiles (sequence, user_id, emoji, nickname) VALUES
(1, 1, 'emoji', 'nickname1'),
(1, 2, 'emoji', 'nickname2');