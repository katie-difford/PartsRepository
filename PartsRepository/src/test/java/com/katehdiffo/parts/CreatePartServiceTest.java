package com.katehdiffo.parts;

import com.fasterxml.jackson.core.JsonParseException;
import com.katehdiffo.parts.web.Response;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import ro.pippo.core.PippoRuntimeException;
import ro.pippo.core.Request;

import static java.util.Collections.singletonList;
import static java.util.Collections.singletonMap;
import static java.util.Optional.empty;
import static java.util.Optional.of;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static ro.pippo.core.HttpConstants.StatusCode.*;

@RunWith(MockitoJUnitRunner.class)
public class CreatePartServiceTest {

    @Mock
    private PartRepository partRepository;
    @Mock
    private PartValidator partValidator;
    @Mock
    private Request request;
    @Mock
    private PippoRuntimeException pippoRuntimeException;
    @Mock
    private JsonParseException jsonParseException;

    private Part part;

    private PartService underTest;

    @Before
    public void setUp() throws Exception {
        part = new Part("Name", "Type", 2211);
        part.setId(1);

        when(request.createEntityFromBody(Part.class)).thenReturn(part);

        underTest = new PartService(partRepository, partValidator);
    }

    @Test
    public void createReturnsSuccessfulResponse() {
        final Response response = underTest.create(request);

        assertThat(response.getStatusCode()).isEqualTo(CREATED);
        assertThat(response.getBody()).isEqualTo(part);
    }

    @Test
    public void createSavesPart() {
        underTest.create(request);

        verify(partRepository).save(part);
    }

    @Test
    public void createReturnsBadRequestResponseIfCreatedPartIsInvalid() {
        when(partValidator.validateForCreate(part)).thenReturn(singletonList("Missing field: name"));

        final Response response = underTest.create(request);

        assertThat(response.getStatusCode()).isEqualTo(BAD_REQUEST);
        assertThat(response.getBody()).isEqualTo(singletonMap("error", "Invalid part: [Missing field: name]"));
    }

    @Test
    public void createReturnsBadRequestResponseIfPartCannotBeParsed() {
        when(pippoRuntimeException.getCause()).thenReturn(jsonParseException);
        when(request.createEntityFromBody(Part.class)).thenThrow(pippoRuntimeException);

        final Response response = underTest.create(request);

        assertThat(response.getStatusCode()).isEqualTo(BAD_REQUEST);
        assertThat(response.getBody()).isEqualTo(singletonMap("error", "Invalid part"));
    }

    @Test
    public void createReturnsBadRequestResponseIfPippoRuntimeExceptionIsThrown() {
        when(pippoRuntimeException.getCause()).thenReturn(new RuntimeException("boom!"));
        when(request.createEntityFromBody(Part.class)).thenThrow(pippoRuntimeException);

        final Response response = underTest.create(request);

        assertThat(response.getStatusCode()).isEqualTo(INTERNAL_ERROR);
        assertThat(response.getBody()).isEqualTo(singletonMap("error", "boom!"));
    }

    @Test
    public void updateReturnsBadRequestResponseIfUpdatedPartIsInvalid() {
        when(partRepository.findById(1L)).thenReturn(of(part));
        when(partValidator.validateForUpdate(part)).thenReturn(singletonList("Empty field: name"));

        final Response response = underTest.update(part.getId(), part);

        assertThat(response.getStatusCode()).isEqualTo(BAD_REQUEST);
        assertThat(response.getBody()).isEqualTo(singletonMap("error", "Invalid part: [Empty field: name]"));
    }

    @Test
    public void deleteReturnsBadRequestIfPartRequestedDoesNotExist() {
        when(partRepository.findById(1L)).thenReturn(empty());

        final Response response = underTest.delete(part.getId());

        assertThat(response.getStatusCode()).isEqualTo(NOT_FOUND);
        assertThat(response.getBody()).isEqualTo(singletonMap("error", "Part with id 1 not found"));
    }
}