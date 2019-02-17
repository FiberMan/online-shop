DROP TABLE IF EXISTS onlineshop.user;
CREATE TABLE `onlineshop`.`user` (
  `user_id` INT NOT NULL AUTO_INCREMENT,
  `name` VARCHAR(70) NULL,
  `login` VARCHAR(45) NOT NULL,
  `login_hash` VARCHAR(100) NOT NULL,
  `login_salt` VARCHAR(100) NOT NULL,
  `user_role` VARCHAR(15) NOT NULL,
  PRIMARY KEY (`user_id`),
  UNIQUE INDEX `login_UNIQUE` (`login` ASC) VISIBLE
);

DROP TABLE IF EXISTS onlineshop.product;
CREATE TABLE onlineshop.product (
  product_id INT NOT NULL AUTO_INCREMENT,
  name VARCHAR(50) NOT NULL,
  description VARCHAR(250),
  price DECIMAL(10,2),
  PRIMARY KEY (product_id)
);