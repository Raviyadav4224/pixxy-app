package com.pixxy.fileService.utils;

import java.io.InputStream;

import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

import com.pixxy.fileService.Dto.ApiResponse;

public class ApiResponseUtil {

//		ok, craeted, error

	public static <T> ResponseEntity<ApiResponse<T>> ok(String message, T data, HttpStatus status) {

		return ResponseEntity.status(status).body(ApiResponse.success(message, data));
	}

	public static <T> ResponseEntity<ApiResponse<T>> created(String message, T data) {

		return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse.success(message, data));
	}

	public static <T> ResponseEntity<ApiResponse<T>> error(String message, HttpStatus status, T data) {

		return ResponseEntity.status(status).body(ApiResponse.failure(message, data));
	}

	public static ResponseEntity<InputStreamResource> fileOutput(String message, InputStream inputStream,
			String mediaType, String fileName) {

		return ResponseEntity.status(HttpStatus.OK)
				.header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + fileName + "\"")
				.contentType(MediaType.parseMediaType(mediaType)).body(new InputStreamResource(inputStream));
	}
}
