package com.company.was.core;

import com.company.was.core.filter.DirectoryTraversalFilter;
import com.company.was.core.filter.ExeFileFilter;
import com.company.was.core.filter.FilterChain;
import com.company.was.core.request.DefaultHttpRequest;
import com.company.was.core.response.HttpResponse;
import com.company.was.core.handler.GetHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

public class Dispatcher {
    private static final Logger logger = LoggerFactory.getLogger(Dispatcher.class);
    private static final GetHandler getHandler = new GetHandler();
    private final FilterChain filterChain = new FilterChain();

    public Dispatcher() {
        filterChain.addFilter(new DirectoryTraversalFilter());
        filterChain.addFilter(new ExeFileFilter());
    }
    public HttpResponse dispatchRequest(DefaultHttpRequest request, HttpResponse response) throws IOException {
        logger.info("filterChain: {}", filterChain.getFilters());
        if (!filterChain.doFilter(request, response)) {
            return HttpResponse.getForbiddenResponse();
        }

        switch (request.requestLine().method()) {
            case "GET" -> response = getHandler.doGet(request, response);
            default -> response = HttpResponse.getNotFoundResponse();
        }
        return response;
    }
}
