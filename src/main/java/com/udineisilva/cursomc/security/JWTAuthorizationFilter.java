package com.udineisilva.cursomc.security;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.junit.runners.Parameterized.UseParametersRunnerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

public class JWTAuthorizationFilter extends BasicAuthenticationFilter {

	@Autowired
	private JWTUtil jwtUtil;
	
	@Autowired
	private UserDetailsService userDetailsService;
	
	public JWTAuthorizationFilter(AuthenticationManager authenticationManager, JWTUtil jwtUtil,  UserDetailsService userDetailsService ) {
		super(authenticationManager);
		this.jwtUtil = jwtUtil;
		this.userDetailsService = userDetailsService;
	}
	
	@Override
	protected void doFilterInternal(HttpServletRequest request,
									HttpServletResponse response,
									FilterChain chain)	throws IOException, ServletException {
	
		String header = request.getHeader("Authorization");
		
		
		if(header != null && header.startsWith("Bearer ")){
			// libera autorizacao do usuario
			UsernamePasswordAuthenticationToken auth = getAuthentication(request, header.substring(7));
			
			if(auth != null){
				SecurityContextHolder.getContext().setAuthentication(auth);
			}
		}
		
		// apos autenticar libera as requisições
	   chain.doFilter(request, response);			
	}

	// verifica se o  usuario esta autorizado 
	private UsernamePasswordAuthenticationToken getAuthentication(HttpServletRequest request, String token) {
        
		if(jwtUtil.tokenValido(token)){
			String username = jwtUtil.getUsername(token);
			UserDetails user = userDetailsService.loadUserByUsername(username);
			return new UsernamePasswordAuthenticationToken(user,  null, user.getAuthorities());
		}
		
		return null;
	}

}
