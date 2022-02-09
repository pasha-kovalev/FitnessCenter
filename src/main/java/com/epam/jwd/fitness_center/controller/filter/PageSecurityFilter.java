package com.epam.jwd.fitness_center.controller.filter;

import com.epam.jwd.fitness_center.controller.PagePath;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebFilter(urlPatterns = {"/jsp/*"})
public class PageSecurityFilter implements Filter {
    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain)
            throws IOException {
        HttpServletResponse response = (HttpServletResponse) servletResponse;
        response.sendRedirect(PagePath.INDEX.getPath());
    }
}
