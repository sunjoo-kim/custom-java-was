package com.company.was.core.response;

import java.io.BufferedWriter;
import java.io.IOException;
import java.util.Map;

public class HttpResponseHeaders {
    private final Map<String, String> headers;

    public HttpResponseHeaders(Map<String, String> headers) {
        this.headers = headers;
    }

    public void writeTo(BufferedWriter out) throws IOException {
        for (Map.Entry<String, String> entry : headers.entrySet()) {
            out.write(entry.getKey() + ": " + entry.getValue() + "\r\n");
        }
    }
}
