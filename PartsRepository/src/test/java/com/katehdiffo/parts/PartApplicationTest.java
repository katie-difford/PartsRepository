package com.katehdiffo.parts;

import com.jayway.restassured.response.Response;
import org.junit.Before;
import org.junit.ClassRule;
import org.junit.Test;
import org.mockito.Mockito;
import ro.pippo.test.PippoRule;
import ro.pippo.test.PippoTest;

import java.util.ArrayList;
import java.util.function.Supplier;

import static com.jayway.restassured.http.ContentType.HTML;
import static com.jayway.restassured.http.ContentType.JSON;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;

public class PartApplicationTest extends PippoTest {

    private static final ArrayList<Part> parts = new ArrayList<>();
    private static Supplier<Long> ids = mock(Supplier.class);
    private static final PartValidator partValidator = new PartValidator();
    private static final PartRepository partRepository = new InMemoryPartRepository(parts, ids);

    @ClassRule
    public static PippoRule pippoRule = new PippoRule(new PartApplication(partRepository, new PartService(partRepository, partValidator)));

    @Before
    public void setUp() throws Exception {
        parts.clear();

        Mockito.when(ids.get()).thenReturn(1L);
    }

    @Test
    public void testHello() {
        Response response = get("/hello");
        response.then().statusCode(200);
        response.then().contentType(HTML);
        assertEquals("Hello World", response.asString());
    }

    @Test
    public void returnEmptyListWhenNoPartsHaveBeenStored() {
        Response response = get("/api/parts");
        response.then().statusCode(200);
        response.then().contentType(JSON);
        response.then().body(equalTo("[]"));
    }

    @Test
    public void returnListOfPartsWhenSomePartsHaveBeenStored() {
        Part part = new Part("Part1", "PartType", 5);
        Part part2 = new Part("Part2", "PartType2", 10);

        part.setId(1L);
        part2.setId(2L);

        parts.add(part);
        parts.add(part2);

        Response response = get("/api/parts");
        response.then().statusCode(200);
        response.then().contentType(JSON);
        response.then().body("[0].id", equalTo(1));
        response.then().body("[0].name", equalTo("Part1"));
        response.then().body("[0].type", equalTo("PartType"));
        response.then().body("[0].quantity", equalTo(5));

        response.then().body("[1].id", equalTo(2));
        response.then().body("[1].name", equalTo("Part2"));
        response.then().body("[1].type", equalTo("PartType2"));
        response.then().body("[1].quantity", equalTo(10));
    }

    @Test
    public void returnASpecificPart() {
        // given
        Part part = new Part("Part1", "PartType", 5);
        part.setId(1L);
        parts.add(part);

        // when
        Response response = get("/api/parts/1");

        // then
        response.then().statusCode(200);
        response.then().contentType(JSON);
        response.then().body("id", equalTo(1));
        response.then().body("name", equalTo("Part1"));
        response.then().body("type", equalTo("PartType"));
        response.then().body("quantity", equalTo(5));
    }

    @Test
    public void return404WhenSpecifiedPartDoesNotExist() {
        Response response = get("/api/parts/1");

        response.then().statusCode(404);
        response.then().contentType(JSON);
        response.then().body("error", equalTo("Part with id 1 not found"));
    }

    @Test
    public void createAPart() {
        Response response = given()
                .contentType(JSON)
                .body("{\n" +
                        "  \"name\": \"name\",\n" +
                        "  \"type\": \"type\",\n" +
                        "  \"quantity\": 1\n" +
                        "}")
                .post("/api/parts");

        response.then().statusCode(201);
        response.then().contentType(JSON);
        response.then().body("id", equalTo(1));
        response.then().body("name", equalTo("name"));
        response.then().body("type", equalTo("type"));
        response.then().body("quantity", equalTo(1));

        final Part expectedPart = new Part("name", "type", 1);
        expectedPart.setId(1);

        assertThat(partRepository.getAll()).containsExactly(expectedPart);
    }

    @Test
    public void createMultiplePartsGetSequentialIds() {
        Mockito.when(ids.get()).thenReturn(1L).thenReturn(50L);

        given()
                .contentType(JSON)
                .body("{\n" +
                        "  \"name\": \"name1\",\n" +
                        "  \"type\": \"type1\",\n" +
                        "  \"quantity\": 1\n" +
                        "}")
                .post("/api/parts");

        Response response = given()
                .contentType(JSON)
                .body("{\n" +
                        "  \"name\": \"name2\",\n" +
                        "  \"type\": \"type2\",\n" +
                        "  \"quantity\": 2\n" +
                        "}")
                .post("/api/parts");

        response.then().statusCode(201);
        response.then().contentType(JSON);
        response.then().body("id", equalTo(50));
        response.then().body("name", equalTo("name2"));
        response.then().body("type", equalTo("type2"));
        response.then().body("quantity", equalTo(2));

        final Part expectedPart1 = new Part("name1", "type1", 1);
        expectedPart1.setId(1);
        final Part expectedPart2 = new Part("name2", "type2", 2);
        expectedPart2.setId(50);

        assertThat(partRepository.getAll()).containsExactly(expectedPart1, expectedPart2);
    }

