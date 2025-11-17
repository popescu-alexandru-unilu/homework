package com.home.backend.service;

import java.util.List;

import com.home.backend.model.Actor;

import jakarta.ejb.Stateless;

/**
 * Service for managing Actor entities.
 * Extends BaseService for common CRUD operations.
 */
@Stateless
public class ActorService extends BaseService<Actor> {

    @Override
    protected Class<Actor> getEntityClass() {
        return Actor.class;
    }

    /**
     * Retrieves all actors ordered by last name then first name.
     * @return List of all actors
     */
    @Override
    public List<Actor> findAll() {
        return entityManager.createQuery(
            "SELECT a FROM Actor a ORDER BY a.lastName, a.firstName",
            Actor.class
        ).getResultList();
    }

    /**
     * Finds multiple actors by their IDs.
     * @param ids List of actor IDs
     * @return List of actors matching the provided IDs
     */
    public List<Actor> findByIds(List<Long> ids) {
        if (ids == null || ids.isEmpty()) {
            return List.of();
        }
        return entityManager.createQuery(
            "SELECT a FROM Actor a WHERE a.id IN :ids ORDER BY a.lastName, a.firstName",
            Actor.class
        ).setParameter("ids", ids).getResultList();
    }

    /**
     * Seeds the database with default actors if empty (development only).
     */
    public void ensureDevSeed() {
        if (count() == 0L) {
            // Add some famous actors
            createActor("Leonardo", "DiCaprio", java.sql.Date.valueOf("1974-11-11").toLocalDate());
            createActor("Brad", "Pitt", java.sql.Date.valueOf("1963-12-18").toLocalDate());
            createActor("Angelina", "Jolie", java.sql.Date.valueOf("1975-06-04").toLocalDate());
        }
    }

    /**
     * Helper method to create and persist an actor.
     */
    private void createActor(String firstName, String lastName, java.time.LocalDate birthDate) {
        Actor actor = new Actor();
        actor.setFirstName(firstName);
        actor.setLastName(lastName);
        actor.setBirthDate(birthDate);
        create(actor);
    }
}
