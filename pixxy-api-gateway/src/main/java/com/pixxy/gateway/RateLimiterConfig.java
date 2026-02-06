package com.pixxy.gateway;

import org.springframework.cloud.gateway.filter.ratelimit.KeyResolver;
import org.springframework.cloud.gateway.filter.ratelimit.RedisRateLimiter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;

@Configuration
public class RateLimiterConfig {
	@Bean
	KeyResolver userKeyResolver() {

		return exchange -> {

			System.out.println("Inside keyResolver"+exchange.getPrincipal());
			return exchange.getPrincipal().cast(JwtAuthenticationToken.class)
					.map(jwt -> jwt.getToken().getClaimAsString("userId")).defaultIfEmpty("anonymous");
		};
	}

	@Bean
	RedisRateLimiter redisRateLimiter() {
//		return new RedisRateLimiter(1, 2, 1);
		return new RedisRateLimiter(100, 200, 1);
	}
}
