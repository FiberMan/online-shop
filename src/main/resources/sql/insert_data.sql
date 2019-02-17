DELETE FROM onlineshop.user;
INSERT INTO onlineshop.user (name, login, login_hash, login_salt, user_role) VALUES ('Y F', 'Filk', 'BpLblmouLG7W6skmw3zF9w==', '95cKMbb0LCI0srpDhnJYHA==', 'ADMIN');
INSERT INTO onlineshop.user (name, login, login_hash, login_salt, user_role) VALUES ('Test User', 'test', 'tuK/ZiROmB7PcWw6F/N1eg==', 'kegiMoJIhOayTS82BhFZTw==', 'USER');
DELETE FROM onlineshop.product;
INSERT INTO onlineshop.product (name, description, price) VALUES ('Картошка', 'Большая и сырая', 100.5);
INSERT INTO onlineshop.product (name, description, price) VALUES ('Кастрюля', 'Чугунная и тяжелая', 555.55);
commit;