package com.company.was.core.response;

import com.company.was.config.Config;
import com.company.was.config.ConfigLoader;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;

public record HttpResponse(HttpResponseStatusLine statusLine, HttpResponseHeaders headers, HttpResponseBody body) {

    private static final Config config = ConfigLoader.load("config/config.json");

    public static HttpResponse getOkResponse(HttpResponseBody responseBody) {
        // 상태 라인 작성
        HttpResponseStatusLine statusLine = new HttpResponseStatusLine("HTTP/1.1", 200, "OK");

        // 헤더 작성
        HashMap<String, String> responseHeadersMap = new HashMap<>();
        responseHeadersMap.put("Content-Type", "text/html");
        HttpResponseHeaders responseHeaders = new HttpResponseHeaders(responseHeadersMap);

        return new HttpResponse(statusLine, responseHeaders, responseBody);
    }

    public static HttpResponse getNotFoundResponse() {
        return getNotFoundResponse("a.com");
    }

    public static HttpResponse getForbiddenResponse() {
        return getForbiddenResponse("a.com");
    }

    public static HttpResponse getNotFoundResponse(String host) {
        // 상태 라인 작성
        HttpResponseStatusLine statusLine = new HttpResponseStatusLine("HTTP/1.1", 400, "Bad Request");

        // 헤더 작성
        HashMap<String, String> responseHeadersMap = new HashMap<>();
        responseHeadersMap.put("Content-Type", "text/html");
        HttpResponseHeaders responseHeaders = new HttpResponseHeaders(responseHeadersMap);
        // 본문 작성
        HttpResponseBody responseBody = null;
        String name = "com/error/404.html";
        config.getVirtualHost(host).errorPages().getOrDefault("404", name);
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(HttpResponse.class.getClassLoader().getResourceAsStream(name)))) {
            String fullContent = reader.lines().reduce("", (acc, line) -> acc + line + "\n");
            if (fullContent.isEmpty()) {
                return HttpResponse.getNotFoundResponse();
            }
            responseBody = new HttpResponseBody(fullContent);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return new HttpResponse(statusLine, responseHeaders, responseBody);
    }
    public static HttpResponse getForbiddenResponse(String host) {
        // 상태 라인 작성
        HttpResponseStatusLine statusLine = new HttpResponseStatusLine("HTTP/1.1", 403, "Forbidden");

        // 헤더 작성
        HashMap<String, String> responseHeadersMap = new HashMap<>();
        responseHeadersMap.put("Content-Type", "text/html");
        HttpResponseHeaders responseHeaders = new HttpResponseHeaders(responseHeadersMap);
        // 본문 작성
        HttpResponseBody responseBody = null;
        String name = "com/error/403.html";
        config.getVirtualHost(host).errorPages().getOrDefault("403", name);
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(HttpResponse.class.getClassLoader().getResourceAsStream(name)))) {
            String fullContent = reader.lines().reduce("", (acc, line) -> acc + line + "\n");
            if (fullContent.isEmpty()) {
                return HttpResponse.getForbiddenResponse();
            }
            responseBody = new HttpResponseBody(fullContent);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return new HttpResponse(statusLine, responseHeaders, responseBody);
    }
}
