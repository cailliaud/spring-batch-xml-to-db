DROP TABLE USERS IF EXISTS;

CREATE TABLE USERS  (
    id BIGINT IDENTITY NOT NULL PRIMARY KEY,
    email VARCHAR(50),
    first_name VARCHAR(20),
    last_name VARCHAR(20)
);