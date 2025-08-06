CREATE SCHEMA IF NOT EXISTS USERS;
/*Create tables*/
CREATE TABLE USERS.ROLE
(
    id   INTEGER PRIMARY KEY AUTO_INCREMENT,
    role TEXT NOT NULL
);

CREATE TABLE USERS.USER_
(
    id       INTEGER PRIMARY KEY AUTO_INCREMENT,
    login    TEXT UNIQUE NOT NULL,
    password TEXT NOT NULL,
    role_id  INTEGER,
    victory  INTEGER,
    defeat   INTEGER,
    image    TEXT,
    FOREIGN KEY (role_id) REFERENCES USERS.ROLE (ID)
);

/*Init tables*/
INSERT INTO USERS.ROLE (role)
VALUES ('ADMIN'),
       ('USER');

INSERT INTO USERS.USER_ (login, password, role_id, victory, defeat, image)
VALUES ('Admin', 'admin', 1, 0, 0, 'Admin.png');
