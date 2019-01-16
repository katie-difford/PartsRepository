package com.katehdiffo.parts;

import java.util.List;
import java.util.Optional;

public interface PartRepository {

    List<Part> getAll();

    Optional<Part> findById(long id);

    void save(Part part);

    void delete(Part foundPart);
}