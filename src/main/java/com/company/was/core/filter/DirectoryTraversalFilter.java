package com.company.was.core.filter;

import com.company.was.core.request.HttpRequest;
import com.company.was.core.response.HttpResponse;

public class DirectoryTraversalFilter implements RequestFilter {
    @Override
    public boolean doFilter(HttpRequest request, HttpResponse response) {
        return  !request.requestLine().path().contains("..");
    }
}