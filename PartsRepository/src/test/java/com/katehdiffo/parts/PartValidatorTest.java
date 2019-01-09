package com.katehdiffo.parts;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import java.util.Optional;

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

        final Optional<String> error = underTest.validateForCreate(part);

        assertThat(error).isEmpty();
    }

    @Test
    public void validateReturnsErrorMessageWhenPartNameIsEmptyString() {
        Part part = new Part("", "Type", 4);

        final Optional<String> error = underTest.validateForCreate(part);

        assertThat(error).contains("The following fields are empty: name");
    }

    @Test
    public void validateReturnsErrorMessageWhenPartNameIsBlank() {
        Part part = new Part("                  ", "Type", 4);

        final Optional<String> error = underTest.validateForCreate(part);

        assertThat(error).contains("The following fields are empty: name");
    }

    @Test
    public void validateReturnsErrorMessageWhenPartNameIsNull() {
        Part part = new Part(null, "Type", 4);

        final Optional<String> error = underTest.validateForCreate(part);

        assertThat(error).contains("Missing required field(s): name");
    }

    @Test
    public void validateReturnsErrorMessageWhenPartTypeIsEmptyString() {
        Part part = new Part("Name", "", 4);

        final Optional<String> error = underTest.validateForCreate(part);

        assertThat(error).contains("The following fields are empty: type");
    }

    @Test
    public void validateReturnsErrorMessageWhenPartTypeIsBlank() {
        Part part = new Part("Name", "              ", 4);

        final Optional<String> error = underTest.validateForCreate(part);

        assertThat(error).contains("The following fields are empty: type");
    }

    @Test
    public void validateReturnsErrorMessageWhenPartTypeIsNull() {
        Part part = new Part("Name", null, 4);

        final Optional<String> error = underTest.validateForCreate(part);

        assertThat(error).contains("Missing required field(s): type");
    }

    @Test
    public void validateReturnsErrorMessageWhenPartQuantityIsNull() {
        Part part = new Part("Name", "Type", null);

        final Optional<String> error = underTest.validateForCreate(part);

        assertThat(error).contains("Missing required field(s): quantity");
    }

    @Test
    public void validateReturnsErrorMessageWhenMultipleFieldsAreMissing() {
        Part part = new Part(null, null, null);

        final Optional<String> error = underTest.validateForCreate(part);

        assertThat(error).contains("Missing required field(s): name, type, quantity");
    }

    //TODO this test name is different to the asserts, seek clarity on this
    @Ignore
    @Test
    public void validateReturnsNoErrorsWhenUpdatingOneFieldOfAPart() {
        Part part = new Part("   ", "   ", null);

        final Optional<String> error = underTest.validateForUpdate(part);

        assertThat(error).contains("The following fields are empty: name, type");
    }
}