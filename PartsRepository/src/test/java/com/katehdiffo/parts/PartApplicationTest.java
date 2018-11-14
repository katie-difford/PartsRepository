package com.katehdiffo.parts;

import com.jayway.restassured.http.ContentType;
import com.jayway.restassured.response.Response;
import org.hamcrest.CoreMatchers;
import org.junit.ClassRule;
import org.junit.Rule;
import org.junit.Test;
import ro.pippo.test.PippoRule;
import ro.pippo.test.PippoTest;

import static org.junit.Assert.*;

public class PartApplicationTest extends PippoTest {

    @ClassRule
    public static PippoRule pippoRule = new PippoRule(new PartApplication());

    @Test
    public void testHello() {
        Response response = get("/hello");
        response.then().statusCode(200);
        response.then().contentType(ContentType.HTML);
        assertEquals("Hello World", response.asString());
    }

    @Test
    public void returnEmptyListWhenNoPartsHaveBeenStored() {
        Response response = get("/api/parts");
        response.then().statusCode(200);
        response.then().contentType(ContentType.JSON);
        response.then().body(CoreMatchers.equalTo("[]"));
    }

//    @Test
//    public void returnListOfPartsWhenSomePartsHaveBeenStored() {
//
//    }
}