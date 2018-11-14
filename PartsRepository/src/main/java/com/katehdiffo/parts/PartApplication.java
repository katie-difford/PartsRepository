package com.katehdiffo.parts;

import ro.pippo.core.Application;

import java.util.*;

import static java.lang.String.format;
import static java.util.Collections.singletonMap;
import static java.util.stream.LongStream.iterate;

public class PartApplication extends Application {
    private static final Iterator<Long> ids = iterate(1, i -> i + 1).iterator();
    private final List<Part> parts;

    public PartApplication(List<Part> parts) {
        this.parts = parts;
    }

    @Override
    protected void onInit() {
        GET("/hello", routeContext -> {
            routeContext.send("Hello World");
        });

        GET("/api/parts", routeContext -> {
            routeContext.json().send(parts);
        });

        GET("/api/parts/{id}", routeContext -> {
            // retrieve some parameters from request
            long id = routeContext.getParameter("id").toLong(0);

            Optional<Part> foundPart = parts.stream()
                    .filter(part -> part.getId() == id)
                    .findAny();

            if (foundPart.isPresent()) {
                routeContext.json().send(foundPart.get());
            } else {
                routeContext.status(404).json().send(singletonMap("error", format("Part with id %d not found", id)));
            }

        });

        POST("/api/parts", routeContext -> {
            Part part = routeContext.createEntityFromBody(Part.class);
            part.setId(ids.next());
            parts.add(part);
            routeContext.status(201).json().send(part);
        });
    }
}
