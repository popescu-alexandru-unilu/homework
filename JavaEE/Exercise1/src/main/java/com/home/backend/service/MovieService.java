package com.home.backend.service;

import java.util.List;
import java.util.Set;

import com.home.backend.model.Actor;
import com.home.backend.model.Director;
import com.home.backend.model.Movie;

import jakarta.ejb.Stateless;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;

/**
 * Service for managing Movie entities and their relationships.
 * Handles movie CRUD operations and associations with directors/actors.
 */
@Stateless
@Named("movieService")
public class MovieService {

    @PersistenceContext
    private EntityManager entityManager;

    @Inject
    private DirectorService directorService;

    @Inject
    private ActorService actorService;

    /**
     * Gets all movies from database, including their director info.
     * Movies are sorted alphabetically by title, then by release year.
     * @return Collection of all movies with director details
     */
    public List<Movie> listAll() {
        // JPQL query to fetch movies with eager loading of director relationship
        String queryStr = "SELECT m FROM Movie m JOIN FETCH m.director ORDER BY m.title, m.year";
        return entityManager.createQuery(queryStr, Movie.class).getResultList();
    }

    /**
     * Retrieves a single movie with full details.
     * Loads director and all associated actors in one query to avoid N+1 problem.
     * @param id The unique identifier of the movie
     * @return Movie object with all relationships loaded, or null if not found
     */
    public Movie findWithDetails(Long id) {
        // Complex query to load movie with all related entities
        List<Movie> results = entityManager.createQuery(
            "SELECT DISTINCT m FROM Movie m " +
            "LEFT JOIN FETCH m.director " +
            "LEFT JOIN FETCH m.actors " +
            "WHERE m.id = :id",
            Movie.class
        ).setParameter("id", id).getResultList();

        // Return first result or null if no movie found
        return results.isEmpty() ? null : results.get(0);
    }

    /**
     * Finds movies by release year.
     * @param year Release year
     * @return List of movies from that year
     */
    public List<Movie> findByYear(Integer year) {
        return entityManager.createQuery(
            "SELECT DISTINCT m FROM Movie m " +
            "JOIN FETCH m.director " +
            "LEFT JOIN FETCH m.actors " +
            "WHERE m.year = :year ORDER BY m.title",
            Movie.class
        ).setParameter("year", year).getResultList();
    }

    /**
     * Creates a new movie in the database.
     * @param movie The movie to create
     */
    public void create(Movie movie) {
        entityManager.persist(movie);
    }

    /**
     * Associates actors with a movie.
     * @param movie The movie
     * @param actorIds IDs of actors to associate
     */
    public void addActors(Movie movie, Iterable<Long> actorIds) {
        List<Long> idList = new java.util.ArrayList<>();
        actorIds.forEach(idList::add);
        Set<Actor> selectedActors = Set.copyOf(actorService.findByIds(idList));
        movie.setActors(selectedActors);
    }

    // Delegated methods for backward compatibility
    public List<Director> allDirectors() {
        return directorService.findAll();
    }

    public Director findDirector(Long id) {
        return directorService.findDirectorById(id);
    }

    public List<Actor> allActors() {
        return actorService.findAll();
    }

    public List<Actor> findActorsByIds(Iterable<Long> ids) {
        List<Long> idList = new java.util.ArrayList<>();
        ids.forEach(idList::add);
        return actorService.findByIds(idList);
    }
}
