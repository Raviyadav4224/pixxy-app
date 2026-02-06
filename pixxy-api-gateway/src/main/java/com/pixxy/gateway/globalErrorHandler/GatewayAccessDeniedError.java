package com.pixxy.gateway.globalErrorHandler;

import java.time.Instant;

import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.server.authorization.ServerAccessDeniedHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.pixxy.gateway.DTO.GlobalErrorResponse;

import reactor.core.publisher.Mono;

// Handles 403 Forbidden Errors
@Component
public class GatewayAccessDeniedError implements ServerAccessDeniedHandler {

	private final ObjectMapper mapper;

	public GatewayAccessDeniedError(ObjectMapper mapper) {
		this.mapper = mapper;
	}

	@Override
	public Mono<Void> handle(ServerWebExchange exchange, AccessDeniedException ex) {
		ServerHttpResponse response = exchange.getResponse();
		response.setStatusCode(HttpStatus.FORBIDDEN);
		response.getHeaders().setContentType(MediaType.APPLICATION_JSON);

		GlobalErrorResponse body = new GlobalErrorResponse(Instant.now(), HttpStatus.FORBIDDEN.value(), "Access denied",
				ex.getMessage(), exchange.getRequest().getPath().value(),
				exchange.getRequest().getHeaders().getFirst("traceparent"));

		byte[] bytes;
		try {
			bytes = mapper.writeValueAsBytes(body);
		} catch (Exception e) {
			bytes = new byte[0];
		}

		DataBuffer buffer = response.bufferFactory().wrap(bytes);
		return response.writeWith(Mono.just(buffer));
	}

}