    @Test
    public void ignorePartIdIfIdIsPassedIn() {
        Mockito.when(ids.get()).thenReturn(1L).thenReturn(2L);

        given()
                .contentType(JSON)
                .body("{\n" +
                        "  \"name\": \"name1\",\n" +
                        "  \"type\": \"type1\",\n" +
                        "  \"quantity\": 1\n" +
                        "}")
                .post("/api/parts");

        Response response = given()
                .contentType(JSON)
                .body("{\n" +
                        "  \"id\": \"1\",\n" +
                        "  \"name\": \"name2\",\n" +
                        "  \"type\": \"type2\",\n" +
                        "  \"quantity\": 2\n" +
                        "}")
                .post("/api/parts");

        response.then().statusCode(201);
        response.then().contentType(JSON);
        response.then().body("id", equalTo(2));
        response.then().body("name", equalTo("name2"));
        response.then().body("type", equalTo("type2"));
        response.then().body("quantity", equalTo(2));

        final Part expectedPart1 = new Part("name1", "type1", 1);
        expectedPart1.setId(1);
        final Part expectedPart2 = new Part("name2", "type2", 2);
        expectedPart2.setId(2);

        assertThat(partRepository.getAll()).containsExactly(expectedPart1, expectedPart2);
    }

    @Test
    public void return400IfAFieldIsMissing() {
        Response response = given()
                .contentType(JSON)
                .body("{\n" +
                        "  \"type\": \"type1\",\n" +
                        "  \"quantity\": 1\n" +
                        "}")
                .post("/api/parts");

        response.then().statusCode(400);
        response.then().contentType(JSON);
        response.then().body("error", equalTo("Invalid part: [Missing field: name]"));
    }

    @Test
    public void return400WhenFieldHasWrongInputType() {
        Response response = given()
                .contentType(JSON)
                .body("{\n" +
                        "  \"name\": \"name2\",\n" +
                        "  \"type\": type,\n" +
                        "  \"quantity\": 1\n" +
                        "}")
                .post("/api/parts");
        response.then().statusCode(400);
        response.then().contentType(JSON);
        response.then().body("error", equalTo("Invalid part"));
    }

    @Test
    public void updateOneFieldOfAPreExistingPart() {
        Part firstPart = new Part("FirstName", "Type1", 1);

        partRepository.save(firstPart);

        Response response = given()
                .contentType(JSON)
                .body("{\n" +
                        "  \"name\": \"SecondName\"\n" +
                        "}")
                .patch("/api/parts/1");

        response.then().statusCode(200);
        response.then().contentType(JSON);
        response.then().body("id", equalTo(1));
        response.then().body("name", equalTo("SecondName"));
        response.then().body("type", equalTo("Type1"));
        response.then().body("quantity", equalTo(1));

        assertThat(firstPart.getName()).isEqualTo("SecondName");
    }

    @Test
    public void updateAllFieldsOfAPreExistingPart() {
        Part firstPart = new Part("AnotherPart", "Type1", 1);

        partRepository.save(firstPart);

        Response response = given()
                .contentType(JSON)
                .body("{\n" +
                        "  \"name\": \"SecondName\",\n" +
                        "  \"type\": \"Type2\",\n" +
                        "  \"quantity\": 5\n" +
                        "}")
                .patch("/api/parts/1");

        response.then().statusCode(200);
        response.then().contentType(JSON);
        response.then().body("id", equalTo(1));
        response.then().body("name", equalTo("SecondName"));
        response.then().body("type", equalTo("Type2"));
        response.then().body("quantity", equalTo(5));

        assertThat(firstPart.getName()).isEqualTo("SecondName");
        assertThat(firstPart.getType()).isEqualTo("Type2");
        assertThat(firstPart.getQuantity()).isEqualTo(5);
    }

    @Test
    public void return404IfPartIsNotPresent() {
        Response response = given()
                .contentType(JSON)
                .body("{\n" +
                        "  \"name\": \"SecondName\",\n" +
                        "  \"type\": \"Type2\",\n" +
                        "  \"quantity\": 5\n" +
                        "}")
                .patch("/api/parts/1");

        response.then().statusCode(404);
        response.then().contentType(JSON);
        response.then().body("error", equalTo("Part with id 1 not found"));
    }

    @Test
    public void return400IfUpdatingFieldWithInvalidValue() {
        Part firstPart = new Part("AnotherPart", "Type1", 1);

        partRepository.save(firstPart);

        Response response = given()
                .contentType(JSON)
                .body("{\n" +
                        "  \"name\": \"\"," +
                        "}")
                .patch("/api/parts/1");

        response.then().statusCode(400);
        response.then().contentType(JSON);
        //TODO - does this need to specify the field which is missing, or is the below error message enough.
        response.then().body("error", equalTo("Part with id 1 has some fields missing"));
    }
}