package com.epam.jwd.fitness_center.controller.filter;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import java.io.IOException;

@WebFilter(urlPatterns = "/*")
public class EncodingFilter implements Filter {
    private static final String DEFAULT_ENCODING = "UTF-8";
    private String encoding;

    @Override
    public void init(FilterConfig filterConfig) {
        encoding = filterConfig.getInitParameter("encoding");
        if (encoding == null) {
            encoding = DEFAULT_ENCODING;
        }
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain)
            throws IOException, ServletException {
        String requestEncoding = servletRequest.getCharacterEncoding();
        if (!encoding.equalsIgnoreCase(requestEncoding)) {
            servletRequest.setCharacterEncoding(encoding);
            servletResponse.setCharacterEncoding(encoding);
        }
        filterChain.doFilter(servletRequest, servletResponse);
    }
}
