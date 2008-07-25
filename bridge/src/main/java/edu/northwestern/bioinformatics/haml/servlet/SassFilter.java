package edu.northwestern.bioinformatics.haml.servlet;

import javax.servlet.Filter;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.FilterChain;
import java.io.IOException;

/**
 * @author Rhett Sutphin
 */
public class SassFilter implements Filter {
    public void init(FilterConfig filterConfig) throws ServletException {
        throw new UnsupportedOperationException("init not implemented");

    }

    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        throw new UnsupportedOperationException("doFilter not implemented");

    }

    public void destroy() {
        throw new UnsupportedOperationException("destroy not implemented");

    }
}
