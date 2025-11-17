CREATE DATABASE IF NOT EXISTS javaee CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
USE javaee;

CREATE TABLE DIRECTOR (
  id INT PRIMARY KEY AUTO_INCREMENT,
  first_name VARCHAR(30) NOT NULL,
  last_name  VARCHAR(30) NOT NULL,
  birth_date DATE
);

CREATE TABLE ACTOR (
  id INT PRIMARY KEY AUTO_INCREMENT,
  first_name VARCHAR(30) NOT NULL,
  last_name  VARCHAR(30) NOT NULL,
  birth_date DATE
);

CREATE TABLE MOVIE (
  id INT PRIMARY KEY AUTO_INCREMENT,
  title VARCHAR(300) NOT NULL,
  release_year INT NOT NULL,
  genre VARCHAR(60),
  runtime_min INT,
  plot_one_line TEXT,
  poster_url VARCHAR(1000),
  imdb_id VARCHAR(20) UNIQUE,
  director_id INT NOT NULL,
  CONSTRAINT uq_movie_title_year UNIQUE (title, release_year),
  CONSTRAINT chk_runtime CHECK (runtime_min IS NULL OR runtime_min > 0),
  CONSTRAINT fk_movie_director FOREIGN KEY (director_id) REFERENCES DIRECTOR(id)
);

CREATE TABLE MOVIE_ACTORS (
  movie_id INT NOT NULL,
  actor_id INT NOT NULL,
  PRIMARY KEY (movie_id, actor_id),
  FOREIGN KEY (movie_id) REFERENCES MOVIE(id),
  FOREIGN KEY (actor_id) REFERENCES ACTOR(id)
);
