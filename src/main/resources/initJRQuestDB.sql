/*Create tables*/
CREATE TABLE QUEST
(
    id    INTEGER PRIMARY KEY AUTO_INCREMENT,
    title TEXT NOT NULL
);

CREATE TABLE QUESTION
(
    id    INTEGER PRIMARY KEY AUTO_INCREMENT,
    title TEXT NOT NULL
);

CREATE TABLE ANSWER
(
    id    INTEGER PRIMARY KEY AUTO_INCREMENT,
    title TEXT NOT NULL
);

CREATE TABLE RESULT
(
    id    INTEGER PRIMARY KEY AUTO_INCREMENT,
    title TEXT NOT NULL
);

/*Init tables*/
INSERT INTO QUEST (title)
VALUES ('JavaRush Quest');

INSERT INTO QUESTION (title)
VALUES ('Ты потерял память. Принять вызов НЛО?'),
       ('Ты принял вызов. Поднимаешься на мостик к капитану?'),
       ('Ты поднялся на мостик. Ты кто?');

INSERT INTO ANSWER (title)
VALUES ('Отклонить вызов'),
       ('Принять вызов'),
       ('Отказаться подниматься на мостик'),
       ('Подняться на мостик'),
       ('Солгать о себе'),
       ('Рассказать правду о себе');

INSERT INTO RESULT (title)
VALUES ('Тебя вернули домой. Победа'),
       ('Ты отклонил вызов. Поражение'),
       ('Ты не пошел на переговоры. Поражение'),
       ('Твою ложь разоблачили. Поражение');
/*Setting up connections*/

CREATE TABLE QUESTION_ANSWER
(
    question_id INTEGER,
    answer_id   INTEGER,
    FOREIGN KEY (question_id) REFERENCES QUESTION (ID) ON DELETE CASCADE,
    FOREIGN KEY (answer_id) REFERENCES ANSWER (ID) ON DELETE CASCADE
);

INSERT INTO QUESTION_ANSWER (question_id, answer_id)
VALUES (1, 1),
       (1, 2),
       (2, 3),
       (2, 4),
       (3, 5),
       (3, 6);

CREATE TABLE ANSWER_NEXT_QUESTION
(
    answer_id   INTEGER,
    question_id INTEGER,
    FOREIGN KEY (answer_id) REFERENCES ANSWER (ID) ON DELETE CASCADE,
    FOREIGN KEY (question_id) REFERENCES QUESTION (ID) ON DELETE CASCADE
);

INSERT INTO ANSWER_NEXT_QUESTION (answer_id, question_id)
VALUES (2, 2),
       (4, 3);

CREATE TABLE ANSWER_RESULT
(
    answer_id INTEGER,
    result_id INTEGER,
    FOREIGN KEY (answer_id) REFERENCES ANSWER (ID) ON DELETE CASCADE,
    FOREIGN KEY (result_id) REFERENCES RESULT (ID) ON DELETE CASCADE
);

INSERT INTO ANSWER_RESULT (answer_id, result_id)
VALUES (1, 2),
       (3, 3),
       (5, 4),
       (6, 1);

CREATE TABLE QUEST_FIRST_QUESTION
(
    quest_id    INTEGER,
    question_id INTEGER,
    FOREIGN KEY (quest_id) REFERENCES QUEST (ID) ON DELETE CASCADE,
    FOREIGN KEY (question_id) REFERENCES QUESTION (ID) ON DELETE CASCADE
);

INSERT INTO QUEST_FIRST_QUESTION (quest_id, question_id)
VALUES (1, 1);