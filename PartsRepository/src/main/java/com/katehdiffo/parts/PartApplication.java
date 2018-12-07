package com.katehdiffo.parts;

import com.katehdiffo.parts.web.Response;
import ro.pippo.core.Application;

import java.util.Optional;

import static java.lang.String.format;
import static java.util.Collections.singletonMap;

public class PartApplication extends Application {
    private final PartRepository partRepository;
    private final PartService createPartService;

    public PartApplication(PartRepository partRepository, PartService createPartService) {
        this.partRepository = partRepository;
        this.createPartService = createPartService;
    }

    @Override
    protected void onInit() {
        GET("/hello", routeContext -> {
            routeContext.send("Hello World");
        });

        GET("/api/parts", routeContext -> {
            routeContext.json().send(partRepository.getAll());
        });

        GET("/api/parts/{id}", routeContext -> {
            // retrieve some parameters from request
            long id = routeContext.getParameter("id").toLong(0);

            final Optional<Part> foundPart = partRepository.findById(id);

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

        PATCH("/api/parts/{id}", routeContext -> {
            long id = routeContext.getParameter("id").toLong(0);

            final Part part = routeContext.createEntityFromBody(Part.class);

            final Response response = createPartService.update(id, part);

            routeContext.getResponse().status(response.getStatusCode())
                    .json(response.getBody());
        });
    }
}
