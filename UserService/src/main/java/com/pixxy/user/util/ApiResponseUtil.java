package com.pixxy.user.util;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.pixxy.user.Dto.ApiResponse;

public class ApiResponseUtil {

//	ok, craeted, error

	public static <T> ResponseEntity<ApiResponse<T>> ok(String message, T data, HttpStatus status) {

		return ResponseEntity.status(status).body(ApiResponse.success(message, data));
	}

	public static <T> ResponseEntity<ApiResponse<T>> created(String message, T data) {

		return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse.success(message, data));
	}

	public static <T> ResponseEntity<ApiResponse<T>> error(String message, HttpStatus status, T data) {

		return ResponseEntity.status(status).body(ApiResponse.failure(message,data));
	}
}
