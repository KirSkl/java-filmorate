# java-filmorate
Аналог кинопоиска. REST API приложение с хранением информации о пользователях и фильмах в Базе Данных. (Spring Boot, Maven, JUnit, JDBC, H2-database, SQL)

![Схема базы данных, использeумая в приложении]([filmorate-sql-scheme.png](https://github.com/KirSkl/java-filmorate/blob/add-sql-diagramm/filmorate-sql-scheme.png))

films
Эта таблица содержит данные о фильмах. Первичный ключ - id фильма.

users
Эта таблица содержит данные о пользователях

likes
Эта таблица содержит информацию о лайках, который пользователи поставили фильмам. Состоит из двух ключей (составного первичного ключа):
film_id - id фильма, которому поставили лайк
user_id - id пользователя, который поставил лайк. 

friends
В таблице содержится информация о предложениях дружбы и их статусе
oferror_id - ID пользователя, который предложил дружбу
acceptor_id - ID пользователя, которому предложили дружбу
status - содержит информацию о том, было предложение о дружбе принято или отклонено
Первые две колонки составляют составной первичный ключ

Примеры запросов:
Получение продолжительности фильма:
SELECT duration FROM films WHERE film_id = 1

Получение адреса электронной почты пользователя по его логину:
SELECT email FROM users WHERE login = 'Xatab'

Получение информации о том, какие пользователи лайкнули фильм:
SELECT user_id FROM likes WHERE film_id = 1

Получение списка друзей пользователя c id = 1:

SELECT offeror_id, acceptor_id FROM friends WHERE (offeror_id = 1 OR acceptor_id = 1) AND status = 'true'
