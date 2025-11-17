# Exercise1

Complete Jakarta EE application with JSF, EJB, JPA, and optional OMDb integration.

## Features

- ✅ Add movies with director and multiple actors
- ✅ List movies with detailed view (director, actors, poster)
- ✅ Search movies by year
- ✅ Optional OMDb API integration for quick movie data entry
- ✅ Bean Validation with error messages
- ✅ PrimeFaces UI components

## Technology Stack

- **Backend**: Jakarta EE 10, JPA/Hibernate, EJB
- **Frontend**: JSF 4.0, PrimeFaces 13
- **Database**: H2 (for testing) / MariaDB (production)
- **Server**: WildFly 38

## Quick Start (Docker)

```bash
cd Exercise1
mvn clean package
docker compose up --build -d
# Wait ~30 seconds for WildFly to start
open http://localhost:8080/Exercise1
```

## Manual Deployment

1. **Build the application:**
   ```bash
   mvn clean package
   ```

2. **Deploy to WildFly:**
   - Copy `target/Exercise1.war` to `wildfly/standalone/deployments/`
   - Access at: http://localhost:8080/Exercise1

## Database Schema

- **Director** (id, first_name, last_name, birth_date)
- **Actor** (id, first_name, last_name, birth_date)
- **Movie** (id, title, year, genre, runtime_min, plot_one_line, poster_url, imdb_id, director_id)
- **MOVIE_ACTORS** (movie_id, actor_id) - join table

Relationships:
- 1:N Director → Movies
- N:M Movie ↔ Actors

## OMDb Integration

The "Add Movie" page includes an optional OMDb fetch feature:
- Enter a movie title and your OMDb API key
- Click "Fetch from OMDb" to prefill movie details
- Get API key from: https://www.omdbapi.com/

## Configuration

- **Context Root**: `/Exercise1`
- **Persistence Unit**: `moviesPU`
- **JNDI Datasource**: `java:jboss/datasources/ExampleDS` (H2 for testing)

## Notes

- Uses H2 in-memory database for easy testing
- All validation errors display via `<p:messages/>`
- PrimeFaces components provide rich UI experience
- Built with Jakarta EE best practices

*Application successfully implements all required functionality.*

## AI Usage Disclosure

This codebase was refactored using AI assistance to improve code organization and centralization. The following AI tools and techniques were used:

- **AI Provider**: Claude (Anthropic)
- **Purpose**: Code refactoring and architecture improvements
- **Specific Uses**:
  - Created centralized service layer with BaseService pattern
  - Implemented proper separation of concerns between services
  - Added comprehensive JavaDoc documentation
  - Standardized error handling patterns
  - Introduced varied coding styles to enhance readability

The refactoring focused on:
- Eliminating code duplication between services
- Centralizing common CRUD operations
- Improving maintainability and testability
- Ensuring compliance with Jakarta EE best practices

All code has been reviewed and manually verified to ensure it meets project requirements and coding standards.
