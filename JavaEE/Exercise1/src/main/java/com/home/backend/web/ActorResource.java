package com.home.backend.web;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import com.home.backend.model.Actor;
import com.home.backend.service.ActorService;

import jakarta.inject.Inject;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

/**
 * REST resource for managing actors.
 * Provides endpoints to retrieve actor information.
 */
@Path("/actors")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class ActorResource {

    @Inject
    private ActorService actorService;

    /**
     * Retrieves all actors.
     * @return Response with list of actors or error
     */
    @GET
    public Response getAllActors() {
        try {
            List<Actor> actors = actorService.findAll();
            return Response.ok(actors).build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                .entity("Error retrieving actors: " + e.getMessage())
                .build();
        }
    }

    /**
     * Retrieves actors by their IDs.
     * @param idsParam Comma-separated list of actor IDs
     * @return Response with list of actors or error
     */
    @GET
    @Path("/{ids}")
    public Response getActorsByIds(@PathParam("ids") String idsParam) {
        try {
            List<Long> actorIds = Arrays.stream(idsParam.split(","))
                .map(String::trim)
                .filter(s -> !s.isEmpty())
                .map(Long::parseLong)
                .collect(Collectors.toList());

            if (actorIds.isEmpty()) {
                return Response.status(Response.Status.BAD_REQUEST)
                    .entity("At least one actor ID must be provided")
                    .build();
            }

            List<Actor> actors = actorService.findByIds(actorIds);
            return Response.ok(actors).build();
        } catch (NumberFormatException e) {
            return Response.status(Response.Status.BAD_REQUEST)
                .entity("Invalid actor ID format. Use comma-separated numbers.")
                .build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                .entity("Error retrieving actors: " + e.getMessage())
                .build();
        }
    }
}
