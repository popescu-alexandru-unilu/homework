package com.home.backend.service;

import java.util.List;

import com.home.backend.model.Director;

import jakarta.ejb.Stateless;

/**
 * Service for managing Director entities.
 * Extends BaseService for common CRUD operations.
 */
@Stateless
public class DirectorService extends BaseService<Director> {

    @Override
    protected Class<Director> getEntityClass() {
        return Director.class;
    }

    /**
     * Retrieves all directors ordered by last name then first name.
     * @return List of directors
     */
    @Override
    public List<Director> findAll() {
        return entityManager.createQuery(
            "SELECT d FROM Director d ORDER BY d.lastName, d.firstName",
            Director.class
        ).getResultList();
    }

    /**
     * Finds a director by ID, returning null if not found.
     * @param id Director ID
     * @return Director or null
     */
    public Director findDirectorById(Long id) {
        return super.findById(id).orElse(null);
    }

    /**
     * Seeds the database with default directors if empty (development only).
     */
    public void ensureDevSeed() {
        if (count() == 0L) {
            Director nolan = new Director();
            nolan.setFirstName("Christopher");
            nolan.setLastName("Nolan");
            nolan.setBirthDate(java.sql.Date.valueOf("1970-07-30").toLocalDate());
            create(nolan);

            Director gerwig = new Director();
            gerwig.setFirstName("Greta");
            gerwig.setLastName("Gerwig");
            gerwig.setBirthDate(java.sql.Date.valueOf("1983-08-04").toLocalDate());
            create(gerwig);
        }
    }
}
