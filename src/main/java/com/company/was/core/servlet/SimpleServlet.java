package com.company.was.core.servlet;

import com.company.was.core.request.DefaultHttpRequest;
import com.company.was.core.response.HttpResponse;
import com.company.was.core.response.HttpResponseBody;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

public class SimpleServlet implements Servlet {
    @Override
    public HttpResponse service(DefaultHttpRequest request, HttpResponse response) {
        // 한국 시간 (Asia/Seoul) 기준 현재 시각
        ZonedDateTime kstTime = ZonedDateTime.now(ZoneId.of("Asia/Seoul"));

        // 원하는 포맷
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

        // 포맷 적용
        String formatted = kstTime.format(formatter);
        HttpResponseBody responseBody = new HttpResponseBody("<html><body><h1>"+formatted+"</h1></body></html>");
        return HttpResponse.getOkResponse(responseBody);
    }
}
