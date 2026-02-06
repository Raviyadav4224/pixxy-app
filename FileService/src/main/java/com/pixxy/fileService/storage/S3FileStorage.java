package com.pixxy.fileService.storage;

import java.io.IOException;
import java.io.InputStream;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

@Component
@ConditionalOnProperty(name = "file.storage.type", havingValue = "S3")
public class S3FileStorage implements FileStorage{

	@Override
	public String store(InputStream inpStr, String contentType) throws IOException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public InputStream load(String storageKey) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void delete(String storageKey) {
		// TODO Auto-generated method stub
		
	}

}
