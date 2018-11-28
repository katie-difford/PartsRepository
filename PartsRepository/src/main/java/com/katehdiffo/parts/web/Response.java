package com.katehdiffo.parts.web;

import java.util.Objects;

public class Response {
    private final int statusCode;
    private final Object body;

    private Response(int statusCode, Object body) {
        this.statusCode = statusCode;
        this.body = body;
    }

    public static Response response(int statusCode, Object body) {
        return new Response(statusCode, body);
    }

    public int getStatusCode() {
        return statusCode;
    }

    public Object getBody() {
        return body;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Response response = (Response) o;
        return statusCode == response.statusCode &&
                Objects.equals(body, response.body);
    }

    @Override
    public int hashCode() {
        return Objects.hash(statusCode, body);
    }

    @Override
    public String toString() {
        return "Response{" +
                "statusCode=" + statusCode +
                ", body=" + body +
                '}';
    }
}