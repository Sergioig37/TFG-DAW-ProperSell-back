package es.proyecto.sergio.filter;

import java.io.IOException;

import es.proyecto.sergio.service.JwtService;
import es.proyecto.sergio.service.UserDetailsServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component // Marca esta clase como un componente de Spring para que pueda ser detectada y gestionada por el contenedor de Spring
public class JwtAuthenticationFilter extends OncePerRequestFilter { // Extiende OncePerRequestFilter para asegurar que el filtro se ejecuta una vez por solicitud

	@Autowired
	JwtService jwtService; // Servicio para gestionar operaciones relacionadas con JWT

	@Autowired
	UserDetailsServiceImpl userDetailsServiceImpl; // Servicio para cargar detalles de usuario

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
		// Obtiene el encabezado de autorización de la solicitud HTTP
		String authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
		String jwt;
		String username;

		// Verifica si el encabezado de autorización es nulo o no comienza con "Bearer "
		if (authHeader == null || !authHeader.startsWith("Bearer ")) {
			filterChain.doFilter(request, response); // Continúa con el siguiente filtro en la cadena si el encabezado no es válido
			return;
		}

		// Extrae el token JWT del encabezado de autorización
		jwt = authHeader.substring(7);

		// Obtiene el nombre de usuario del token JWT
		username = jwtService.getUsernameFromToken(jwt);

		// Verifica si el nombre de usuario no es nulo y no hay una autenticación existente en el contexto de seguridad
		if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
			// Carga los detalles del usuario utilizando el nombre de usuario extraído del token
			UserDetails userDetails = userDetailsServiceImpl.loadUserByUsername(username);

			// Verifica si el token JWT es válido para los detalles del usuario cargados
			if (jwtService.isTokenValid(jwt, userDetails)) {
				// Crea un token de autenticación para el usuario
				UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());

				// Establece los detalles de la solicitud web en el token de autenticación
				authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

				// Establece el token de autenticación en el contexto de seguridad
				SecurityContextHolder.getContext().setAuthentication(authToken);
			}
		}

		// Continúa con el siguiente filtro en la cadena
		filterChain.doFilter(request, response);
	}
}
