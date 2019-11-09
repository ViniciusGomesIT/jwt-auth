package br.com.api.security.utils;

import static java.util.Objects.nonNull;

import java.io.Serializable;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

@Component
public class JwtTokenUtil implements Serializable {
	
	private static final long serialVersionUID = 3862081440499016511L;

	@Value("${jwt.secret}")
	private String secret;
	
	@Value("${jwt.expiration}")
	private Long expiration;

	//retrieve username from jwt token
	public String getUsernameFromToken(String token) {
		
		if ( nonNull(token) ) {
			return getClaimFromToken(token, Claims::getSubject);
		} 
		
		return null;
	}
	
	public <T> T getClaimFromToken(String token, Function<Claims, T> claimsResolver) {
		final Claims claims = getAllClaimsFromToken(token);
		
		return claimsResolver.apply(claims);
	}
	
	//for retrieveing any information from token we will need the secret key
	private Claims getAllClaimsFromToken(String token) {
			return Jwts.parser()
					.setSigningKey(secret)
					.parseClaimsJws(token)
					.getBody();
	}
	
	//generate token for user
	public String generateToken(UserDetails userDetails) {
		Map<String, Object> claims = new HashMap<>();
		
		return doGenerateToken(claims, userDetails.getUsername());
	}
	
	//while creating the token -
	//1. Define  claims of the token, like Issuer, Expiration, Subject, and the ID
	//2. Sign the JWT using the HS512 algorithm and secret key.
	//3. According to JWS Compact Serialization(https://tools.ietf.org/html/draft-ietf-jose-json-web-signature-41#section-3.1)
	//   compaction of the JWT to a URL-safe string 
	private String doGenerateToken(Map<String, Object> claims, String subject) {
		return Jwts.builder()
				.setClaims(claims)
				.setSubject(subject)
				.setIssuedAt(new Date())
				.setExpiration(new Date(System.currentTimeMillis() + expiration * 1000))
				.signWith(SignatureAlgorithm.HS512, secret).compact();
	}
	
	//validate token
	public boolean validateToken(String token, UserDetails userDetails) {
		if ( nonNull(token) && !token.isEmpty() ) {
			final String username = getUsernameFromToken(token);
			
			return ( username.equals(userDetails.getUsername()) && !isTokenExpired(token) );
		}
		
		return false;
	}
	
	//check if token is not expired to be refreshed
	public boolean canTokenBeRefreshed(String token) {
		if ( nonNull(token) && !token.isEmpty() ) {
			return ( !isTokenExpired(token) );
		}
		
		return false;
	}
	
	//check if the token has expired
	private boolean isTokenExpired(String token) {
		final Date expirationDate = getExpirationDateFromToken(token);
		
		return expirationDate.before(new Date());
	}
	
	//retrieve expiration date from jwt token
	public Date getExpirationDateFromToken(String token) {
		return getClaimFromToken(token, Claims::getExpiration);
	}

	//generate a new one token
	public String refreshToken(String token) {
		
		if ( nonNull(token) && !token.isEmpty() ) {
			final Claims claims = getAllClaimsFromToken(token);
			
			return doGenerateToken(claims, getUsernameFromToken(token));
		}
		
		//TODO throw an exception
		return null;
	}
}
