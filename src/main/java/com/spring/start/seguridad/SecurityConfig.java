package com.spring.start.seguridad;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

	@Bean
	BCryptPasswordEncoder bCryptPasswordEncoder() {
		return new BCryptPasswordEncoder();
	}

	@Bean
	AuthenticationManager authenticationManager(HttpSecurity http, BCryptPasswordEncoder bCryptPasswordEncoder,
			UserDetailsService userDetailsService) throws Exception {
		return http.getSharedObject(AuthenticationManagerBuilder.class).userDetailsService(userDetailsService)
				.passwordEncoder(bCryptPasswordEncoder).and().build();
	}

	@Bean
	public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
		http.csrf(AbstractHttpConfigurer::disable).authorizeHttpRequests(
				authorizationManagerRequestMatcherRegistry -> authorizationManagerRequestMatcherRegistry
						.requestMatchers(HttpMethod.DELETE).hasRole("ADMIN").requestMatchers("/").permitAll()
						.requestMatchers("/agente", "/agenteCliente", "/cliente").authenticated()
						.requestMatchers("/cliente/del/**", "/agente/del/**", "/agenteCliente/del/**",
								"/cliente/edit/**", "/agente/edit/**", "/agenteCliente/edit/**")
						.hasAuthority("ADMIN").anyRequest().authenticated())
				.formLogin(formLogin -> formLogin.permitAll());

		return http.build();
	}
}
