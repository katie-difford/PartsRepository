package com.katehdiffo.parts;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

import static java.util.Objects.isNull;
import static java.util.stream.Collectors.toList;
import static org.eclipse.jetty.util.StringUtil.isBlank;

public class PartValidator {

    public List<String> validateForCreate(Part part) {
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

        Stream<String> emptyStream = emptyFields.stream().map(field -> String.format("Empty field: %s", field));
        Stream<String> missingStream = missingFields.stream().map(field -> String.format("Missing field: %s", field));

        return Stream.concat(emptyStream, missingStream).collect(toList());


//        if (!missingFields.isEmpty()) {
//            final String formattedMissingFields = missingFields.stream().collect(joining(", "));
//
//            return Optional.of(format("Missing required field(s): %s", formattedMissingFields));
//        }
//
//        if (!emptyFields.isEmpty()) {
//            final String formattedEmptyFields = emptyFields.stream().collect(joining(", "));
//
//            return Optional.of(format("The following fields are empty: %s", formattedEmptyFields));
//        }

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