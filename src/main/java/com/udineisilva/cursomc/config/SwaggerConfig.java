package com.udineisilva.cursomc.config;

import org.springframework.context.annotation.Bean;

import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

/** 
 * Essa classe utiliza o swagger para gerar documentação da API automaticamente a
 *  partir do projeto. Automatiza o processo de geração e atualização da documentação.
 *  acessar o no caminho http:localhost/<portaApp>/swagger-ui.html 
 * 
 */
@EnableSwagger2
public class SwaggerConfig {
	@Bean
	public Docket api() {
		return new Docket(DocumentationType.SWAGGER_2)
				.select()
				.apis(RequestHandlerSelectors.any())
				.paths(PathSelectors.any())
				.build();
	}
}