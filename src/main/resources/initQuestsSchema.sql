CREATE SCHEMA IF NOT EXISTS QUESTS;
/*Create tables*/
CREATE TABLE QUESTS.QUEST
(
    id    INTEGER PRIMARY KEY AUTO_INCREMENT,
    title TEXT NOT NULL,
    author_id INTEGER,
    FOREIGN KEY (author_id) REFERENCES USERS.USER_ (ID) ON DELETE SET NULL
);

CREATE TABLE QUESTS.QUESTION
(
    id    INTEGER PRIMARY KEY AUTO_INCREMENT,
    title TEXT NOT NULL
);

CREATE TABLE QUESTS.ANSWER
(
    id    INTEGER PRIMARY KEY AUTO_INCREMENT,
    title TEXT NOT NULL
);

CREATE TABLE QUESTS.RESULT
(
    id    INTEGER PRIMARY KEY AUTO_INCREMENT,
    title TEXT NOT NULL,
    victory BOOLEAN NOT NULL
);

/*Init tables*/
INSERT INTO QUESTS.QUEST (title, author_id)
VALUES ('JavaRush Quest', 1);

INSERT INTO QUESTS.QUESTION (title)
VALUES ('Ты потерял память. Принять вызов НЛО?'),
       ('Ты принял вызов. Поднимаешься на мостик к капитану?'),
       ('Ты поднялся на мостик. Ты кто?');

INSERT INTO QUESTS.ANSWER (title)
VALUES ('Отклонить вызов'),
       ('Принять вызов'),
       ('Отказаться подниматься на мостик'),
       ('Подняться на мостик'),
       ('Солгать о себе'),
       ('Рассказать правду о себе');

INSERT INTO QUESTS.RESULT (title, victory)
VALUES ('Тебя вернули домой. Победа', TRUE),
       ('Ты отклонил вызов. Поражение', FALSE),
       ('Ты не пошел на переговоры. Поражение', FALSE),
       ('Твою ложь разоблачили. Поражение', FALSE);
/*Setting up connections*/

CREATE TABLE QUESTS.QUESTION_ANSWER
(
    question_id INTEGER,
    answer_id   INTEGER,
    FOREIGN KEY (question_id) REFERENCES QUESTS.QUESTION (ID) ON DELETE CASCADE,
    FOREIGN KEY (answer_id) REFERENCES QUESTS.ANSWER (ID) ON DELETE CASCADE
);

INSERT INTO QUESTS.QUESTION_ANSWER (question_id, answer_id)
VALUES (1, 1),
       (1, 2),
       (2, 3),
       (2, 4),
       (3, 5),
       (3, 6);

CREATE TABLE QUESTS.ANSWER_NEXT_QUESTION
(
    answer_id   INTEGER,
    question_id INTEGER,
    FOREIGN KEY (answer_id) REFERENCES QUESTS.ANSWER (ID) ON DELETE CASCADE,
    FOREIGN KEY (question_id) REFERENCES QUESTS.QUESTION (ID) ON DELETE CASCADE
);

INSERT INTO QUESTS.ANSWER_NEXT_QUESTION (answer_id, question_id)
VALUES (2, 2),
       (4, 3);

CREATE TABLE QUESTS.ANSWER_RESULT
(
    answer_id INTEGER,
    result_id INTEGER,
    FOREIGN KEY (answer_id) REFERENCES QUESTS.ANSWER (ID) ON DELETE CASCADE,
    FOREIGN KEY (result_id) REFERENCES QUESTS.RESULT (ID) ON DELETE CASCADE
);

INSERT INTO QUESTS.ANSWER_RESULT (answer_id, result_id)
VALUES (1, 2),
       (3, 3),
       (5, 4),
       (6, 1);

CREATE TABLE QUESTS.QUEST_FIRST_QUESTION
(
    quest_id    INTEGER,
    question_id INTEGER,
    FOREIGN KEY (quest_id) REFERENCES QUESTS.QUEST (ID) ON DELETE CASCADE,
    FOREIGN KEY (question_id) REFERENCES QUESTS.QUESTION (ID) ON DELETE CASCADE
);

INSERT INTO QUESTS.QUEST_FIRST_QUESTION (quest_id, question_id)
VALUES (1, 1);