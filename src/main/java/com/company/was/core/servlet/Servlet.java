package com.company.was.core.servlet;

import com.company.was.core.request.DefaultHttpRequest;
import com.company.was.core.response.HttpResponse;

public interface Servlet {
    HttpResponse service(DefaultHttpRequest request, HttpResponse response);
}
