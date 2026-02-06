package com.pixxy.noti;

import java.time.Instant;

public record FileUploadedEvent(String userId,
        String fileId,
        String fileName,
        Instant uploadedAt
        ) {

}
