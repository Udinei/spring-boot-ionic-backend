package com.udineisilva.cursomc.config;

import java.text.ParseException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import com.udineisilva.cursomc.services.DBService;
import com.udineisilva.cursomc.services.EmailService;
import com.udineisilva.cursomc.services.MockEmailService;
	
/** Essa classe define as configuração em tempo de teste da aplicação 
 *  Classe de instanciação e população do BD H2,
 *  Classe de Mock de email */
@Configuration
@Profile("test")
public class TestConfig {
	
	@Autowired
	private DBService dbService;
	
	@Bean
	public boolean instantiateDatabase() throws ParseException{
		dbService.instantiateTestDatabase();
		
		return true;
		
	}
	
	@Bean
	public EmailService emailService(){
		return new MockEmailService();
	}

}
