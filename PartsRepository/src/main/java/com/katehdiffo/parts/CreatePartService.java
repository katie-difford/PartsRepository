package com.katehdiffo.parts;

import com.fasterxml.jackson.core.JsonParseException;
import com.katehdiffo.parts.web.Response;
import ro.pippo.core.PippoRuntimeException;
import ro.pippo.core.Request;

import java.util.List;
import java.util.Optional;
import java.util.function.Supplier;

import static com.katehdiffo.parts.web.Response.response;
import static java.util.Collections.singletonMap;
import static ro.pippo.core.HttpConstants.StatusCode.BAD_REQUEST;
import static ro.pippo.core.HttpConstants.StatusCode.CREATED;
import static ro.pippo.core.HttpConstants.StatusCode.INTERNAL_ERROR;

public class CreatePartService {

    private final List<Part> parts;
    private final Supplier<Long> idSupplier;
    private final PartValidator partValidator;

    public CreatePartService(List<Part> parts, Supplier<Long> idSupplier, PartValidator partValidator) {
        this.parts = parts;
        this.idSupplier = idSupplier;
        this.partValidator = partValidator;
    }

    public Response create(Request request) {
//        try {
//            final Part part = routeContext.createEntityFromBody(Part.class);
//            part.setId(idSupplier.get());
//
//            final Optional<String> validationErrors = partValidator.validate(part);
//
//            if (validationErrors.isPresent()) {
//                routeContext.status(400).json().send(singletonMap("error", validationErrors.get()));
//            } else {
//                parts.add(part);
//                routeContext.status(201).json().send(part);
//            }
//        } catch (PippoRuntimeException e) {
//            final Throwable cause = e.getCause();
//            if (cause instanceof JsonParseException) {
//                routeContext.status(400).json().send(singletonMap("error", "Invalid part"));
//            }
//        }

        try {
            final Part part = request.createEntityFromBody(Part.class);
            part.setId(idSupplier.get());

            final Optional<String> validationErrors = partValidator.validate(part);

            if (validationErrors.isPresent()) {
                return response(BAD_REQUEST, singletonMap("error", validationErrors.get()));
            }

            parts.add(part);

            return response(CREATED, part);
        } catch (PippoRuntimeException e) {
            final Throwable cause = e.getCause();
            if (cause instanceof JsonParseException) {
                return response(BAD_REQUEST, singletonMap("error", "Invalid part"));
            }
            return response(INTERNAL_ERROR, singletonMap("error", cause.getMessage()));
        } catch (Exception e) {
            return response(INTERNAL_ERROR, singletonMap("error", e.getMessage()));
        }
    }
}