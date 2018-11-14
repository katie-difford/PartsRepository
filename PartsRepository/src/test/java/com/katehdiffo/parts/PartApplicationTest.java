package com.katehdiffo.parts;

import com.jayway.restassured.http.ContentType;
import com.jayway.restassured.response.Response;
import com.sun.tools.javac.util.List;
import org.hamcrest.CoreMatchers;
import org.junit.Before;
import org.junit.ClassRule;
import org.junit.Test;
import ro.pippo.test.PippoRule;
import ro.pippo.test.PippoTest;

import java.util.ArrayList;

import static com.jayway.restassured.http.ContentType.*;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertEquals;

public class PartApplicationTest extends PippoTest {

    private static final ArrayList<Part> parts = new ArrayList<>();

    @ClassRule
    public static PippoRule pippoRule = new PippoRule(new PartApplication(parts));

    @Before
    public void setUp() throws Exception {
        parts.clear();
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
}