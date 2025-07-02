package com.company.was.core.filter;

import com.company.was.core.request.HttpRequest;
import com.company.was.core.request.HttpRequestLine;

import java.util.Collections;

public class MockHttpRequest implements HttpRequest {
    private final String path;

    public MockHttpRequest(String path) {
        this.path = path;
    }

    @Override
    public HttpRequestLine requestLine() {
        return new HttpRequestLine("GET", path, Collections.EMPTY_MAP, "HTTP/1.1");
    }
}
