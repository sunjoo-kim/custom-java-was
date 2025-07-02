package com.company.was.core.filter;

import com.company.was.core.request.DefaultHttpRequest;
import com.company.was.core.response.HttpResponse;

import java.util.ArrayList;
import java.util.List;

public class FilterChain {
    private final List<RequestFilter> filters = new ArrayList<>();

    public void addFilter(RequestFilter filter) {
        filters.add(filter);
    }

    public boolean doFilter(DefaultHttpRequest request, HttpResponse response) {
        for (RequestFilter filter : filters) {
            if (!filter.doFilter(request, response)) {
                return false;
            }
        }
        return true;
    }

    public List<RequestFilter> getFilters() {
        return filters;
    }
}
