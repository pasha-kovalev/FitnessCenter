package com.epam.jwd.fitness_center.controller.filter;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import java.io.IOException;

@WebFilter(urlPatterns = "/*")
public class EncodingFilter implements Filter {
    private static final String ENCODING = "UTF-8";

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain)
            throws IOException, ServletException {
        String responseEncoding = servletRequest.getCharacterEncoding();

        if(!ENCODING.equalsIgnoreCase(responseEncoding)) {
                servletResponse.setCharacterEncoding(ENCODING);
        }
        filterChain.doFilter(servletRequest, servletResponse);
    }
}
