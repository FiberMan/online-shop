CREATE SCHEMA `onlineshop` DEFAULT CHARACTER SET utf8;

DROP TABLE IF EXISTS onlineshop.user;

CREATE TABLE `onlineshop`.`user` (
  `user_id` INT NOT NULL AUTO_INCREMENT,
  `name` VARCHAR(70) NULL,
  `login` VARCHAR(45) NOT NULL,
  `login_hash` VARCHAR(100) NOT NULL,
  `login_salt` VARCHAR(100) NOT NULL,
  `user_role` VARCHAR(15) NOT NULL,
  PRIMARY KEY (`user_id`),
  UNIQUE INDEX `login_UNIQUE` (`login` ASC) VISIBLE);

INSERT INTO onlineshop.user (name, login, login_hash, login_salt, user_role) VALUES ('Y F', 'Filk', 'BpLblmouLG7W6skmw3zF9w==', '95cKMbb0LCI0srpDhnJYHA==', 'ADMIN');
INSERT INTO onlineshop.user (name, login, login_hash, login_salt, user_role) VALUES ('Test User', 'test', 'tuK/ZiROmB7PcWw6F/N1eg==', 'kegiMoJIhOayTS82BhFZTw==', 'USER');
commit;

--select * from user;




DROP TABLE IF EXISTS onlineshop.product;

CREATE TABLE onlineshop.product (
  product_id INT NOT NULL AUTO_INCREMENT,
  name VARCHAR(50) NOT NULL,
  description VARCHAR(250),
  price DECIMAL(10,2),
  PRIMARY KEY (product_id)
);

INSERT INTO onlineshop.product (name, description, price) VALUES ('Картошка', 'Большая и сырая', 100.5);
INSERT INTO onlineshop.product (name, description, price) VALUES ('Кастрюля', 'Чугунная и тяжелая', 555.55);
commit;

SELECT * FROM onlineshop.product;