package com.udineisilva.cursomc.config;

import java.text.ParseException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import com.udineisilva.cursomc.services.DBService;
import com.udineisilva.cursomc.services.EmailService;
import com.udineisilva.cursomc.services.SmtpEmailService;
	
@Configuration
@Profile("dev")
public class DevConfig {
	
	@Autowired
	private DBService dbService;
	
	// obtem o status para criacao do bd, do arquivo application-dev.properties
	@Value("${spring.jpa.hibernate.ddl-auto}")
	private String strategy;
	
	@Bean
	public boolean instantiateDatabase() throws ParseException{
		
		// somente cria novamente o banco de dados se for solicitado 
		if(!"create".equals(strategy)){
			return false;
		}
	
		// chama o servico pra instanciação dos objetos da aplicação em banco de dados
		dbService.intantiateTestDatabase();
		return true;
		
	}
	
	@Bean
	public EmailService emailService(){
		return new SmtpEmailService();
	}

}
