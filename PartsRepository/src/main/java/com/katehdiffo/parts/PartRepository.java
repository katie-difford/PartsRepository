package com.katehdiffo.parts;

import com.katehdiffo.parts.web.Response;

import java.util.List;
import java.util.Optional;

public interface PartRepository {

    List<Part> getAll();

    Optional<Part> findById(long id);

    void save(Part part);

    void update(Part part);
}