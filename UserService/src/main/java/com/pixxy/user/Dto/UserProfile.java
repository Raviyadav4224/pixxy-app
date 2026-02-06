package com.pixxy.user.Dto;

import java.time.Instant;
import java.util.List;

public record UserProfile( Long id,
        String email,
        String userName,
        String firstName,
        String lastName,
        String profilePictureUrl,
        String role,
        boolean emailVerified,
        Instant createdAt,
        List<FileResponse> files) {

}
