package com.pixxy.user.controller;

import java.util.Map;
import java.util.concurrent.CompletableFuture;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.pixxy.user.Dto.ApiResponse;
import com.pixxy.user.Dto.LoginUserDto;
import com.pixxy.user.Dto.RegisterUserDto;
import com.pixxy.user.Dto.UpdateUserDto;
import com.pixxy.user.service.UserService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/v1/users/")
public class UserController {

	private static final Logger log = LoggerFactory.getLogger(UserController.class);

	private UserService userService;

	public UserController(UserService userService) {
		this.userService = userService;
	}

	@GetMapping("status/{ms}")
	public CompletableFuture<String> checkStatus(@PathVariable long ms) {
		log.info("status being checked");
		return userService.getStatusAsync(ms).thenApply((res) -> res + " User Service is running");
	}

	@PostMapping("register")
	public ResponseEntity<ApiResponse<Map<String, String>>> registerUser(@RequestBody @Valid RegisterUserDto userDto) {
		log.info("inside register controller");
		return userService.registerUser(userDto);
	}

	@PostMapping("login")
	public ResponseEntity<ApiResponse<Map<String, String>>> loginUser(@RequestBody @Valid LoginUserDto userDto) {
		log.info("Inside Login controller");
		return userService.loginUser(userDto);
	}

	@PostMapping("logout")
	public ResponseEntity<ApiResponse<Map<String, String>>> logoutUser(
			@RequestHeader("Authorization") String authHeader) {
		return userService.logoutUser(authHeader);
	}

	@PatchMapping("update")
	public ResponseEntity<ApiResponse<Map<String, String>>> updateUser(@RequestBody @Valid UpdateUserDto userDto) {
		return userService.updateUser(userDto);
	}

	@PostMapping("profile")
	public ResponseEntity<ApiResponse<Map<String, Object>>> getUserProfile(@RequestHeader("X-User-Id") Long userId) {
		System.out.println("user id" + userId);
		return userService.getUserProfileDetails(userId);
	}
}
