DROP TABLE IF EXISTS onlineshop.user;
CREATE TABLE onlineshop.user (
  user_id serial primary key,
  name varchar(50) not null,
  login varchar(50) not null,
  login_hash varchar(100) not null,
  login_salt varchar(100) not null,
  user_role varchar(15) not null
);
CREATE UNIQUE INDEX login_UNIQUE ON onlineshop.user (login);

DROP TABLE IF EXISTS onlineshop.product;
CREATE TABLE onlineshop.product
(
    product_id serial primary key,
    name varchar(50) not null,
    description varchar(250),
    price numeric(10, 2)
);