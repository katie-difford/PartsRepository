package com.katehdiffo.parts.application;

import com.katehdiffo.parts.model.Part;

import java.util.ArrayList;
import java.util.List;
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

    }

    public List<String> validateForUpdate(Part part) {
        List<String> emptyFields = new ArrayList<>();

        if (isBlank(part.getName())) {
            if (!isNull(part.getName())) {
                emptyFields.add("name");
            }
        }

        if (isBlank(part.getType())) {
            if (!isNull(part.getType())) {
                emptyFields.add("type");
            }
        }

        Stream<String> emptyStream = emptyFields.stream().map(field -> String.format("Empty field: %s", field));

        return emptyStream.collect(toList());
    }
}