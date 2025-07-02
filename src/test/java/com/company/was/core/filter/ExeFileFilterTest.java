package com.company.was.core.filter;

import com.company.was.core.response.HttpResponse;
import junit.framework.TestCase;

public class ExeFileFilterTest extends TestCase {

    private final ExeFileFilter filter = new ExeFileFilter();

    public void testValidPath() {
        MockHttpRequest request = new MockHttpRequest("/index.html");
        HttpResponse response = HttpResponse.getNotFoundResponse();
        boolean result = filter.doFilter(request, response);
        assertTrue(result);
    }

    public void testInvalidPath() {
        MockHttpRequest request = new MockHttpRequest("/index.exe");
        HttpResponse response = HttpResponse.getNotFoundResponse();
        boolean result = filter.doFilter(request, response);
        assertFalse(result);
    }

}