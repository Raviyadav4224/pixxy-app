package com.pixxy.fileService.config;

import static org.springframework.security.config.Customizer.withDefaults;

//import javax.crypto.spec.SecretKeySpec;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
//import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

//import com.pixxy.fileService.filters.GatewayAuthenticationFilter;

@EnableWebSecurity
@Configuration
public class SecurityConfig {

//	JwtFilter jwtfilter;
//
//	public SecurityConfig(JwtFilter jwtfilter) {
//		this.jwtfilter = jwtfilter;
//	}
//
//	@Bean
//	JwtDecoder jwtDecoder(JwtProperties properties) {
//		return NimbusJwtDecoder.withSecretKey(new SecretKeySpec(properties.getSecret().getBytes(), "HmacSHA256"))
//				.build();
//	}

//	private final GatewayAuthenticationFilter gatewayAuthenticationFilter;
//
//	public SecurityConfig(GatewayAuthenticationFilter gatewayAuthenticationFilter) {
//		this.gatewayAuthenticationFilter = gatewayAuthenticationFilter;
//	}

	@Bean
	SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

		http.csrf((csrf) -> csrf.disable()).cors(withDefaults())
//				.authorizeHttpRequests(auth -> auth.anyRequest().authenticated())
				.authorizeHttpRequests(auth -> auth.anyRequest().permitAll())
//				.addFilterBefore(gatewayAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
				.sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS));

		return http.build();
	}
}
