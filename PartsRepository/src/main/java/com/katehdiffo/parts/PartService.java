package com.katehdiffo.parts;

import com.fasterxml.jackson.core.JsonParseException;
import com.katehdiffo.parts.web.Response;
import ro.pippo.core.PippoRuntimeException;
import ro.pippo.core.Request;

import java.util.Optional;

import static com.katehdiffo.parts.web.Response.response;
import static java.util.Collections.singletonMap;
import static ro.pippo.core.HttpConstants.StatusCode.*;
import static ro.pippo.core.util.StringUtils.isNullOrEmpty;

public class PartService {

    private final PartRepository partRepository;
    private final PartValidator partValidator;

    public PartService(PartRepository partRepository, PartValidator partValidator) {
        this.partRepository = partRepository;
        this.partValidator = partValidator;
    }

    public Response create(Request request) {
        try {
            final Part part = request.createEntityFromBody(Part.class);

            final Optional<String> validationErrors = partValidator.validate(part);

            if (validationErrors.isPresent()) {
                return response(BAD_REQUEST, singletonMap("error", validationErrors.get()));
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

    public Response update(Long id, Part partWithUpdatedFields) {
        Optional<Part> foundPart = partRepository.findById(id);

        Part part = foundPart.get();

        String updatedName = partWithUpdatedFields.getName();
        String updatedType = partWithUpdatedFields.getType();
        Integer updatedQuantity = partWithUpdatedFields.getQuantity();

        if(!isNullOrEmpty(updatedName)) {
            part.setName(updatedName);
        }

        if(!isNullOrEmpty(updatedType)) {
            part.setType(updatedType);
        }

        if(updatedQuantity != null) {
            part.setQuantity(updatedQuantity);
        }

        return response(OK, part);
    }
}