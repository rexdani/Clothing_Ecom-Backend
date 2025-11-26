package com.Ecom.config;


import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class JwtAuthenticationEntryPoint implements AuthenticationEntryPoint {

    @Override
    public void commence(
            HttpServletRequest request,
            HttpServletResponse response,
            org.springframework.security.core.AuthenticationException authException
    ) throws IOException, ServletException {

        // This is triggered when the user tries to access a secured REST endpoint
        // without supplying any valid credentials (no token or invalid token)
        response.sendError(HttpServletResponse.SC_UNAUTHORIZED,
                "Unauthorized: Access is denied due to invalid or missing token");
    }
}

