package com.katehdiffo.parts;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static java.lang.String.format;
import static java.util.stream.Collectors.joining;
import static ro.pippo.core.util.StringUtils.isNullOrEmpty;

public class PartValidator {

    public Optional<String> validate(Part part) {
        List<String> missingFields = new ArrayList<>();

        if (isNullOrEmpty(part.getName())) {
            missingFields.add("name");
        }

        if (isNullOrEmpty(part.getType())) {
            missingFields.add("type");
        }

        if(part.getQuantity() == null) {
            missingFields.add("quantity");
        }

        if(missingFields.isEmpty()) {
            return Optional.empty();
        }

        final String formattedFields = missingFields.stream().collect(joining(", "));
        return Optional.of(format("Missing required field(s): %s", formattedFields));
    }
}