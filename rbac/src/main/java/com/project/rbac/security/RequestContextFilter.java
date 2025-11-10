package com.project.rbac.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.project.rbac.common.ApiException;
import com.project.rbac.domain.*;

import java.io.IOException;


@Component("customRequestContextFilter")
public class RequestContextFilter extends OncePerRequestFilter {
    @Override
    protected void doFilterInternal(HttpServletRequest req, HttpServletResponse res, FilterChain chain)
    throws ServletException, IOException {
        try {
            String userId = req.getHeader("X-User-Id");
            String roleStr = req.getHeader("X-Role");
            String planStr = req.getHeader("X-Plan");

            if (userId == null || roleStr == null || planStr == null) {
                throw new ApiException(HttpStatus.UNAUTHORIZED, "Missing auth headers: X-User-Id, X-Role, X-Plan");
            }

            Role role; Plan plan;
            try { role = Role.valueOf(roleStr); } catch (Exception e) { throw new ApiException(HttpStatus.UNAUTHORIZED, "Invalid role"); }
            try { plan = Plan.valueOf(planStr); } catch (Exception e) { throw new ApiException(HttpStatus.UNAUTHORIZED, "Invalid plan"); }

            RequestContext.set(new RequestContext(userId, role, plan));
            chain.doFilter(req, res);
        } finally {
            RequestContext.clear();
        }
    }
}
