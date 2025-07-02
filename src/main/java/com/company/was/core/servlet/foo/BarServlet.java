package com.company.was.core.servlet.foo;

import com.company.was.core.request.DefaultHttpRequest;
import com.company.was.core.response.HttpResponse;
import com.company.was.core.response.HttpResponseBody;
import com.company.was.core.servlet.Servlet;

public class BarServlet implements Servlet {
    @Override
    public HttpResponse service(DefaultHttpRequest request, HttpResponse response) {
        HttpResponseBody responseBody = new HttpResponseBody("<html><body><h1>foo bar</h1></body></html>");
        return HttpResponse.getOkResponse(responseBody);
    }
}
