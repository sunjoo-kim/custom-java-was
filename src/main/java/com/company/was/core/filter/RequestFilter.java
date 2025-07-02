package com.company.was.core.filter;

import com.company.was.core.request.HttpRequest;
import com.company.was.core.response.HttpResponse;

public interface RequestFilter {
    boolean doFilter(HttpRequest request, HttpResponse response);
}