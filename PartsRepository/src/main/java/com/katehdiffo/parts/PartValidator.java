package com.katehdiffo.parts;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static java.lang.String.format;
import static java.util.Objects.isNull;
import static java.util.stream.Collectors.joining;
import static org.eclipse.jetty.util.LazyList.isEmpty;
import static org.eclipse.jetty.util.StringUtil.isBlank;

public class PartValidator {

    public Optional<String> validateForCreate(Part part) {
        List<String> emptyFields = new ArrayList<>();
        List<String> missingFields = new ArrayList<>();

        if (isNull(part.getName())) {
            missingFields.add("name");
        } else if (isBlank(part.getName())) {
            emptyFields.add("name");
        }

        if (isNull(part.getType())) {
            missingFields.add("type");
        } else if (isBlank(part.getType())) {
            emptyFields.add("type");
        }

        if (isNull(part.getQuantity())) {
            missingFields.add("quantity");
        }

        if (!missingFields.isEmpty()) {
            final String formattedMissingFields = missingFields.stream().collect(joining(", "));

            return Optional.of(format("Missing required field(s): %s", formattedMissingFields));
        }

        if (!emptyFields.isEmpty()) {
            final String formattedEmptyFields = emptyFields.stream().collect(joining(", "));

            return Optional.of(format("The following fields are empty: %s", formattedEmptyFields));
        }

        return Optional.empty();
    }

    public Optional<String> validateForUpdate(Part part) {
//        List<String> emptyFields = new ArrayList<>();
//
//        if (isEmpty(part.getName())) {
//            emptyFields.add("name");
//        }
//
//       if (isEmpty(part.getType())) {
//            emptyFields.add("type");
//        }
//
//        if (!emptyFields.isEmpty()) {
//            final String formattedEmptyFields = emptyFields.stream().collect(joining(", "));
//
//            return Optional.of(format("The following fields are empty: %s", formattedEmptyFields));
//        }
//

        System.out.println("Part was okay");
        return Optional.empty();
    }
}