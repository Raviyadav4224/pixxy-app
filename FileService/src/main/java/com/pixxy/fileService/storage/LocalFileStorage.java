package com.pixxy.fileService.storage;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.UUID;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

@Component
@ConditionalOnProperty(name = "file.storage.type", havingValue = "LOCAL")
public class LocalFileStorage implements FileStorage {

	private final String rootPath = "/data/files/";
	private final Path rootDirectory = Paths.get(rootPath);

	@Override
	public String store(InputStream inpStr, String contentType) throws IOException {
		String key = UUID.randomUUID().toString();
		Files.createDirectories(rootDirectory);
		Files.copy(inpStr, rootDirectory.resolve(key));
		return key;
	}

	@Override
	public InputStream load(String storageKey) {

		InputStream in = null;
		try {
			Path filePath = rootDirectory.resolve(storageKey).normalize();

			if (!Files.exists(filePath)) {
				throw new FileNotFoundException("File doesn't exists");
			}
			in = Files.newInputStream(filePath, StandardOpenOption.READ);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return in;
	}

	@Override
	public void delete(String storageKey) {

		Path filePath = rootDirectory.resolve(storageKey).normalize();

		try {
			Files.delete(filePath);
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

}
