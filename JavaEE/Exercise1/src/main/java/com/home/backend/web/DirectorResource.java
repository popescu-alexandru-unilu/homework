package com.home.backend.web;

import java.util.List;

import com.home.backend.model.Director;
import com.home.backend.service.DirectorService;

import jakarta.inject.Inject;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

/**
 * REST resource for managing directors.
 * Provides endpoints to retrieve director information.
 */
@Path("/directors")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class DirectorResource {

    @Inject
    private DirectorService directorService;

    /**
     * Retrieves all directors.
     * @return Response with list of directors or error
     */
    @GET
    public Response getAllDirectors() {
        try {
            List<Director> directors = directorService.findAll();
            return Response.ok(directors).build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                .entity("Error retrieving directors: " + e.getMessage())
                .build();
        }
    }

    /**
     * Retrieves a specific director by ID.
     * @param id Director ID
     * @return Response with director or error
     */
    @GET
    @Path("/{id}")
    public Response getDirectorById(@PathParam("id") Long id) {
        try {
            Director director = directorService.findDirectorById(id);
            if (director == null) {
                return Response.status(Response.Status.NOT_FOUND)
                    .entity("Director not found with id: " + id)
                    .build();
            }
            return Response.ok(director).build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                .entity("Error retrieving director: " + e.getMessage())
                .build();
        }
    }
}
