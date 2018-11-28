package com.katehdiffo.parts;

import ro.pippo.core.Application;

import java.util.List;
import java.util.Optional;
import java.util.function.Supplier;

import static java.lang.String.format;
import static java.util.Collections.singletonMap;
import static ro.pippo.core.util.StringUtils.isNullOrEmpty;

public class PartApplication extends Application {
    private final List<Part> parts;
    private final Supplier<Long> idSupplier;
    private final PartValidator partValidator;

    public PartApplication(List<Part> parts, Supplier<Long> idSupplier, PartValidator partValidator) {
        this.parts = parts;
        this.idSupplier = idSupplier;
        this.partValidator = partValidator;
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
            Part part = routeContext.createEntityFromBody(Part.class);
            part.setId(idSupplier.get());

            final Optional<String> validationErrors = partValidator.validate(part);

            if (validationErrors.isPresent()) {
                routeContext.status(400).json().send(singletonMap("error", validationErrors.get()));
            } else {
                parts.add(part);
                routeContext.status(201).json().send(part);
            }
        });
    }
}
