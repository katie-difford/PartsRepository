package com.katehdiffo.parts.web;

import com.fasterxml.jackson.core.JsonParseException;
import com.katehdiffo.parts.application.PartRepository;
import com.katehdiffo.parts.application.PartService;
import com.katehdiffo.parts.model.Part;
import ro.pippo.core.Application;
import ro.pippo.core.PippoRuntimeException;

import java.util.Optional;

import static java.lang.String.format;
import static java.util.Collections.singletonMap;
import static ro.pippo.core.HttpConstants.StatusCode.BAD_REQUEST;
import static ro.pippo.core.HttpConstants.StatusCode.INTERNAL_ERROR;

public class PartApplication extends Application {
    private final PartRepository partRepository;
    private final PartService partService;

    public PartApplication(PartRepository partRepository, PartService partService) {
        this.partRepository = partRepository;
        this.partService = partService;
    }

    @Override
    protected void onInit() {
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
            final Response response = partService.create(routeContext.getRequest());
            routeContext.getResponse()
                    .status(response.getStatusCode())
                    .json(response.getBody());
        });

        PATCH("/api/parts/{id}", routeContext -> {
            long id = routeContext.getParameter("id").toLong(0);
            try {
                final Part updatedPart = routeContext.createEntityFromBody(Part.class);

                final Response response = partService.update(id, updatedPart);

                routeContext.getResponse().status(response.getStatusCode())
                        .json(response.getBody());
            } catch (PippoRuntimeException e) {
                final Throwable cause = e.getCause();
                if (cause instanceof JsonParseException) {
                    routeContext.getResponse().status(BAD_REQUEST)
                            .json(singletonMap("error", format("Part with id %s has some fields missing", id)));
                }
                routeContext.getResponse().status(INTERNAL_ERROR)
                        .json(singletonMap("error", cause.getMessage()));
            } catch (Exception e) {
                routeContext.getResponse().status(INTERNAL_ERROR)
                        .json(singletonMap("error", e.getMessage()));
            }

        });

        DELETE("/api/parts/{id}", routeContext -> {
            long id = routeContext.getParameter("id").toLong(0);

            Response response = partService.delete(id);

            routeContext.getResponse()
                    .status(response.getStatusCode())
                    .json(response.getBody());
        });
    }
}

