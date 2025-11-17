package com.home.backend.service;

import jakarta.ejb.Stateless;
import jakarta.json.JsonObject;
import jakarta.ws.rs.client.ClientBuilder;
import jakarta.ws.rs.core.MediaType;

@Stateless
public class OmdbService {
    private static final String API = "https://www.omdbapi.com/";

    public JsonObject fetchMovie(String title, String apiKey) {
        var client = ClientBuilder.newClient();
        var resp = client.target(API)
            .queryParam("t", title)
            .queryParam("apikey", apiKey)
            .request(MediaType.APPLICATION_JSON_TYPE)
            .get();
        if (resp.getStatus() != 200) throw new RuntimeException("OMDb HTTP " + resp.getStatus());
        return resp.readEntity(JsonObject.class);
    }
}
