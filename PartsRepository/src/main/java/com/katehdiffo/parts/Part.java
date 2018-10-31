package com.katehdiffo.parts;

import java.util.Objects;

public class Part {
    private long id;
    private String name;
    private String type;
    private int quantity;

    public Part() {
        // Jackson
    }

    public Part(long id, String name, String type, int quantity) {
        this.name = name;
        this.type = type;
        this.quantity = quantity;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    //redo when I change a field
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Part part = (Part) o;
        return Objects.equals(name, part.name) &&
                Objects.equals(type, part.type) &&
                Objects.equals(quantity, part.quantity);
    }

    @Override
    public int hashCode() {

        return Objects.hash(name, type, quantity);
    }

    @Override
    public String toString() {
        return "Part{" +
                "name='" + name + '\'' +
                ", type='" + type + '\'' +
                ", quantity='" + quantity + '\'' +
                '}';
    }

}
