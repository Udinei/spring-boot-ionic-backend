package com.udineisilva.cursomc.security;

import java.util.Date;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

/** Essa classe gera o token, a ser enviado nas requisições, com base 
 * no algoritimo HS512 e na palavra secret,    */
@Component
public class JWTUtil {
	
	@Value("${jwt.secret}")
	private String secret;
	
	@Value("${jwt.expiration}")
	private	Long expiration;
	
	public String generateToken(String username){
		return Jwts.builder()
				.setSubject(username)
				.setExpiration(new Date(System.currentTimeMillis() + expiration))
				.signWith(SignatureAlgorithm.HS512, secret.getBytes())
				.compact();
				
	}

	public boolean tokenValido(String token) {
		
		// armazena as reinvidicacoes do token do usuario
		Claims claims = getClaims(token);
		
		if(claims != null){
			String username = claims.getSubject();
			Date expirationDate = claims.getExpiration();
			Date now = new Date(System.currentTimeMillis());
			if(username != null && expirationDate != null && now.before(expirationDate)){
				return true;
			}
			
		}
		return false;
	}

	
	private Claims getClaims(String token) {
		try {
			return Jwts.parser().setSigningKey(secret.getBytes()).parseClaimsJws(token).getBody();		
		} catch (Exception e) {
           return null;
		}
	
	}
	

	public String getUsername(String token) {
		// armazena as reinvidicacoes do token do usuario
		Claims claims = getClaims(token);
		
		if(claims != null){
		    return claims.getSubject();
		}
		return null;
	}

}
