package com.pixxy.user.Dto;

import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class UpdateUserDto {

	private final String emailRegex = "^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$";

	@Pattern(regexp = emailRegex, message = "Email does not meet the required format. It must contain a valid username, '@' symbol, domain name, and extension (e.g., name@example.com).")
	private String email;

	private String userName;

	private String firstName;

	private String lastName;

	private String profilePictureUrl;
}
