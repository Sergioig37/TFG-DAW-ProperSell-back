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

@Configuration // Indica que esta es una clase de configuración de Spring
@EnableWebSecurity // Habilita la seguridad web en Spring
@EnableMethodSecurity // Habilita la seguridad basada en métodos
public class SecurityConfig {

	@Autowired
	JwtAuthenticationFilter jwtAuthenticationFilter; // Inyección de dependencia para el filtro de autenticación JWT

	@Autowired
	AuthenticationProvider authProvider; // Inyección de dependencia para el proveedor de autenticación

	@Bean // Define el bean para la cadena de filtros de seguridad
	public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
		http.csrf(csrf -> csrf.disable()) // Deshabilita la protección CSRF
				.authorizeHttpRequests(authRequest -> authRequest
						.requestMatchers("/auth/**", "/propiedad", "/propiedad/habilitadas").permitAll() // Permite acceso público a estas rutas
						.requestMatchers("/estadisticas/**").hasAuthority("ADMIN") // Restringe estas rutas a usuarios con el rol ADMIN
						.requestMatchers("/alerta/**").hasAuthority("ADMIN")
						.requestMatchers("/usuario/usuario/{id}").hasAuthority("ADMIN")
						.requestMatchers("/usuario/usuarioExcluido/{id}").hasAuthority("ADMIN")
						.requestMatchers("/usuario/enabled/{id}/{enabled}").hasAuthority("ADMIN")
						.requestMatchers("/usuario/del/**").hasAnyAuthority("USER", "ADMIN") // Restringe estas rutas a usuarios con los roles USER o ADMIN
						.requestMatchers("/usuario/edit/**").hasAnyAuthority("USER", "ADMIN")
						.requestMatchers("/usuario/propiedades/**").hasAuthority("USER") // Restringe estas rutas a usuarios con el rol USER
						.requestMatchers("/usuario/{id}/alertas").hasAuthority("USER")
						.requestMatchers("/usuario/{id}/alertasDisponibles").hasAuthority("USER")
						.requestMatchers("/usuario/{idUsuario}/{id}/{add}").hasAuthority("USER")
						.requestMatchers("/propiedad/del/{id}/{idUser}").hasAuthority("USER")
						.requestMatchers("/propiedad/save").hasAuthority("USER")
						.requestMatchers("/propiedad/edit/{id}/{idUser}").hasAuthority("USER")
						.requestMatchers("/propiedad/enabled/**").hasAuthority("ADMIN")
						.anyRequest().authenticated() // Requiere autenticación para cualquier otra solicitud
				)
				.sessionManagement(sessionManager -> sessionManager.sessionCreationPolicy(SessionCreationPolicy.STATELESS)) // Configura la política de sesiones como stateless
				.authenticationProvider(authProvider) // Configura el proveedor de autenticación
				.addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class) // Añade el filtro JWT antes del filtro de autenticación de usuario y contraseña
				.logout(logout -> logout.permitAll()); // Permite el logout para todos
		return http.build(); // Construye y devuelve la cadena de filtros de seguridad
	}

	@Bean // Define el bean para la configuración de CORS
	public WebMvcConfigurer corsConfigurer() {
		return new WebMvcConfigurer() {
			@Override
			public void addCorsMappings(CorsRegistry registry) {
				registry.addMapping("/**") // Permite CORS para todas las rutas
						.allowedOriginPatterns("*") // Permite todas las procedencias
						.allowedMethods("*") // Permite todos los métodos HTTP
						.allowCredentials(true) // Permite el uso de credenciales
						.allowedHeaders("Authorization", "Content-Type"); // Permite estos encabezados
			}
		};
	}
}
