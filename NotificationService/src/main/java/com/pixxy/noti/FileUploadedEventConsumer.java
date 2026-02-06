package com.pixxy.noti;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public class FileUploadedEventConsumer {
	@KafkaListener(topics = "file-uploaded")
	public void handle(FileUploadedEvent event) {
		System.out.println(event);
		System.out.println("Notification triggered for user={} file={}" + event.fileName() + event.fileId());
	}
}
