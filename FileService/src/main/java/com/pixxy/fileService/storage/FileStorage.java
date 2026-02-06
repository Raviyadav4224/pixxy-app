package com.pixxy.fileService.storage;

import java.io.IOException;
import java.io.InputStream;

public interface FileStorage {

	String store(InputStream inpStr, String contentType) throws IOException;
	
	InputStream load(String storageKey);
	
	void delete(String storageKey);
}
