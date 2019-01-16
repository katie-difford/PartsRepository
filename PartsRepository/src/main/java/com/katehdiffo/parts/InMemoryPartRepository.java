package com.katehdiffo.parts;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;
import java.util.function.Supplier;

import static java.util.stream.LongStream.iterate;

public class InMemoryPartRepository implements PartRepository {

    private static final Iterator<Long> ids = iterate(1, i -> i + 1).iterator();

    private final List<Part> parts;
    private final Supplier<Long> idSupplier;

    public InMemoryPartRepository(List<Part> parts, Supplier<Long> idSupplier) {
        this.parts = parts;
        this.idSupplier = idSupplier;
    }

    public InMemoryPartRepository() {
        this.parts = new ArrayList<>();
        this.idSupplier = ids::next;
    }

    @Override
    public List<Part> getAll() {
        return parts;
    }

    @Override
    public Optional<Part> findById(long id) {
        return parts.stream()
                .filter(part -> part.getId() == id)
                .findAny();
    }

    @Override
    public void save(Part part) {
        part.setId(idSupplier.get());
        parts.add(part);
    }

    @Override
    public void delete(Optional<Part> foundPart) {
        parts.remove(foundPart);

    }
}