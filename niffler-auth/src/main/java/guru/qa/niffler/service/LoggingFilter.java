package guru.qa.niffler.service;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Arrays;

@Component
public class LoggingFilter extends OncePerRequestFilter {

    private static final Logger log = LoggerFactory.getLogger(LoggingFilter.class);

    @Override
    protected void doFilterInternal(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, FilterChain filterChain) throws ServletException, IOException {
        if ("POST".equalsIgnoreCase(httpServletRequest.getMethod())) {
//            CachedBodyHttpServletRequest cachedBodyHttpServletRequest = new CachedBodyHttpServletRequest(httpServletRequest);
//            log.debug(cachedBodyHttpServletRequest.getReader().lines().collect(Collectors.joining(System.lineSeparator())));

            log.debug(httpServletRequest.getContentType());
            log.debug(httpServletRequest.getCharacterEncoding());
            log.debug(Arrays.stream(httpServletRequest.getCookies()).map(c -> c.getName() + "=" + c.getValue()).toList().toString());
            log.debug(httpServletRequest.getParameter("username"));
            log.debug(httpServletRequest.getParameter("password"));
            log.debug(httpServletRequest.getParameter("_csrf"));
        }
        filterChain.doFilter(httpServletRequest, httpServletResponse);
    }
}