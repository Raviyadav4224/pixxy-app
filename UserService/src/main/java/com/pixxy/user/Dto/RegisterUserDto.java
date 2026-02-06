package com.pixxy.user.Dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class RegisterUserDto {

	private final String passwordRegex = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[!@#$%^&*()_+\\-=\\[\\]{};':\"\\\\|,.<>/?]).{8,20}$";

	private final String emailRegex = "^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$";

	@NotBlank(message = "Email is required")
	@Pattern(regexp = emailRegex, message = "Email does not meet the required format. It must contain a valid username, '@' symbol, domain name, and extension (e.g., name@example.com).")
	private String email;
	
	@NotBlank(message = "Username is required")
	private String userName;
	
	@NotBlank(message = "Firstname is required")
	private String firstName;
	
	@NotBlank(message = "Lastname is required")
	private String lastName;
	
	@NotBlank(message = "Password is required")
	@Pattern(regexp = passwordRegex, message = "Password does not meets the criteria of having at least one digit, one lowercase letter, one uppercase letter, one special character, and a length between 8 and 20 characters")
	private String password;
	
	
	private String profilePictureUrl;

}
