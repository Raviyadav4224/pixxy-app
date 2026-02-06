package com.pixxy.fileService.controller;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.pixxy.fileService.Dto.ApiResponse;
import com.pixxy.fileService.Dto.FileResponse;
import com.pixxy.fileService.service.FileService;
import com.ravi.jwt.JwtUtils;

@RestController
@RequestMapping(path = "/v1/files/")
public class FileController {

	private static final Logger log = LoggerFactory.getLogger(FileController.class);

	FileService fileService;
	JwtUtils jwtUtils;

	public FileController(FileService fileService, JwtUtils jwtUtils) {
		this.fileService = fileService;
		this.jwtUtils = jwtUtils;
	}

	@GetMapping("allFiles")
	public ResponseEntity<ApiResponse<List<FileResponse>>> getAllFiles(@RequestHeader("X-User-Id") Long userId) {
		log.info("inside allFiles controller");
		return fileService.getAllFiles(userId);
	}

	@PostMapping("upload")
	public ResponseEntity<ApiResponse<String>> uploadFile(@RequestParam MultipartFile file,
			@RequestHeader("X-User-Id") Long userId) {
		return fileService.upload(file, userId);
	}

	@DeleteMapping("remove/{fileId}")
	public ResponseEntity<ApiResponse<String>> removeFile(@PathVariable Long fileId,
			@RequestHeader("X-User-Id") Long userId) {
		return fileService.removeFile(fileId, userId);
	}

	@GetMapping("file/{fileId}")
	public ResponseEntity<?> getFile(@PathVariable Long fileId, @RequestHeader("X-User-Id") Long userId) {
		return fileService.getFile(fileId, userId);
	}

	@GetMapping("status/{ms}")
	public String getFile(@PathVariable long ms) throws InterruptedException {
		System.out.println("@PathVariable long ms"+ms);
		Thread.sleep(ms);
		return "File Service is Running";
	}
}
