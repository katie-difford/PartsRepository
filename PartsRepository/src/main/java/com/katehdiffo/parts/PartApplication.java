package com.katehdiffo.parts;

import com.katehdiffo.parts.web.Response;
import ro.pippo.core.Application;

import java.util.List;
import java.util.Optional;

import static java.lang.String.format;
import static java.util.Collections.singletonMap;

public class PartApplication extends Application {
    private final List<Part> parts;
    private final CreatePartService createPartService;

    public PartApplication(List<Part> parts, CreatePartService createPartService) {
        this.parts = parts;
        this.createPartService = createPartService;
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
                routeContext.status(404).json().send(singletonMap("error", format("Part with id %s not found", id)));
            }

        });

        POST("/api/parts", routeContext -> {
            final Response response = createPartService.create(routeContext.getRequest());
            routeContext.getResponse()
                    .status(response.getStatusCode())
                    .json(response.getBody());
        });
    }
}
