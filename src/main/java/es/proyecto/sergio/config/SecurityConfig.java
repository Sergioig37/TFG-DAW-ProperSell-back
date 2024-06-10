package es.proyecto.sergio.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import es.proyecto.sergio.filter.JwtAuthenticationFilter;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {

	@Autowired
	JwtAuthenticationFilter jwtAuthenticationFilter;
	@Autowired
	AuthenticationProvider authProvider;

	@Bean
	public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
		http.csrf(csrf -> csrf.disable())
				.authorizeHttpRequests(authRequest -> authRequest
						.requestMatchers("/auth/**", "/propiedad", "/propiedad/habilitadas").permitAll()
						.requestMatchers("/estadisticas/**").hasAuthority("ADMIN")
						.requestMatchers("/alerta/**").hasAuthority("ADMIN")
						.requestMatchers("/usuario/usuario/{id}").hasAuthority("ADMIN")
						.requestMatchers("/usuario/usuarioExcluido/{id}").hasAuthority("ADMIN")
						.requestMatchers("/usuario/enabled/{id}/{enabled}").hasAuthority("ADMIN")
						.requestMatchers("/usuario/del/**").hasAnyAuthority("USER", "ADMIN")
						.requestMatchers("/usuario/edit/**").hasAnyAuthority("USER", "ADMIN")
						.requestMatchers("/usuario/propiedades/**").hasAuthority("USER")
						.requestMatchers("/usuario/{id}/alertas").hasAuthority("USER")
						.requestMatchers("/usuario/{id}/alertasDisponibles").hasAuthority("USER")
						.requestMatchers("/usuario/{idUsuario}/{id}/{add}").hasAuthority("USER")
						.requestMatchers("/propiedad/del/{id}/{idUser}").hasAuthority("USER")
						.requestMatchers("/propiedad/save").hasAuthority("USER")
						.requestMatchers("/propiedad/edit/{id}/{idUser}").hasAuthority("USER")
						.requestMatchers("/propiedad/enabled/**").hasAuthority("ADMIN")
						.anyRequest().authenticated()
				)
				.sessionManagement(
						sessionManager -> sessionManager.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
				.authenticationProvider(authProvider)
				.addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
				.logout(logout -> logout.permitAll());
		return http.build();
	}

	@Bean
	public WebMvcConfigurer corsConfigurer() {
		return new WebMvcConfigurer() {
			@Override
			public void addCorsMappings(CorsRegistry registry) {
				registry.addMapping("/**")
						.allowedOriginPatterns("*")
						.allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")
						.allowCredentials(true)
						.allowedHeaders("Authorization", "Content-Type")
						;
			}
		};
	}


}
