package com.udineisilva.cursomc.security;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.udineisilva.cursomc.dto.CredenciaisDTO;
                                             
/**
 * Esse classe intercepta as requisições de login (nota: /login é um caminho reservado do spring security)
 * para tanto deve estender a classe UsernamePasswordAuthenticationFilter 
 * */
public class JWTAuthenticationFilter extends UsernamePasswordAuthenticationFilter {
	
	// Os dois atributos abaixo seram injetados via o construtor da classe
	private AuthenticationManager authenticationManager;
	private JWTUtil jwtUtil;
	
	public JWTAuthenticationFilter(AuthenticationManager authenticationManager, JWTUtil jwtUtil) {
		// se houver erros, corrige o erro de falhas na autenticação
		setAuthenticationFailureHandler(new JWTAuthenticationFailureHandler());
		
		this.authenticationManager = authenticationManager;
		this.jwtUtil = jwtUtil;
		
	}
	
	
	// tenta autenticar o usuario informado na requisicao
	@Override
	public Authentication attemptAuthentication(HttpServletRequest req, 
											    HttpServletResponse res) throws AuthenticationException {
		
		try {
			// pegando os dados vindos da requisicao e convertendo para CredenciaisDTO
			CredenciaisDTO creds = new ObjectMapper()
					.readValue(req.getInputStream(), CredenciaisDTO.class);
			
			UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
					                                             creds.getEmail(), creds.getSenha(), new ArrayList<>());
			// verifica se o usuario é valido
			Authentication auth = authenticationManager.authenticate(authToken);
			return auth;
			
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
		
		
	}
	
	
	// gera o token e insere no request da requisição
	@Override
	protected void successfulAuthentication(HttpServletRequest req, 
		                                   HttpServletResponse res,
		                                   FilterChain chain,
		                                   Authentication auth) throws IOException, ServletException {
		
		String username = ((UserSS) auth.getPrincipal()).getUsername();
		String token = jwtUtil.generateToken(username);
		res.addHeader("Authorization", "Bearer " + token);
		res.addHeader("access-control-expose-headers", "Authorization");
		                                                
		System.out.println("Herder expose... : " + res.getHeaders("access-control-expose-headers")); 
		System.out.println("Herder Authorization... : " + res.getHeader("Authorization"));
		
		// apos autenticar libera as requisições
		// chain.doFilter(req, res);		                                   
	}
	
	// caso a auteniicação fallhar essa classe sera executada
	private class JWTAuthenticationFailureHandler implements AuthenticationFailureHandler {

		@Override
		public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response,
				AuthenticationException exception) throws IOException, ServletException {
			
			response.setStatus(401);
			response.setContentType("application/json");
			response.getWriter().append(json());
			
		}
		
		private String json(){
			long date = new Date().getTime();
			return "{\"timestatmp\": " + date + ","
					+ "\"status\": 401, "
					+ "\"error\": \"Não autorizado\", "
					+ "\"message\": \"Email ou senha inválidos\", "
					+ "\"path\": \"/login\"}"; 
		}
		
	}
	
			
}
