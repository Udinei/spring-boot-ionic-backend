package com.udineisilva.cursomc.config;

import java.util.Arrays;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter{
	
	@Autowired
	private Environment env;
	
	
	// acesso publico permite o acesso a todos as urls abaixo sem autenticação
	private static final String[] PUBLIC_MATCHERS = {
			"/h2-console/**",
	};
	
	// permite o acesso somente de leitura, a todas as urls abaixo sem autenticacao
	private static final String[] PUBLIC_MATCHERS_GET = {
				"/produtos/**",
				"/categorias/**"
	};
	
	
	@Override
	protected void configure(HttpSecurity http) throws Exception {
		
		// desabilita a proteção do BD H2, quando estiver sendo usado o profile de test
		if(Arrays.asList(env.getActiveProfiles()).contains("test")) {
			http.headers().frameOptions().disable();
		}
		// desabilita a proteção de ataques CSRF, pois a aplicação não trabalha com sessão
		http.cors().and().csrf().disable(); 
		http.authorizeRequests()
		.antMatchers(HttpMethod.GET, PUBLIC_MATCHERS_GET).permitAll()  // somente o metodo GET
		.antMatchers(PUBLIC_MATCHERS).permitAll()                      // todos os metodos GET, PUT, POST, DELETE   
		.anyRequest().authenticated(); // Os acessos daqui pra cima esta liberado, daqui para baixo exige autenticação
		
		// Não permite a aplicação criar sessao de usuarios
		http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
		
	}
	
	
	// este metodo habilita o acesso a aplicação, vindo de varias fontes externas
	@Bean
	CorsConfigurationSource corsConfigurationSource() {
		CorsConfiguration configuration = new CorsConfiguration().applyPermitDefaultValues();
		configuration.setAllowedMethods(Arrays.asList("POST", "GET", "PUT", "DELETE", "OPTIONS"));
		
		final UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
		source.registerCorsConfiguration("/**", configuration);
		
		return source;
	}
}
