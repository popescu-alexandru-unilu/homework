package com.home.backend.web;

import java.util.List;
import java.util.stream.Collectors;

import com.home.backend.model.Actor;
import com.home.backend.model.Movie;
import com.home.backend.service.MovieService;

import jakarta.inject.Inject;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

@Path("/movies")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class MovieResource {

    @Inject
    private MovieService movieService;

    @GET
    public Response getAllMovies() {
        try {
            List<Movie> movies = movieService.listAll();
            return Response.ok(movies).build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("Error retrieving movies: " + e.getMessage())
                    .build();
        }
    }

    @GET
    @Path("/{id}")
    public Response getMovieById(@PathParam("id") Long id) {
        try {
            Movie movie = movieService.findWithDetails(id);
            if (movie == null) {
                return Response.status(Response.Status.NOT_FOUND)
                        .entity("Movie not found with id: " + id)
                        .build();
            }
            return Response.ok(movie).build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("Error retrieving movie: " + e.getMessage())
                    .build();
        }
    }

    @GET
    @Path("/year/{year}")
    public Response getMoviesByYear(@PathParam("year") Integer year) {
        try {
            List<Movie> movies = movieService.findByYear(year);
            return Response.ok(movies).build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("Error retrieving movies by year: " + e.getMessage())
                    .build();
        }
    }

    @POST
    public Response createMovie(Movie movie) {
        try {
            if (movie.getTitle() == null || movie.getTitle().trim().isEmpty()) {
                return Response.status(Response.Status.BAD_REQUEST)
                        .entity("Movie title is required")
                        .build();
            }
            if (movie.getYear() == null) {
                return Response.status(Response.Status.BAD_REQUEST)
                        .entity("Movie year is required")
                        .build();
            }
            if (movie.getDirector() == null || movie.getDirector().getId() == null) {
                return Response.status(Response.Status.BAD_REQUEST)
                        .entity("Movie director is required")
                        .build();
            }

            movieService.create(movie);

            // Add actors if provided
            if (movie.getActors() != null && !movie.getActors().isEmpty()) {
                List<Long> actorIds = movie.getActors().stream()
                        .map(Actor::getId)
                        .collect(Collectors.toList());
                movieService.addActors(movie, actorIds);
            }

            return Response.status(Response.Status.CREATED)
                    .entity(movie)
                    .build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("Error creating movie: " + e.getMessage())
                    .build();
        }
    }
}
