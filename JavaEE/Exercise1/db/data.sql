INSERT INTO DIRECTOR(first_name,last_name,birth_date)
VALUES ('Christopher','Nolan','1970-07-30'),
       ('Greta','Gerwig','1983-08-04');

INSERT INTO ACTOR(first_name,last_name,birth_date)
VALUES ('Cillian','Murphy','1976-05-25'),
       ('Emily','Blunt','1983-02-23'),
       ('Ryan','Gosling','1980-11-12'),
       ('Margot','Robbie','1990-07-02');

INSERT INTO MOVIE(title,release_year,genre,runtime_min,plot_one_line,poster_url,imdb_id,director_id)
VALUES (
  'Oppenheimer', 2023, 'Biography, Drama, History', 180,
  'A physicist leads the Manhattan Project.',
  'https://example.com/posters/oppenheimer.jpg',
  'tt15398776',
  (SELECT id FROM DIRECTOR WHERE first_name='Christopher' AND last_name='Nolan')
);

INSERT INTO MOVIE_ACTORS(movie_id,actor_id)
VALUES (
  (SELECT id FROM MOVIE WHERE title='Oppenheimer' AND release_year=2023),
  (SELECT id FROM ACTOR WHERE first_name='Cillian' AND last_name='Murphy')
),(
  (SELECT id FROM MOVIE WHERE title='Oppenheimer' AND release_year=2023),
  (SELECT id FROM ACTOR WHERE first_name='Emily' AND last_name='Blunt')
);
