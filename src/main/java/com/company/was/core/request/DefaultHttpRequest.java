package com.company.was.core.request;

import java.util.Map;

public record DefaultHttpRequest(HttpRequestLine requestLine, Map<String, String> headers, HttpRequestBody body) implements HttpRequest {
    public String getClassPath() {
        return getResourcePath()
                .replace("/", ".")      // 경로 구분자 → 패키지 구분자
                .replace(".java", "");  // 확장자 제거
    }

    public String getResourcePath() {
        String originalPath = this.requestLine().path();
        return originalPath.startsWith("/") ? originalPath.substring(1) : originalPath;
    }
}
