package com.example.start.jwt;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

import javax.crypto.SecretKey;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import com.example.start.user.Usuario;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;

@Service
public class JwtService {

	private final static String SECRET_KEY = "2af73203581a1dd684c7c33b0a6000693276fa0a5819342b20cda963ebb119bd";
	
	
	@Value("${security.jwt.expiration-time}")
    private long jwtExpiration;
	
	public String getToken(UserDetails user) {
		// TODO Auto-generated method stub
		return getToken(new HashMap<>(), user);
	}

	private String getToken(Map<String, Object> extraClaims, UserDetails user) {
		// TODO Auto-generated method stub
		return Jwts.builder()
				.claims(extraClaims)
				.subject(user.getUsername())
				.issuedAt(new Date(System.currentTimeMillis()))
				.expiration(new Date(System.currentTimeMillis()+ jwtExpiration))
				.signWith(getKey())
				.compact();
	}

	private SecretKey getKey() {
		// TODO Auto-generated method stub
		byte[] keyBytes = Decoders.BASE64.decode(SECRET_KEY);
		
		return Keys.hmacShaKeyFor(keyBytes);
	}

	
	public String getUsernameFromToken(String token) {
		// TODO Auto-generated method stub
		return getClaim(token, Claims::getSubject);
	}

	public boolean isTokenValid(String token, UserDetails userDetails) {
		// TODO Auto-generated method stub
		String username = getUsernameFromToken(token);
		return (username.equals(userDetails.getUsername())&& !isTokenExpired(token));
	}

	private Claims getAllClaims(String token) {
		
		return Jwts.parser()
				.verifyWith(getKey())
				.build()
				.parseSignedClaims(token)
				.getPayload();
	}
	
	public <T> T getClaim(String token, Function<Claims, T> claimsResolver) {
		 Claims claims = getAllClaims(token);
		return claimsResolver.apply(claims);
	}
	
	public Date getExpirationDate(String token) {
		return getClaim(token, Claims::getExpiration);
	}
	
	public boolean isTokenExpired(String token) {
		return getExpirationDate(token).before(new Date());
	}
}
