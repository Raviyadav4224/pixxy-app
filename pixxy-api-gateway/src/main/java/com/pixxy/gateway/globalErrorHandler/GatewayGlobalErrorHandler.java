package com.pixxy.gateway.globalErrorHandler;

import java.net.ConnectException;
import java.net.UnknownHostException;
import java.time.Instant;
import java.util.UUID;
import java.util.concurrent.TimeoutException;

import org.springframework.boot.web.reactive.error.ErrorWebExceptionHandler;
import org.springframework.core.annotation.Order;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.server.ServerWebExchange;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.pixxy.gateway.DTO.GlobalErrorResponse;

import io.micrometer.tracing.Span;
import io.micrometer.tracing.Tracer;
import reactor.core.publisher.Mono;

// Handles Server Error 500, Service Unavailable errors 503

@Component
@Order(-2)
public class GatewayGlobalErrorHandler implements ErrorWebExceptionHandler {
	private final ObjectMapper mapper;
	private final Tracer tracer;

	public GatewayGlobalErrorHandler(Tracer tracer, ObjectMapper mapper) {
		this.mapper = mapper;
		this.tracer = tracer;
	}

	@Override
	public Mono<Void> handle(ServerWebExchange exchange, Throwable ex) {

		ServerHttpResponse response = exchange.getResponse();
		HttpStatus status = getResponseStatus(ex);

		response.setStatusCode(status);

		response.getHeaders().setContentType(MediaType.APPLICATION_JSON);

		GlobalErrorResponse errorResponse = new GlobalErrorResponse(Instant.now(), status.value(),
				status.getReasonPhrase(), resolveMessage(ex), exchange.getRequest().getPath().value(), getTraceId());

		byte[] body;
		try {
			body = mapper.writeValueAsBytes(errorResponse);
		} catch (Exception e) {
			body = new byte[0];
		}

		DataBuffer buffer = response.bufferFactory().wrap(body);
		return response.writeWith(Mono.just(buffer));
	}

	public HttpStatus getResponseStatus(Throwable ex) {
		if (ex instanceof ResponseStatusException rse) {
			return HttpStatus.resolve(rse.getStatusCode().value());
		}

		if (ex instanceof TimeoutException) {
			return HttpStatus.GATEWAY_TIMEOUT;
		}

		if (ex instanceof ConnectException || ex instanceof UnknownHostException) {
			return HttpStatus.SERVICE_UNAVAILABLE;
		}

		return HttpStatus.INTERNAL_SERVER_ERROR;
	}

	private String resolveMessage(Throwable ex) {

		if (ex instanceof ResponseStatusException rse) {
			return rse.getReason();
		}

		if (ex instanceof TimeoutException) {
			return "Downstream service timed out";
		}

		if (ex instanceof ConnectException) {
			return "Downstream service unavailable";
		}

		return "Unexpected gateway error";
	}

	private String getTraceId() {

		Span span = tracer.currentSpan();
		return span != null ? span.context().traceId() : UUID.randomUUID().toString();
	}
}
