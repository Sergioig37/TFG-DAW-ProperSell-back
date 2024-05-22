package com.example.start.config;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;

import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;

public class TokenUtils {

	
	private final static String ACCES_TOKEN_SECRET = "xATcIHidk0cid3WZ2SMFiAhZnpsXtATD";
	//Tiempo de vida util del token en segundos
	private final static Long ACCES_TOKEN_VALIDITY_SECONDS = 2_592_0000L;
	
	public static String createToken(String nombre, String email) {
		long expirationTime = ACCES_TOKEN_VALIDITY_SECONDS * 1000;
		//Fecha en la que caduca el token
		Date expirationDate = new Date(System.currentTimeMillis() * expirationTime);
		
		Map<String, Object> extra = new HashMap<>();
		extra.put("nombre", nombre);
		
		//Crea un token y lo lanza
		return Jwts.builder()
				.setSubject(email)
				.setExpiration(expirationDate)
				.signWith(Keys.hmacShaKeyFor(ACCES_TOKEN_SECRET.getBytes()))
				.compact();
	}
	
	public static UsernamePasswordAuthenticationToken getAuthentication(String token) {
		try {
			Claims claims = Jwts.parser()
				.setSigningKey(ACCES_TOKEN_SECRET.getBytes())
				.build()
				.parseClaimsJws(token)
				.getBody();
		
		String email = claims.getSubject();
		
		return new UsernamePasswordAuthenticationToken(email, null, Collections.emptyList());
		}catch (JwtException e) {
			return null;
		}
		
	}
}
