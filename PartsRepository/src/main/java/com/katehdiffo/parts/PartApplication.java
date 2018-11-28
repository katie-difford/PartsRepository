package com.katehdiffo.parts;

import ro.pippo.core.Application;

import java.util.*;
import java.util.function.Supplier;

import static java.lang.String.format;
import static java.lang.String.valueOf;
import static java.util.Collections.singletonMap;
import static ro.pippo.core.util.StringUtils.isNullOrEmpty;

public class PartApplication extends Application {
    private final List<Part> parts;
    private final Supplier<Long> idSupplier;

    public PartApplication(List<Part> parts, Supplier<Long> idSupplier) {
        this.parts = parts;
        this.idSupplier = idSupplier;
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

            if (oneOrMoreFieldsMissing(part)) {
                routeContext.status(400).json().send(singletonMap("error", format("One or more fields is missing")));
            } else {
                parts.add(part);
                routeContext.status(201).json().send(part);
            }
        });
    }

    private boolean oneOrMoreFieldsMissing(Part part) {
        boolean nameIsNotPresent = isNullOrEmpty(part.getName());
        boolean typeIsNotPresent = isNullOrEmpty(part.getType());
        boolean quantityIsNotPresent = isNullOrEmpty(valueOf(part.getQuantity()));

        if (nameIsNotPresent || typeIsNotPresent || quantityIsNotPresent) {
            return true;
        } else {
            return false;
        }
    }
}
