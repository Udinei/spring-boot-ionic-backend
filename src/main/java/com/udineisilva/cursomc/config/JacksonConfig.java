package com.udineisilva.cursomc.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.udineisilva.cursomc.domain.PagamentoComBoleto;
import com.udineisilva.cursomc.domain.PagamentoComCartao;

/** Essa classe permite o Jackson instanciar uma classe escrita no formato Json para Java, 
 * enviada no metodo POST, no caso do uso de herança (subclasses)  */
@Configuration
public class JacksonConfig {
	// https://stackoverflow.com/questions/41452598/overcome-can-not-construct-instance-ofinterfaceclass-
	// without-hinting-the-pare
	
	@Bean
	public Jackson2ObjectMapperBuilder objectMapperBuilder() {
	Jackson2ObjectMapperBuilder builder = new Jackson2ObjectMapperBuilder() {
		
		public void configure(ObjectMapper objectMapper) {
			objectMapper.registerSubtypes(PagamentoComCartao.class);
			objectMapper.registerSubtypes(PagamentoComBoleto.class);
			
			super.configure(objectMapper);
			}
		};
		
	return builder;
	
	}
}
