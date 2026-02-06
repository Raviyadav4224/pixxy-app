package com.pixxy.fileService.utils;

import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

public class SecurityHelper {

	public static Long getCurrentUserId(Authentication auth) {

		auth = SecurityContextHolder.getContext().getAuthentication();

		if (auth == null || auth instanceof AnonymousAuthenticationToken) {
			return null;
		}

		Object principal = auth.getPrincipal();

//		if (principal instanceof Jwt jwt) {
//			System.out.println(jwt.getClaim("userId").toString());
//			return jwt.getClaim("userId");
//		}

		return null;
	}
}
