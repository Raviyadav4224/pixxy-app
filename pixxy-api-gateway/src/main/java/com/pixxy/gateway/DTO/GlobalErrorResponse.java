package com.pixxy.gateway.DTO;

import java.time.Instant;

public record GlobalErrorResponse(Instant timestamp, int status, String error, String message, String path,
		String traceId) {

}
