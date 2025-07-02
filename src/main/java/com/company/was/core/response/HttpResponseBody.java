package com.company.was.core.response;

import java.io.BufferedWriter;
import java.io.IOException;

public record HttpResponseBody(String body) {
    public void writeTo(BufferedWriter out) throws IOException {
        out.write(body);
    }
}
