package es.proyecto.sergio.config;

import es.proyecto.sergio.service.UserDetailsServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import es.proyecto.sergio.dao.UsuarioDAO;

@Service // Marca esta clase como un servicio de Spring
public class ApplicationConfig {

	@Autowired
	UsuarioDAO usuarioDAO; // Inyección de dependencia para el acceso a datos de usuario

	@Autowired
	UserDetailsServiceImpl userDetailsServiceImpl; // Inyección de dependencia para el servicio de detalles del usuario

	@Bean // Define un bean para el codificador de contraseñas
	public PasswordEncoder bCryptPasswordEncoder() {
		return new BCryptPasswordEncoder(); // Usa BCrypt para encriptar contraseñas
	}

	@Bean // Define un bean para el gestor de autenticación
	public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
		return config.getAuthenticationManager(); // Obtiene y devuelve el AuthenticationManager de la configuración de autenticación
	}

	@Bean // Define un bean para el proveedor de autenticación
	public AuthenticationProvider authenticationProvider() {
		DaoAuthenticationProvider authenticationProvider = new DaoAuthenticationProvider();
		authenticationProvider.setUserDetailsService(userDetailsService()); // Asocia el servicio de detalles de usuario
		authenticationProvider.setPasswordEncoder(bCryptPasswordEncoder()); // Asocia el codificador de contraseñas
		return authenticationProvider; // Devuelve el proveedor de autenticación configurado
	}

	@Bean // Define un bean para el servicio de detalles de usuario
	private org.springframework.security.core.userdetails.UserDetailsService userDetailsService() {
		return username -> userDetailsServiceImpl.loadUserByUsername(username); // Usa el método loadUserByUsername del UserDetailsServiceImpl
	}
}
