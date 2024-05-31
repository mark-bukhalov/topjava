DELETE
FROM user_role
WHERE user_id is not null;

DELETE
FROM users
where id is not null;

DELETE
FROM meals
where id is not null;

ALTER SEQUENCE global_seq RESTART WITH 100000;

INSERT INTO users (name, email, password)
VALUES ('User', 'user@yandex.ru', 'password'),
       ('Admin', 'admin@gmail.com', 'admin'),
       ('Guest', 'guest@gmail.com', 'guest');

INSERT INTO user_role (role, user_id)
VALUES ('USER', 100000),
       ('ADMIN', 100001);

INSERT INTO meals (user_id, date_time, description, calories)
VALUES (100000, '2020-01-30 10:00:00', 'Завтрак [User]', 500),
       (100000, '2020-01-30 13:00:00', 'Обед [User]', 1000),
       (100000, '2020-01-30 20:00:00', 'Ужин [User]', 500),
       (100000, '2020-01-31 13:00:00', 'Обед [User]', 500),
       (100001, '2020-01-31 00:00:00', 'Еда на граничное значение [Admin]', 100),
       (100001, '2020-01-31 10:00:00', 'Завтрак [Admin]', 1000),
       (100001, '2020-01-31 13:00:00', 'Обед [Admin]', 500),
       (100001, '2020-01-31 20:00:00', 'Ужин [Admin]', 410);