package com.company.was.core;

import com.company.was.config.Config;
import com.company.was.config.ConfigLoader;
import com.company.was.core.request.*;
import com.company.was.core.response.HttpResponse;
import com.company.was.core.response.HttpResponseBody;
import com.company.was.core.response.HttpResponseHeaders;
import com.company.was.core.response.HttpResponseStatusLine;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.file.Files;
import java.util.*;

public class Server {

    private static final Logger logger = LoggerFactory.getLogger(Server.class);
    private static final HttpRequestLineParser requestLineBuilder = new HttpRequestLineParser();
    private static final HttpRequestHeaderParser headerParser = new HttpRequestHeaderParser();
    private static final HttpRequestBodyParser bodyParser = new HttpRequestBodyParser();
    private static final Dispatcher DISPATCHER = new Dispatcher();
    private static final Config CONFIG = ConfigLoader.load("config/config.json");

    public static void main(String[] args) throws Exception {
        ServerSocket serverSocket = new ServerSocket(CONFIG.port());
        logger.info("Server started on port {}", CONFIG.port());
        logger.info("Configuration: {}", CONFIG);

        while (true) {
            Socket socket = serverSocket.accept();
            new Thread(() -> handleClient(socket, CONFIG)).start();
        }
    }

    private static void handleClient(Socket socket, Config config) {
        try (
                BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                BufferedWriter out = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()))
        ) {

            // 요청 라인 읽기
            final String requestLine = in.readLine();
            logger.info("Received request line: {}", requestLine);
            HttpRequestLine requestLineInstance = requestLineBuilder.execute(requestLine);
            if (!requestLineInstance.version().startsWith("HTTP/1.1")) {
                respondError(out, 400, config);
                return;
            }

            // 헤더 섹션 읽기
            final Map<String, String> headers = headerParser.execute(in);

            final String hostHeader = headers.getOrDefault("Host", null);
            if (hostHeader != null && requestLineInstance.path().contains(".")) {
                Config.VirtualHost virtualHost = config.getVirtualHost(hostHeader);
                logger.info("path: {}, httpRoot: {}", requestLineInstance.path(), virtualHost.httpRoot());
                requestLineInstance = requestLineInstance.updatePath(virtualHost.httpRoot() + requestLineInstance.path());
                logger.info("Updated path: {}", requestLineInstance.path());
            }
            logger.info("Request Line: {}", requestLineInstance);
            // Content-Length 기반 본문 읽기 (있을 경우)
            final String contentLengthValue = headers.get("Content-Length");
            final HttpRequestBody httpRequestBody = bodyParser.execute(in, contentLengthValue);

            final DefaultHttpRequest request = new DefaultHttpRequest(requestLineInstance, headers, httpRequestBody);

            final String host = headers.getOrDefault("Host", null);
            final Config.VirtualHost vhost = config.getVirtualHost(host);
            if (vhost == null) {
                respondError(out, 403, config, host);
                return;
            }

            HttpResponse httpResponse = new HttpResponse(null, null, null);

            HttpResponse response = DISPATCHER.dispatchRequest(request, httpResponse);
            writeToBuffer(response, out);
            out.flush();
        } catch (IOException e) {
            logger.error("I/O error", e);
        }
    }

    private static void writeToBuffer(final HttpResponse response, final BufferedWriter out) throws IOException {
        StringWriter buffer = new StringWriter(); // 전체 응답 로그용 버퍼
        BufferedWriter responseWriter = new BufferedWriter(buffer);

        // 상태 라인 작성
        HttpResponseStatusLine statusLine = response.statusLine();
        statusLine.writeTo(responseWriter);

        // 헤더 작성
        HttpResponseHeaders responseHeaders = response.headers();
        responseHeaders.writeTo(responseWriter);

        // 빈 줄 (헤더 끝)
        responseWriter.write("\r\n");

        // 본문 작성
        HttpResponseBody responseBody = response.body();
        responseBody.writeTo(responseWriter);

        // flush 내부 버퍼 → StringWriter
        responseWriter.flush();

        // 로그 출력
        String fullResponse = buffer.toString();
        logger.info("HTTP Response:\n{}", fullResponse);

        // 최종 응답을 실제 클라이언트용 BufferedWriter에 전달
        out.write(fullResponse);
    }


    private static void respondError(BufferedWriter out, int code, Config config) throws IOException {
        respondError(out, code, config, "localhost");
    }

    private static void respondError(BufferedWriter out, int code, Config config, String host) throws IOException {
        Config.VirtualHost vhost = config.hosts().get(host);
        String filename = vhost.errorPages().get(String.valueOf(code));
        String body = Files.readString(new File("errors/" + filename).toPath());
        out.write("HTTP/1.1 " + code + " ERROR\r\nContent-Type: text/html\r\n\r\n" + body);
        out.flush();
    }
}
