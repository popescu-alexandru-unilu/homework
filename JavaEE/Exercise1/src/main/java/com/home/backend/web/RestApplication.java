package com.home.backend.web;

import jakarta.ws.rs.ApplicationPath;
import jakarta.ws.rs.core.Application;

@ApplicationPath("/api")
public class RestApplication extends Application {
    // JAX-RS will automatically discover and register all @Path annotated classes
    // in the same package or subpackages
}
