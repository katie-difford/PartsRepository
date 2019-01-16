package com.katehdiffo.parts;

import com.fasterxml.jackson.core.JsonParseException;
import com.katehdiffo.parts.web.Response;
import ro.pippo.core.PippoRuntimeException;
import ro.pippo.core.Request;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Predicate;
import java.util.function.Supplier;

import static com.katehdiffo.parts.web.Response.response;
import static java.lang.String.format;
import static java.util.Collections.singletonMap;
import static ro.pippo.core.HttpConstants.StatusCode.*;
import static ro.pippo.core.util.StringUtils.isNullOrEmpty;

class PartService {

    private final PartRepository partRepository;
    private final PartValidator partValidator;

    PartService(PartRepository partRepository, PartValidator partValidator) {
        this.partRepository = partRepository;
        this.partValidator = partValidator;
    }

    Response create(Request request) {
        try {
            final Part part = request.createEntityFromBody(Part.class);

            final List<String> validationErrors = partValidator.validateForCreate(part);

            if (!validationErrors.isEmpty()) {
                return response(BAD_REQUEST, singletonMap("error", format("Invalid part: %s", validationErrors)));
            }

            partRepository.save(part);

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

    Response update(Long id, Part partWithUpdatedFields) {
        Optional<Part> foundPart = partRepository.findById(id);

        if (foundPart.isPresent()) {
            Part part = foundPart.get();

            final List<String> validationErrors = partValidator.validateForUpdate(partWithUpdatedFields);

            if (!validationErrors.isEmpty()) {
                return response(BAD_REQUEST, singletonMap("error", format("Invalid part: %s", validationErrors)));
            } else {
                updateField(partWithUpdatedFields::getName, part::setName, field -> !isNullOrEmpty(field));
                updateField(partWithUpdatedFields::getType, part::setType, field -> !isNullOrEmpty(field));
                updateField(partWithUpdatedFields::getQuantity, part::setQuantity, Objects::nonNull);
            }

            return response(OK, part);
        } else {
            return response(NOT_FOUND, singletonMap("error", format("Part with id %s not found", id)));
        }
    }

    private <T> void updateField(Supplier<T> newField, Consumer<T> fieldSetter, Predicate<T> fieldShouldBeSet) {
        T field = newField.get();
        if (fieldShouldBeSet.test(field)) {
            fieldSetter.accept(field);
        }
    }

    Response delete(long id) {
        Optional<Part> foundPart = partRepository.findById(id);

        if (foundPart.isPresent()) {
            partRepository.delete(foundPart.get());
            return response(NO_RESPONSE, null);
        } else {
            return response(NOT_FOUND, singletonMap("error", format("Part with id %s does not exist", id)));
        }
    }

    private <T> void updateField(Supplier<T> newField, Consumer<T> fieldSetter, Predicate<T> fieldShouldBeSet) {
        T field = newField.get();
        if (fieldShouldBeSet.test(field)) {
            fieldSetter.accept(field);
        }
    }
}