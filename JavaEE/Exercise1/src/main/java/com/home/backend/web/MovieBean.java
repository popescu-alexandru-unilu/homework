package com.home.backend.web;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import com.home.backend.model.Actor;
import com.home.backend.model.Director;
import com.home.backend.model.Movie;
import com.home.backend.service.ActorService;
import com.home.backend.service.DirectorService;
import com.home.backend.service.MovieService;
import com.home.backend.service.OmdbService;

import jakarta.annotation.PostConstruct;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import jakarta.json.JsonObject;

/**
 * JSF backing bean for movie management operations.
 * Handles movie CRUD, search, and OMDb integration.
 */
@Named
@ViewScoped
public class MovieBean implements Serializable {

    @Inject
    private DirectorService directorService;

    @Inject
    private ActorService actorService;

    @Inject
    private MovieService movieService;

    @Inject
    private OmdbService omdbService;

    private List<Movie> all;
    private Movie selected;
    private Movie form;
    private Long directorId;
    private List<Director> directors;
    private Long formDirectorId;
    private List<Long> formActorIds;
    private Integer searchYear;
    private List<Movie> searchResults;
    private String omdbTitle;
    private String omdbApiKey;

    @PostConstruct
    public void init() {
        directors = directorService.findAll();
        // DEV SAFETY: seed directors if list is empty
        if (directors == null || directors.isEmpty()) {
            directorService.ensureDevSeed();
            directors = directorService.findAll();
        }

        // Also ensure actors are seeded
        List<Actor> actors = actorService.findAll();
        if (actors == null || actors.isEmpty()) {
            actorService.ensureDevSeed();
        }

        try {
            all = movieService.listAll();
        } catch (Exception e) {
            // Database might not be ready yet, initialize empty list
            all = new ArrayList<>();
        }
        form = new Movie();
        formActorIds = new ArrayList<>();
    }

    public void loadDetails(Long id) {
        try {
            selected = movieService.findWithDetails(id);
        } catch (Exception e) {
            selected = null;
            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "Failed to load movie details: " + e.getMessage()));
        }
    }

    public String searchByYear() {
        FacesContext fc = FacesContext.getCurrentInstance();
        if (searchYear == null) {
            fc.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,
                    "Please enter a valid year.", null));
            return null; // stay on page
        }
        try {
            searchResults = movieService.findByYear(searchYear);
            if (searchResults.isEmpty()) {
                fc.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN,
                        "No movies found for " + searchYear + ".", null));
            }
        } catch (Exception e) {
            fc.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,
                    "Search failed. See server log.", e.getMessage()));
        }
        return null;
    }

    public void save() {
        try {
            Director dir = directorService.findDirectorById(directorId);
            form.setDirector(dir);
            movieService.addActors(form, formActorIds);
            movieService.create(form);

            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_INFO, "Saved", "Movie saved successfully"));

            // refresh list and reset form
            try {
                all = movieService.listAll();
            } catch (Exception e) {
                all = new ArrayList<>();
            }
            form = new Movie();
            directorId = null;
            formActorIds = new ArrayList<>();
        } catch (Exception ex) {
            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", ex.getMessage()));
        }
    }

    public void fetchOmdb() {
        if (omdbTitle == null || omdbTitle.trim().isEmpty() || omdbApiKey == null || omdbApiKey.trim().isEmpty()) {
            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_WARN, "Warning", "Please provide both title and API key"));
            return;
        }

        JsonObject movieData = omdbService.fetchMovie(omdbTitle.trim(), omdbApiKey.trim());
        if (movieData == null || !"True".equals(movieData.getString("Response", "False"))) {
            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "Movie not found or API error"));
            return;
        }

        // Prefill form fields
        form.setTitle(movieData.getString("Title", ""));
        var y = movieData.getString("Year","").replaceAll("\\D","");
        if (!y.isEmpty()) form.setYear(Integer.valueOf(y));
        form.setGenre(movieData.getString("Genre",""));
        var runtime = movieData.getString("Runtime","").replaceAll("\\D","");
        if (!runtime.isEmpty()) form.setRuntimeMin(Integer.valueOf(runtime));
        form.setPlotOneLine(movieData.getString("Plot",""));
        form.setPosterUrl(movieData.getString("Poster",""));
        form.setImdbId(movieData.getString("imdbID",""));

        FacesContext.getCurrentInstance().addMessage(null,
                new FacesMessage(FacesMessage.SEVERITY_INFO, "Fetched", "Movie data fetched from OMDb"));
    }

    private Integer parseRuntime(String runtime) {
        if (runtime == null || runtime.trim().isEmpty()) return null;
        try {
            // Extract number from "180 min" format
            String[] parts = runtime.split(" ");
            return Integer.valueOf(parts[0]);
        } catch (Exception e) {
            return null;
        }
    }

    public String actorNames(Movie m) {
        if (m == null || m.getActors() == null || m.getActors().isEmpty()) return "";
        return m.getActors().stream().map(Actor::getFullName).collect(Collectors.joining(", "));
    }

    // Getters / Setters
    public List<Movie> getAll() { return all; }
    public Movie getSelected() { return selected; }
    public void setSelected(Movie selected) { this.selected = selected; }
    public Movie getForm() { return form; }
    public void setForm(Movie form) { this.form = form; }
    public Long getDirectorId() { return directorId; }
    public void setDirectorId(Long directorId) { this.directorId = directorId; }
    public List<Director> getDirectors() { return directors; }
    public Long getFormDirectorId() { return formDirectorId; }
    public void setFormDirectorId(Long formDirectorId) { this.formDirectorId = formDirectorId; }
    public List<Long> getFormActorIds() { return formActorIds; }
    public void setFormActorIds(List<Long> formActorIds) { this.formActorIds = formActorIds; }
    public Integer getSearchYear() { return searchYear; }
    public void setSearchYear(Integer searchYear) { this.searchYear = searchYear; }
    public List<Movie> getSearchResults() { return searchResults; }
    public String getOmdbTitle() { return omdbTitle; }
    public void setOmdbTitle(String omdbTitle) { this.omdbTitle = omdbTitle; }
    public String getOmdbApiKey() { return omdbApiKey; }
    public void setOmdbApiKey(String omdbApiKey) { this.omdbApiKey = omdbApiKey; }
}
