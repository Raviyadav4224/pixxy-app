package com.pixxy.user.client;

import java.util.List;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;

import com.pixxy.user.Dto.ApiResponse;
import com.pixxy.user.Dto.FileResponse;

@FeignClient(name = "FILESERVICE" ,url = "${FILESERVICE.URL}")

public interface FileServiceClient {

	@GetMapping("/v1/files/allFiles")
	public ResponseEntity<ApiResponse<List<FileResponse>>> files(@RequestHeader("X-User-Id") Long userId);

	@GetMapping("/v1/files/status/{ms}")
	public String status(@PathVariable long ms);

}
