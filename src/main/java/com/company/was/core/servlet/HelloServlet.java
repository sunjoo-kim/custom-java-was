package com.company.was.core.servlet;

import com.company.was.core.request.DefaultHttpRequest;
import com.company.was.core.response.HttpResponse;
import com.company.was.core.response.HttpResponseBody;

public class HelloServlet implements Servlet {
    @Override
    public HttpResponse service(DefaultHttpRequest request, HttpResponse response) {
        HttpResponseBody responseBody = new HttpResponseBody("<html><body><h1>Hello, World!</h1></body></html>");
        return HttpResponse.getOkResponse(responseBody);
    }
}
