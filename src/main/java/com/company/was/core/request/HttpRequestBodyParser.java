package com.company.was.core.request;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.IOException;

public class HttpRequestBodyParser {

    private static final Logger logger = LoggerFactory.getLogger(HttpRequestBodyParser.class);

    public HttpRequestBody execute(BufferedReader in, String contentLengthValue) throws IOException {
        if (in == null || contentLengthValue == null) {
            logger.warn("BufferedReader or Content-Length value is null");
            return null;
        }
        
        final int contentLength = Integer.parseInt(contentLengthValue);
        final char[] body = new char[contentLength];
        final int read = in.read(body, 0, contentLength);
        final String requestBody = new String(body, 0, read);
        logger.info("Request Body: {}", requestBody);
        return new HttpRequestBody(requestBody, contentLength);
    }
}
