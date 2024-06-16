package es.proyecto.sergio.service;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

import javax.crypto.SecretKey;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;

@Service
public class JwtService {

    private final static String SECRET_KEY = "2af73203581a1dd684c7c33b0a6000693276fa0a5819342b20cda963ebb119bd"; // Clave secreta para firmar los tokens JWT


    @Value("${security.jwt.expiration-time}")
    private long jwtExpiration; // Tiempo de expiración del JWT configurado en properties

    // Genera un token JWT para un usuario dado
    public String getToken(UserDetails user) {

        return getToken(new HashMap<>(), user);  // Llama a la sobrecarga del método con un mapa de claims vacío
    }

    // Genera un token JWT con claims adicionales y detalles del usuario
    private String getToken(Map<String, Object> extraClaims, UserDetails user) {



        return Jwts.builder()
                .claims(extraClaims) // Establece los claims adicionales
                .subject(user.getUsername()) // Establece el sujeto del token (nombre de usuario)
                .issuedAt(new Date(System.currentTimeMillis())) // Establece la fecha de emisión del token
                .expiration(new Date(System.currentTimeMillis() + jwtExpiration)) // Establece la fecha de expiración del token
                .signWith(getKey()) // Firma el token con la clave secreta
                .claim("role", user.getAuthorities()) // Añade el rol del usuario como claim
                .compact(); // Construye y devuelve el token JWT
    }

    // Decodifica la clave secreta y la convierte en una clave HMAC-SHA
    private SecretKey getKey() {

        byte[] keyBytes = Decoders.BASE64.decode(SECRET_KEY); // Decodifica la clave secreta desde Base64

        return Keys.hmacShaKeyFor(keyBytes); // Devuelve la clave secreta en formato HMAC-SHA
    }

    // Extrae el nombre de usuario del token JWT
    public String getUsernameFromToken(String token) {

        return getClaim(token, Claims::getSubject);// Utiliza el claim resolver para obtener el sujeto (nombre de usuario)
    }


    // Verifica si el token JWT es válido para el usuario dado
    public boolean isTokenValid(String token, UserDetails userDetails) {

        String username = getUsernameFromToken(token); // Obtiene el nombre de usuario del token
        return (username.equals(userDetails.getUsername()) && !isTokenExpired(token)); // Verifica si el nombre de usuario coincide y el token no ha expirado
    }


    // Extrae todos los claims del token JWT
    private Claims getAllClaims(String token) {


        return Jwts.parser()
                .verifyWith(getKey()) // Configura la clave secreta para verificar el token
                .build()
                .parseSignedClaims(token)// Parse el token y obtiene los claims
                .getPayload();// Devuelve el payload del token
    }

    // Obtiene un claim específico del token JWT utilizando un claims resolver
    public <T> T getClaim(String token, Function<Claims, T> claimsResolver) {
        Claims claims = getAllClaims(token); // Obtiene todos los claims del token
        return claimsResolver.apply(claims); // Aplica el resolver para obtener el claim específico
    }

    // Obtiene la fecha de expiración del token JWT
    public Date getExpirationDate(String token) {
        return getClaim(token, Claims::getExpiration); // Utiliza el claim resolver para obtener la fecha de expiración
    }

    // Verifica si el token JWT ha expirado
    public boolean isTokenExpired(String token) {
        return getExpirationDate(token).before(new Date()); // Compara la fecha de expiración con la fecha actual
    }
}
