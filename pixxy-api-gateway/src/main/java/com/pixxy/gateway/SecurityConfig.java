package com.pixxy.gateway;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.oauth2.jwt.NimbusReactiveJwtDecoder;
import org.springframework.security.oauth2.jwt.ReactiveJwtDecoder;
import org.springframework.security.web.server.SecurityWebFilterChain;

import com.pixxy.gateway.globalErrorHandler.GatewayAccessDeniedError;
import com.pixxy.gateway.globalErrorHandler.GatewayAuthEntryPoint;

@Configuration
@EnableWebFluxSecurity
public class SecurityConfig {
	@Bean
	SecurityWebFilterChain securityWebFilterChain(ServerHttpSecurity http, GatewayAuthEntryPoint authEntryPoint,
			GatewayAccessDeniedError accessDenied) {

		return http.csrf(ServerHttpSecurity.CsrfSpec::disable)

				.authorizeExchange(
						exchange -> exchange
//						Public routes
						.pathMatchers(HttpMethod.OPTIONS, "/**").permitAll()
						.pathMatchers("/api/v1/users/status/*", "/api/v1/users/register", "/api/v1/users/login").permitAll()
//						Authenticated routes
						.pathMatchers("/api/v1/**").authenticated()
//						Rest all routes
						.anyExchange().permitAll())

				.exceptionHandling(ex -> ex.authenticationEntryPoint(authEntryPoint).accessDeniedHandler(accessDenied))
				.oauth2ResourceServer(oauth2 -> oauth2.jwt(Customizer.withDefaults()))

				.build();
	}

	@Value("${jwt.secret}")
	private String SECRET;

	@Bean
	ReactiveJwtDecoder reactiveJwtDecoder() {
		SecretKey secretKey = new SecretKeySpec(SECRET.getBytes(), "HmacSHA256");
		return NimbusReactiveJwtDecoder.withSecretKey(secretKey).build();
	}

}
