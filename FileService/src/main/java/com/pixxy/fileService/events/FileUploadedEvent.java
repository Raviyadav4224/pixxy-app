package com.pixxy.fileService.events;

import java.time.Instant;

public record FileUploadedEvent(String userId,
        String fileId,
        String fileName,
        Instant uploadedAt
        ) {

}
