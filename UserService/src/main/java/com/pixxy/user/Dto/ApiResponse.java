package com.pixxy.user.Dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ApiResponse<T> {

	private boolean success;

	private String message;

	private T data;

	public static <T> ApiResponse<T> success(String message, T data) {
		return new ApiResponse<T>(true, message, data);
	}
	
	public static <T> ApiResponse<T> failure(String message, T data) {
		return new ApiResponse<T>(false, message, data);
	}
}
