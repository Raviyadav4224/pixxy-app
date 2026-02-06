package com.pixxy.gateway;

import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.annotation.Order;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;

import reactor.core.publisher.Mono;

@Component
//@Order(-1)
public class JwtUserContextFilter implements GlobalFilter {

	@Override
	public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
		System.out.println("Inside JWTGlobalFilter");
		System.out.println(exchange.getRequest().getHeaders().toSingleValueMap());
		return exchange.getPrincipal().ofType(JwtAuthenticationToken.class).flatMap(auth -> {

			String userId = auth.getToken().getClaimAsString("userId");
			ServerHttpRequest mutatedRequest = exchange.getRequest().mutate().header("X-User-Id", userId)
					.header("X-Gateway-Auth", "pixxy-gateway").headers(h -> h.remove("Authorization")).build();

			return chain.filter(exchange.mutate().request(mutatedRequest).build());
		})
				// 🔥 THIS IS THE KEY LINE
				.switchIfEmpty(chain.filter(exchange));
	}
}
