package com.katehdiffo.parts;

import com.fasterxml.jackson.core.JsonParseException;
import com.katehdiffo.parts.web.Response;
import ro.pippo.core.Application;
import ro.pippo.core.PippoRuntimeException;

import java.util.Optional;

import static java.lang.String.format;
import static java.util.Collections.singletonMap;
import static ro.pippo.core.HttpConstants.StatusCode.BAD_REQUEST;
import static ro.pippo.core.HttpConstants.StatusCode.INTERNAL_ERROR;

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
            try {
                final Part updatedPart = routeContext.createEntityFromBody(Part.class);

                final Response response = createPartService.update(id, updatedPart);

                routeContext.getResponse().status(response.getStatusCode())
                        .json(response.getBody());
            } catch (PippoRuntimeException e) {
                final Throwable cause = e.getCause();
                if (cause instanceof JsonParseException) {
                    routeContext.getResponse().status(BAD_REQUEST)
                            .json(singletonMap("error", "Invalid part"));
                }
                routeContext.getResponse().status(INTERNAL_ERROR)
                        .json(singletonMap("error", cause.getMessage()));
            } catch (Exception e) {
                routeContext.getResponse().status(INTERNAL_ERROR)
                        .json(singletonMap("error", e.getMessage()));
            }

        });
    }
}

