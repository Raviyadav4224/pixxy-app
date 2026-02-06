package com.pixxy.user.Dto;

import java.time.Instant;

public record FileResponse(
        Long id,
        String originalFileName,
        String contentType,
        Long size,
        Instant createdAt
) {}
