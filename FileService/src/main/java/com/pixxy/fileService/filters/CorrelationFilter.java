package com.pixxy.fileService.filters;

import java.io.IOException;

import org.slf4j.MDC;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
//
//@Component
//public class CorrelationFilter extends OncePerRequestFilter {
//
//	@Override
//	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
//			throws ServletException, IOException {
//		
//		System.out.println(request);
//		String correlationId = request.getHeader("X-Correlation-Id");
//		String userId = request.getHeader("X-User-Id");
//
//		MDC.put("requestId", correlationId);
//		MDC.put("userId", userId);
//
//		try {
//			filterChain.doFilter(request, response);
//		} finally {
//			MDC.clear();
//		}
//	}
//
//}
