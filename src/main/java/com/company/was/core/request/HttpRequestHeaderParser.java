package com.company.was.core.request;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class HttpRequestHeaderParser {

    private static final Logger logger = LoggerFactory.getLogger(HttpRequestHeaderParser.class);

    public Map<String, String> execute(final BufferedReader in) throws IOException {
        final Map<String, String> headers = new HashMap<>();
        while (true) {
            final String line = in.readLine();
            if (line == null || line.trim().isEmpty()) break;
            int idx = line.indexOf(":");
            if (idx > 0) {
                final String key = line.substring(0, idx).trim();
                final String value = line.substring(idx + 1).trim();
                logger.info("header: {}: {}", key, value);
                headers.put(key, value);
            }
        }
        return headers;
    }
}
