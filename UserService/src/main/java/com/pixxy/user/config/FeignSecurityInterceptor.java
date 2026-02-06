package com.pixxy.user.config;

import feign.RequestInterceptor;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

@Configuration
public class FeignSecurityInterceptor {

	@Bean
	RequestInterceptor requestInterceptor() {
		return requestTemplate -> {

			System.out.println("inside FeignSecurityInterceptor");
			ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder
					.getRequestAttributes();

			if (attributes == null) {
				return;
			}

			HttpServletRequest request = attributes.getRequest();

			// Forward Authorization header
			System.out.println("Headers" + request.getHeaderNames());
			System.out.println(request.getHeader("X-User-Id"));
			System.out.println("X-Gateway-Auth"+request.getHeader("X-Gateway-Auth"));
			String authorization = request.getHeader(HttpHeaders.AUTHORIZATION);
			if (authorization != null) {
				requestTemplate.header(HttpHeaders.AUTHORIZATION, authorization);
			}

			// Forward X-User-Id header
			String userId = request.getHeader("X-User-Id");
			if (userId != null) {
				requestTemplate.header("X-User-Id", userId);
			}
		};
	}
}
