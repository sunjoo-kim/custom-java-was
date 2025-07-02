package com.company.was.core.request;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;

public class HttpRequestLineParser {

    private static final Logger logger = LoggerFactory.getLogger(HttpRequestLineParser.class);


    public HttpRequestLine execute(final String requestLine) {
        if (requestLine == null) return null;
        final String[] parts = requestLine.split(" ");
        final String method = parts[0];
        final String version = parts[2];

        final String fullPath = parts[1];
        String queryString = "";

        final String[] pathParts = fullPath.split("\\?", 2);
        final String path = pathParts[0];
        if (pathParts.length > 1) {
            queryString = pathParts[1];
        }

        logger.info("Parsed request line parts: fullPath={}", fullPath);
        final Map<String, String> queryParams = parseQueryString(queryString);
        logger.info("Parsed request line: method={}, path={}, queryParams={}, version={}", method, path, queryParams, version);

        return new HttpRequestLine(method, path, queryParams, version);
    }

    private Map<String, String> parseQueryString(final String queryString) {
        final Map<String, String> queryParams = new HashMap<>();
        if (!queryString.isEmpty()) {
            for (final String pair : queryString.split("&")) {
                final String[] kv = pair.split("=", 2);
                if (kv.length == 2) {
                    queryParams.put(kv[0], kv[1]);
                }
            }
        }
        return queryParams;
    }
}
