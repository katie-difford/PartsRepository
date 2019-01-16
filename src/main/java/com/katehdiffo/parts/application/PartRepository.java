package com.katehdiffo.parts.application;

import com.katehdiffo.parts.model.Part;

import java.util.List;
import java.util.Optional;

public interface PartRepository {

    List<Part> getAll();

    Optional<Part> findById(long id);

    void save(Part part);

    void delete(Part foundPart);
}