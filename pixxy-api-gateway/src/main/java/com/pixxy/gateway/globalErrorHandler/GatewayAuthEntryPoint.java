package com.pixxy.gateway.globalErrorHandler;

import java.time.Instant;
import java.util.UUID;

import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.server.ServerAuthenticationEntryPoint;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.pixxy.gateway.DTO.GlobalErrorResponse;

import io.micrometer.tracing.Span;
import io.micrometer.tracing.Tracer;
import reactor.core.publisher.Mono;

// Handles 401 Error Response
@Component
public class GatewayAuthEntryPoint implements ServerAuthenticationEntryPoint {
	private final ObjectMapper mapper;
	private final Tracer tracer;

	public GatewayAuthEntryPoint(ObjectMapper mapper, Tracer tracer) {
		this.mapper = mapper;
		this.tracer = tracer;
	}

	@Override
	public Mono<Void> commence(ServerWebExchange exchange, AuthenticationException ex) {

		ServerHttpResponse response = exchange.getResponse();
		response.setStatusCode(HttpStatus.UNAUTHORIZED);
		response.getHeaders().setContentType(MediaType.APPLICATION_JSON);
		String traceId = getTraceId();

		GlobalErrorResponse body = new GlobalErrorResponse(Instant.now(), HttpStatus.UNAUTHORIZED.value(),
				"Invalid or missing JWT", ex.getMessage(), exchange.getRequest().getPath().value(), traceId);

		byte[] bytes;
		try {
			bytes = mapper.writeValueAsBytes(body);
		} catch (Exception e) {
			bytes = new byte[0];
		}

		DataBuffer buffer = response.bufferFactory().wrap(bytes);
		return response.writeWith(Mono.just(buffer));
	}

	private String getTraceId() {

		Span span = tracer.currentSpan();
		return span != null ? span.context().traceId() : UUID.randomUUID().toString();
	}

}
