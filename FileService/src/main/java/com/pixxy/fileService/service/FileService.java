package com.pixxy.fileService.service;

import java.io.IOException;
import java.io.InputStream;
import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.pixxy.fileService.Dto.ApiResponse;
import com.pixxy.fileService.Dto.FileResponse;
import com.pixxy.fileService.events.FileUploadedEvent;
import com.pixxy.fileService.model.FileMetaData;
import com.pixxy.fileService.repo.FileRepo;
import com.pixxy.fileService.storage.FileStorage;
import com.pixxy.fileService.utils.ApiResponseUtil;
import com.ravi.jwt.JwtUtils;

@Service
public class FileService {

	FileRepo fileRepo;
	JwtUtils jwtutils;
	FileStorage fileStorage;

	private KafkaTemplate<String, FileUploadedEvent> kafkaTemplate;

	public FileService(FileRepo fileRepo, JwtUtils jwtutils, FileStorage fileStorage,
			KafkaTemplate<String, FileUploadedEvent> kafkaTemplate) {
		this.fileRepo = fileRepo;
		this.jwtutils = jwtutils;
		this.fileStorage = fileStorage;
		this.kafkaTemplate = kafkaTemplate;
	}

	public ResponseEntity<ApiResponse<List<FileResponse>>> getAllFiles(Long userId) {

//		jwtutils.extractUserId(null)
		List<FileMetaData> files = fileRepo.findAllByOwnerId(userId);

		List<FileResponse> response = files.stream().map(file -> new FileResponse(file.getId(),
				file.getOriginalFileName(), file.getContentType(), file.getSize(), file.getCreatedAt())).toList();
		return ApiResponseUtil.ok("Files fetched", response, HttpStatus.OK);
	}

	public ResponseEntity<ApiResponse<String>> upload(MultipartFile file, Long userId) {
		// 1. Validate
		if (file.isEmpty()) {
			throw new IllegalArgumentException("Empty file");
		}

		if (file.getSize() > 20 * 1024 * 1024) {
			throw new IllegalArgumentException("Max size exceeded");
		}

		// 2. Store binary (THIS is where LocalFileStorage is used)
		String storageKey;
		try {
			storageKey = fileStorage.store(file.getInputStream(), file.getContentType());
		} catch (IOException e) {
			throw new RuntimeException(e);
		}

		// 3. Save metadata
		FileMetaData meta = new FileMetaData();
		meta.setOwnerId(userId);
		meta.setOriginalFileName(file.getOriginalFilename());
		meta.setStorageKey(storageKey);
		meta.setContentType(file.getContentType());
		meta.setSize(file.getSize());

		fileRepo.save(meta);

//		Producing a Kafka Event
		CompletableFuture<SendResult<String, FileUploadedEvent>> res = kafkaTemplate.send("file-uploaded",
				new FileUploadedEvent(meta.getId().toString(),storageKey,meta.getOriginalFileName(),Instant.now()));
		res.thenRun(() -> {

			System.out.println("Kafka Completed");
		});
		System.out.println("Kafka completed");
		return ApiResponseUtil.created("File saved successfully", null);
	}

	public ResponseEntity<ApiResponse<String>> removeFile(Long fileId, Long userId) {
		Optional<FileMetaData> existingFile = fileRepo.findById(fileId);

		if (existingFile.isEmpty()) {
			return ApiResponseUtil.error("File doesn't exists", HttpStatus.NOT_FOUND, null);
		}
		FileMetaData file = existingFile.get();

		if (!file.getOwnerId().equals(userId)) {
			return ApiResponseUtil.error("Invalid Request", HttpStatus.FORBIDDEN, null);
		}

		fileStorage.delete(file.getStorageKey());
		fileRepo.delete(file);

		return ApiResponseUtil.ok("File removed successfully", null, HttpStatus.OK);
	}

	public ResponseEntity<?> getFile(Long fileId, Long userId) {
		Optional<FileMetaData> existingFile = fileRepo.findById(fileId);

		if (existingFile.isEmpty()) {
			return ApiResponseUtil.error("File doesn't exists", HttpStatus.NOT_FOUND, null);
		}
		FileMetaData file = existingFile.get();

		if (!file.getOwnerId().equals(userId)) {
			return ApiResponseUtil.error("Invalid Request", HttpStatus.FORBIDDEN, null);
		}

		InputStream fileOutput = fileStorage.load(file.getStorageKey());
//		null chefck for fileoutput , if input stream is null
		return ApiResponseUtil.fileOutput("", fileOutput, file.getContentType(), file.getOriginalFileName());
	}

}
