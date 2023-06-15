MERGE into MPA_RATING values (1, 'G');
MERGE into MPA_RATING values (2, 'PG');
MERGE into MPA_RATING values (3, 'PG-13');
MERGE into MPA_RATING values (4, 'R');
MERGE into MPA_RATING values (5, 'NC-17');

MERGE into PUBLIC.GENRES (GENRE_ID, NAME) VALUES (1, 'Комедия');
MERGE into PUBLIC.GENRES (GENRE_ID, NAME) VALUES (2, 'Драма');
MERGE into PUBLIC.GENRES (GENRE_ID, NAME) VALUES (3, 'Мультфильм');
MERGE into PUBLIC.GENRES (GENRE_ID, NAME) VALUES (4, 'Триллер');
MERGE into PUBLIC.GENRES (GENRE_ID, NAME) VALUES (5, 'Документальный');
MERGE into PUBLIC.GENRES (GENRE_ID, NAME) VALUES (6, 'Боевик');