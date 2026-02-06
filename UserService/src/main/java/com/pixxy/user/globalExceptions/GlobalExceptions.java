package com.pixxy.user.globalExceptions;

import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.pixxy.user.Dto.ApiResponse;
import com.pixxy.user.util.ApiResponseUtil;

@RestControllerAdvice
public class GlobalExceptions {

	@ExceptionHandler(exception = MethodArgumentNotValidException.class)
	public ResponseEntity<ApiResponse<Map<String, String>>> invalidArgumentsException(
			MethodArgumentNotValidException ex) {

		HashMap<String, String> response = new HashMap<String, String>();

		ex.getFieldErrors().forEach(err -> {
			System.out.println(err.getField() + " MSG " + err.getDefaultMessage());

			response.put(err.getField(), err.getDefaultMessage());
		});
		return ApiResponseUtil.error("Invalid input", HttpStatus.BAD_REQUEST, response);
	}

//	Network, Timeout, Connection Refused
	@ExceptionHandler(exception = feign.RetryableException.class)
	public ResponseEntity<ApiResponse<Map<String, String>>> feignException(feign.RetryableException ex) {

		HashMap<String, String> response = new HashMap<String, String>();

		return ApiResponseUtil.error("Downstream service error", HttpStatus.SERVICE_UNAVAILABLE, response);
	}

	@ExceptionHandler(feign.FeignException.class)
	public ResponseEntity<ApiResponse<Map<String, String>>> handleFeignException(feign.FeignException ex) {

		HttpStatus status;

		if (ex.status() == 404) {
			status = HttpStatus.NOT_FOUND;
		} else if (ex.status() == 400) {
			status = HttpStatus.BAD_REQUEST;
		} else if (ex.status() == 403) {
			status = HttpStatus.FORBIDDEN;
		} else if (ex.status() == 503) {
			status = HttpStatus.SERVICE_UNAVAILABLE;
		} else {
			status = HttpStatus.BAD_GATEWAY; // safest default
		}

		return ApiResponseUtil.error("Downstream service error", status, Map.of());
	}

}
