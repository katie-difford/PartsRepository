package com.katehdiffo.parts;

import org.junit.Before;
import org.junit.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class PartValidatorTest {

    private PartValidator underTest;

    @Before
    public void setUp() throws Exception {
        underTest = new PartValidator();
    }

    @Test
    public void validateReturnsEmptyIfPartIsValid() {
        Part part = new Part("Name", "Type", 3);

        final List<String> error = underTest.validateForCreate(part);

        assertThat(error).isEmpty();
    }

    @Test
    public void validateReturnsErrorMessageWhenPartNameIsEmptyString() {
        Part part = new Part("", "Type", 4);

        final List<String> error = underTest.validateForCreate(part);

        assertThat(error).contains("Empty field: name");
    }

    @Test
    public void validateReturnsErrorMessageWhenPartNameIsBlank() {
        Part part = new Part("                  ", "Type", 4);

        final List<String> error = underTest.validateForCreate(part);

        assertThat(error).contains("Empty field: name");
    }

    @Test
    public void validateReturnsErrorMessageWhenPartNameIsNull() {
        Part part = new Part(null, "Type", 4);

        final List<String> error = underTest.validateForCreate(part);

        assertThat(error).contains("Missing field: name");
    }

    @Test
    public void validateReturnsErrorMessageWhenPartTypeIsEmptyString() {
        Part part = new Part("Name", "", 4);

        final List<String> error = underTest.validateForCreate(part);

        assertThat(error).contains("Empty field: type");
    }

    @Test
    public void validateReturnsErrorMessageWhenPartTypeIsBlank() {
        Part part = new Part("Name", "              ", 4);

        final List<String> error = underTest.validateForCreate(part);

        assertThat(error).contains("Empty field: type");
    }

    @Test
    public void validateReturnsErrorMessageWhenPartTypeIsNull() {
        Part part = new Part("Name", null, 4);

        final List<String> error = underTest.validateForCreate(part);

        assertThat(error).contains("Missing field: type");
    }

    @Test
    public void validateReturnsErrorMessageWhenPartQuantityIsNull() {
        Part part = new Part("Name", "Type", null);

        final List<String> error = underTest.validateForCreate(part);

        assertThat(error).contains("Missing field: quantity");
    }

    @Test
    public void validateReturnsErrorMessageWhenMultipleFieldsAreMissing() {
        Part part = new Part(null, null, null);

        final List<String> error = underTest.validateForCreate(part);

        assertThat(error).contains("Missing field: name", "Missing field: type", "Missing field: quantity");
    }

    @Test
    public void validateReturnsNoErrorsWhenUpdatingOneFieldOfAPart() {
        Part part = new Part("   ", "   ", null);

        final List<String> error = underTest.validateForUpdate(part);

        assertThat(error).contains("Empty field: name", "Empty field: type");
    }
}