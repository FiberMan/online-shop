CREATE TABLE IF NOT EXISTS user (
  `user_id` INTEGER PRIMARY KEY AUTOINCREMENT,
  `name` TEXT,
  `login` TEXT NOT NULL,
  `login_hash` TEXT NOT NULL,
  `login_salt` TEXT NOT NULL,
  `user_role` TEXT NOT NULL
);

CREATE TABLE IF NOT EXISTS product (
  `product_id` INTEGER PRIMARY KEY AUTOINCREMENT,
  `name` TEXT NOT NULL,
  `description` TEXT,
  `price` NUMERIC
);