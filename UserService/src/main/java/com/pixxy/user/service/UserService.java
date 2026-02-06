package com.pixxy.user.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.ravi.jwt.JwtClaims;
import com.ravi.jwt.JwtUtils;

import io.github.resilience4j.bulkhead.annotation.Bulkhead;
import io.github.resilience4j.bulkhead.annotation.Bulkhead.Type;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;
import io.github.resilience4j.timelimiter.annotation.TimeLimiter;

import com.pixxy.user.Dto.ApiResponse;
import com.pixxy.user.Dto.FileResponse;
import com.pixxy.user.Dto.LoginUserDto;
import com.pixxy.user.Dto.RegisterUserDto;
import com.pixxy.user.Dto.UpdateUserDto;
import com.pixxy.user.Dto.UserProfile;
import com.pixxy.user.client.FileServiceClient;
import com.pixxy.user.model.UserModel;
import com.pixxy.user.repo.UserRepo;
import com.pixxy.user.util.ApiResponseUtil;

import jakarta.validation.Valid;

@Service
public class UserService {

	UserRepo userRepo;
	JwtUtils jwtUtils;
	FileServiceClient fileServiceClient;

	public UserService(UserRepo userRepo, JwtUtils jwtUtils, FileServiceClient fileServiceClient) {
		this.userRepo = userRepo;
		this.jwtUtils = jwtUtils;
		this.fileServiceClient = fileServiceClient;
	}

	public ResponseEntity<ApiResponse<Map<String, String>>> registerUser(RegisterUserDto userDto) {

		Map<String, String> response = new HashMap<String, String>();

//		User Already Exists

		Optional<UserModel> existingUser = userRepo.findByEmail(userDto.getEmail());
		if (existingUser.isPresent()) {

			UserModel user = existingUser.get();
			if (!userDto.getPassword().equals(user.getPassword())) {
				return ApiResponseUtil.error("Invalid Credentials", HttpStatus.BAD_REQUEST, null);
			}

			return ApiResponseUtil.error("User Already Exists, Please Log in to continue", HttpStatus.BAD_REQUEST,
					null);
		}

		UserModel newUser = new UserModel();

		newUser.setEmail(userDto.getEmail());
		newUser.setUserName(userDto.getUserName());
		newUser.setFirstName(userDto.getFirstName());
		newUser.setLastName(userDto.getLastName());
		newUser.setPassword(userDto.getPassword());
		newUser.setProfilePictureUrl(userDto.getProfilePictureUrl());

		UserModel savedUser = userRepo.save(newUser);

//		generate jwt , accessToken
		String token = jwtUtils.generateToken(savedUser.getId().toString(), Map.of(JwtClaims.USER_ID, savedUser.getId(),
				JwtClaims.ROLE, savedUser.getRole(), JwtClaims.ISSUER, "user-service"));
		response.put("token", token);

		return ApiResponseUtil.created("User Created Successfully", response);
	}

	public ResponseEntity<ApiResponse<Map<String, String>>> loginUser(@Valid LoginUserDto userDto) {
		// TODO Auto-generated method stub

		Optional<UserModel> existingUser = userRepo.findByEmail(userDto.getEmail());

		if (!existingUser.isPresent()) {
			return ApiResponseUtil.error("Invalid Credentials", HttpStatus.BAD_REQUEST, null);
		}

		UserModel user = existingUser.get();
		if (!userDto.getPassword().equals(user.getPassword())) {
			return ApiResponseUtil.error("Invalid Credentials", HttpStatus.BAD_REQUEST, null);
		}

//		generate jwt 
		HashMap<String, String> response = new HashMap<String, String>();

//		generate jwt , accessToken
		String token = jwtUtils.generateToken(user.getId().toString(), Map.of(JwtClaims.USER_ID, user.getId(),
				JwtClaims.ROLE, user.getRole(), JwtClaims.ISSUER, "user-service"));
		response.put("token", token);
		return ApiResponseUtil.ok("User logged in succesfully", response, HttpStatus.OK);

	}

	public ResponseEntity<ApiResponse<Map<String, String>>> logoutUser(String authHeader) {

//		invalidate the token using Redis
		System.out.println(authHeader);

		return ApiResponseUtil.ok("User logged out successfully", null, HttpStatus.OK);
	}

	public ResponseEntity<ApiResponse<Map<String, String>>> updateUser(@Valid UpdateUserDto userDto) {
		// TODO Auto-generated method stub
		return null;
	}

	public ResponseEntity<ApiResponse<Map<String, Object>>> getUserProfileDetails(Long userId) {
		HashMap<String, Object> response = new HashMap<String, Object>();
		Optional<UserModel> existingUser = userRepo.findById(userId);

		if (!existingUser.isPresent()) {
			ApiResponseUtil.error("User doesn't exists", HttpStatus.BAD_REQUEST, null);
		}

		UserModel user = existingUser.get();

		System.out.println("Calling FileService from User Service");
		List<FileResponse> allFiles = fileServiceClient.files(userId).getBody().getData();

		UserProfile userProfile = new UserProfile(user.getId(), user.getEmail(), user.getUserName(),
				user.getFirstName(), user.getLastName(), user.getProfilePictureUrl(), user.getRole(),
				user.getIsActive(), user.getCreatedAt(), allFiles);
		response.put("user", userProfile);
		return ApiResponseUtil.ok("User details fetched successfully", response, HttpStatus.OK);
	}

	@Retry(name = "fileService", fallbackMethod = "fallbackRetry")
	@CircuitBreaker(name = "fileService")
	@TimeLimiter(name = "fileService")
	@Bulkhead(type = Type.SEMAPHORE, name = "fileService")
	public CompletableFuture<String> getStatusAsync(long ms) {
		ExecutorService executor = Executors.newFixedThreadPool(20);
		return CompletableFuture.supplyAsync(() -> fileServiceClient.status(ms), executor);
	}

	public CompletableFuture<String> fallbackTimeLimiter(Throwable ex) {
		System.out.println(ex.getMessage() + ex.getLocalizedMessage());
		return CompletableFuture.completedFuture("File Service is Down fallbackTimeLimiter");
	}

	public CompletableFuture<String> fallbackCB(Throwable ex) {
		System.out.println(ex.getMessage());
		return CompletableFuture.completedFuture("File Service is Down fallbackCB");
	}

	public CompletableFuture<String> fallbackRetry(Throwable ex) {
		System.out.println(ex.getMessage() + ex.getClass().getName());
		return CompletableFuture.completedFuture("File Service is Down fallbackRetry");
	}

	public void method() throws InterruptedException, ExecutionException {

		ExecutorService executors = Executors.newFixedThreadPool(3);

//		Future<Integer> futureResult = executors.submit(() -> {
//			System.out.println("Inside Thread");
//			Thread.sleep(3000);
//			return 99;
//		});
//		System.out.println("Thinking........");
//
//		Integer result = futureResult.get(); // BLOCKS
//		System.out.println(result);
		
		

		executors.shutdown();
	}
}
