package com.home.backend.service;

import java.util.List;
import java.util.Optional;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;

/**
 * Base service class providing common CRUD operations.
 * Subclasses should specify the entity type and implement specific business logic.
 */
public abstract class BaseService<T> {

    @PersistenceContext
    protected EntityManager entityManager;

    protected abstract Class<T> getEntityClass();

    /**
     * Locates an entity using its unique identifier.
     * Returns an Optional to handle cases where the entity might not exist.
     * @param id The entity's unique identifier
     * @return Optional container with the entity if found, empty otherwise
     */
    public Optional<T> findById(Long id) {
        // Use entityManager.find for direct lookup by primary key
        T foundEntity = entityManager.find(getEntityClass(), id);
        return Optional.ofNullable(foundEntity);
    }

    /**
     * Fetches all entities of the specified type from the database.
     * Note: This loads all records - use with caution for large datasets.
     * @return Complete list of entities
     */
    public List<T> findAll() {
        // Build JPQL query dynamically using the entity class name
        String jpql = "SELECT e FROM " + getEntityClass().getSimpleName() + " e";
        return entityManager.createQuery(jpql, getEntityClass()).getResultList();
    }

    /**
     * Persists a new entity.
     * @param entity The entity to save
     */
    public void create(T entity) {
        entityManager.persist(entity);
    }

    /**
     * Merges an existing entity.
     * @param entity The entity to update
     * @return The updated entity
     */
    public T update(T entity) {
        return entityManager.merge(entity);
    }

    /**
     * Removes an entity.
     * @param entity The entity to delete
     */
    public void delete(T entity) {
        entityManager.remove(entityManager.contains(entity) ? entity : entityManager.merge(entity));
    }

    /**
     * Counts total entities of this type.
     * @return The total count
     */
    public long count() {
        return entityManager.createQuery(
            "SELECT COUNT(e) FROM " + getEntityClass().getSimpleName() + " e",
            Long.class
        ).getSingleResult();
    }
}
