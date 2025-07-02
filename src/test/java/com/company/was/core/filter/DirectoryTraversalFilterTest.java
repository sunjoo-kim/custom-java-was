package com.company.was.core.filter;

import com.company.was.core.request.HttpRequest;
import com.company.was.core.response.HttpResponse;
import org.junit.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class DirectoryTraversalFilterTest {
    private final DirectoryTraversalFilter filter = new DirectoryTraversalFilter();;

    @Test
    public void testValidPath() {
        HttpRequest request = new MockHttpRequest("/index.html");
        HttpResponse response = HttpResponse.getNotFoundResponse();
        boolean result = filter.doFilter(request, response);
        assertTrue(result);
    }

    @Test
    public void testInvalidPath() {
        HttpRequest request = new MockHttpRequest("../index.html");
        HttpResponse response = HttpResponse.getNotFoundResponse();
        boolean result = filter.doFilter(request, response);
        assertFalse(result);
    }

}