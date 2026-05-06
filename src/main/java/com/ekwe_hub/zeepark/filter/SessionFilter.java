package com.ekwe_hub.zeepark.filter;                     // <-- no "security"

import com.ekwe_hub.zeepark.exception.UnauthorizedException;
import com.ekwe_hub.zeepark.service.SessionService;
import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class SessionFilter implements Filter {

    private final SessionService sessionService;

    public SessionFilter(SessionService sessionService) {
        this.sessionService = sessionService;
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response,
                         FilterChain chain) throws IOException, ServletException {
        HttpServletRequest httpReq = (HttpServletRequest) request;
        HttpServletResponse httpRes = (HttpServletResponse) response;

        // Allow login/register endpoints without token
        String path = httpReq.getRequestURI();
        if (path.startsWith("/api/auth") || path.equals("/api/users/register")) {
            chain.doFilter(request, response);
            return;
        }

        // Check for Bearer token
        String authHeader = httpReq.getHeader("Authorization");
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            httpRes.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            httpRes.getWriter().write("{\"error\": \"Missing or invalid token\"}");
            return;
        }

        String token = authHeader.substring(7);
        try {
            sessionService.getAuthenticatedUser(token);   // throws if invalid
            chain.doFilter(request, response);            // proceed
        } catch (UnauthorizedException e) {
            httpRes.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            httpRes.getWriter().write("{\"error\": \"" + e.getMessage() + "\"}");
        }
    }
}