package com.company.was.core.response;

import java.io.BufferedWriter;
import java.io.IOException;

public record HttpResponseStatusLine(String version, int statusCode, String reasonPhrase) {
    public void writeTo(BufferedWriter out) throws IOException {
        out.write(version + " " + statusCode + " " + reasonPhrase + "\r\n");
    }
}
