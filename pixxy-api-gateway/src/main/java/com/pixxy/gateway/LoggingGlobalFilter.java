package com.pixxy.gateway;

import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.annotation.Order;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;

import reactor.core.publisher.Mono;

@Component
//@Order(-100)
public class LoggingGlobalFilter implements GlobalFilter {

	private Logger log = LoggerFactory.getLogger(LoggingGlobalFilter.class);

	@Override
	public Mono<Void> filter(ServerWebExchange exchange,
			org.springframework.cloud.gateway.filter.GatewayFilterChain chain) {
		ServerHttpRequest request = exchange.getRequest();

		request.getHeaders().forEach((key, values) -> {
			values.forEach(value -> log.info(request.getPath() + "Incoming Header -> {} : {}", key, value));
		});

		return chain.filter(exchange);
	}

}