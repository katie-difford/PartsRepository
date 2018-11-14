package com.katehdiffo.parts;

import com.jayway.restassured.http.ContentType;
import com.jayway.restassured.response.Response;
import org.junit.Test;
import ro.pippo.test.PippoTest;

import static org.junit.Assert.*;

public class PartApplicationTest extends PippoTest {

    @Test
    public void testHello() {
        Response response = get("/hello");
        response.then().statusCode(200);
        response.then().contentType(ContentType.HTML);
        assertEquals("Hello World", response.asString());
    }
}