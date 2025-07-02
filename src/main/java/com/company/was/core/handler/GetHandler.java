package com.company.was.core.handler;

import com.company.was.core.request.DefaultHttpRequest;
import com.company.was.core.response.HttpResponse;
import com.company.was.core.response.HttpResponseBody;
import com.company.was.core.servlet.Servlet;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class GetHandler {

    private static final Logger logger = LoggerFactory.getLogger(GetHandler.class);

    public HttpResponse doGet(DefaultHttpRequest request, HttpResponse response) {
        String resourcePath = request.getResourcePath();
        if (resourcePath.contains(".")) {
            return getResourceResponse(request, resourcePath);
        }

        String classPath = request.getClassPath();
        try {
            // 1. 클래스 로딩
            Class<?> aClass = Class.forName(classPath);

            // 2. 인스턴스 생성
            Object servletObj = aClass.getDeclaredConstructor().newInstance();

            // 3. SimpleServlet 타입인지 확인
            if (servletObj instanceof Servlet) {
                Servlet servlet = (Servlet) servletObj;

                // 4. 서비스 메서드 호출 (HttpRequest, HttpResponse는 가상의 타입이라고 가정)
                return servlet.service(request, response);
            } else {
                logger.warn("클래스가 SimpleServlet을 구현하지 않습니다: {}", classPath);
            }

        } catch (ClassNotFoundException e) {
            logger.error("클래스를 찾을 수 없습니다: {}", classPath, e);
        } catch (Exception e) {
            logger.error("클래스 인스턴스 생성 또는 서비스 호출 중 오류 발생: {}", classPath, e);
        }

        return HttpResponse.getNotFoundResponse();
    }

    private HttpResponse getResourceResponse(DefaultHttpRequest request, String resourcePath) {
        InputStream inputStream = HttpResponse.class.getClassLoader().getResourceAsStream(resourcePath);
        if (inputStream == null) {
            // 리소스가 없을 경우 404 Not Found 응답
            return HttpResponse.getNotFoundResponse(request.headers().getOrDefault("Host", "unknown"));
        }

        HttpResponseBody responseBody = null;
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream))) {
            String fullContent = reader.lines().reduce("", (acc, line) -> acc + line + "\n");
            if (fullContent.isEmpty()) {
                return HttpResponse.getNotFoundResponse();
            }
            responseBody = new HttpResponseBody(fullContent);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return HttpResponse.getOkResponse(responseBody);
    }

}
