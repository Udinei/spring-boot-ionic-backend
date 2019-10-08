package com.udineisilva.cursomc.config;

import java.util.Arrays;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import com.udineisilva.cursomc.security.JWTAuthenticationFilter;
import com.udineisilva.cursomc.security.JWTAuthorizationFilter;
import com.udineisilva.cursomc.security.JWTUtil;

@EnableGlobalMethodSecurity(prePostEnabled = true) // proteção por metodo
@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter{
	
	@Autowired
	private JWTUtil jwtUtil;
	
	@Autowired
	private Environment env;
	
	@Autowired
	private UserDetailsService userDetailsService; 
	
	
	// acesso publico permite o acesso a todos as urls abaixo sem autenticação
	private static final String[] PUBLIC_MATCHERS = {
			"/h2-console/**",
	};
	
	
	// permite o acesso somente de leitura, a todas as urls abaixo sem autenticacao
	private static final String[] PUBLIC_MATCHERS_GET = {
				"/produtos/**",
				"/categorias/**"
	};

	// endpoints que estão liberado o acesso via POST, mesmo sem o usuario estar logado
		private static final String[] PUBLIC_MATCHERS_POST = {
				"/clientes/**",
				"/clientes/picture",
				"/auth/forgot/**" // esqueceu a senha
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
		.antMatchers(HttpMethod.POST, PUBLIC_MATCHERS_POST).permitAll() 
		.antMatchers(HttpMethod.GET, PUBLIC_MATCHERS_GET).permitAll()  // somente o metodo GET
		.antMatchers(PUBLIC_MATCHERS).permitAll()                      // todos os metodos GET, PUT, POST, DELETE   
		.anyRequest().authenticated(); // Os acessos daqui pra cima esta liberado, daqui para baixo exige autenticação
		
		// adicionando Filtro de autenticação JWT na aplicacao 
		http.addFilter(new JWTAuthenticationFilter(authenticationManager(), jwtUtil));
		
		// adicionando Filtro de autorizacão JWT na aplicacao 
		http.addFilter(new JWTAuthorizationFilter(authenticationManager(), jwtUtil, userDetailsService));

		// Não permite a aplicação guardar sessões de estado 
		http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
		
	}
	
	// Esse metodo identifica qual classe é capaz de ideintificar o usuario por email
	@Override
	public void configure(AuthenticationManagerBuilder auth) throws Exception {
		 auth.userDetailsService(userDetailsService).passwordEncoder(bCryptPasswordEncoder());
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
	
	// utilizado para criptografar senha
	@Bean
	public BCryptPasswordEncoder bCryptPasswordEncoder(){
		return new BCryptPasswordEncoder();
		
	}
	
}
