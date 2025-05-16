package com.etravel.userservice.config;

import jakarta.servlet.*;
import jakarta.servlet.http.*;
import org.springframework.core.Ordered;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import java.io.IOException;

@Component
public class AuthLoggingFilter implements Filter, Ordered {
    @Override
    public int getOrder() {
        // run before Spring Security’s filter chain
        return Ordered.HIGHEST_PRECEDENCE;
    }

    @Override
    public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain)
            throws IOException, ServletException {
        HttpServletRequest  r = (HttpServletRequest) req;
        // ← correct header name, and a space before the URI for readability
        System.out.println("🔑 User-Service sees Authorization: "
                + r.getHeader("Authorization")
                + "   [URI=" + r.getRequestURI() + "]\n"
                );
        chain.doFilter(req, res);
//        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
//        System.out.println("🛡️  After auth: " + auth);
//        System.out.println("   Authorities: " + auth.getAuthorities());
    }
}