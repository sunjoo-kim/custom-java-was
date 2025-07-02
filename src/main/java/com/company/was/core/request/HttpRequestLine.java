package com.company.was.core.request;

import java.util.Map;

public record HttpRequestLine(String method, String path, Map<String, String> queryParams, String version) {
    public HttpRequestLine updatePath(String newPath) {
        return new HttpRequestLine(
                this.method,
                newPath,
                this.queryParams,
                this.version
        );
    }
}
