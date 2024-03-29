package com.udineisilva.cursomc.config;

import java.util.Properties;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;

/**
 * Classe de configuração do envio de emails @PropertySource({
 * "classpath:env/mail-${ambiente:local}.properties" }) Acessa o arquivo padrao
 * de configuração de envio de email. classpath:env/ - endereco dentro da
 * aplicacao onde ficam os arquivos de configuracao de email
 * mail-${ambiente:local}.properties - nome completo do arquivo onde ficam os
 * dados de acesso a caixa de email ambiente - nome dado a variavel padrao do
 * tomcat (embarcado), em: Tomcat v8.0 Server at localhost Server General
 * Information > Open Lauch Configuration > Enviroment O nome do arquivo será
 * formado pelo conteudo da variavel ambiente: + "local" Caso não encontre no
 * arquivo mail-ambiente:local as configuração, vai executando na sequencia as
 * outras anottation @PropertySource até achar os dados necessarios (username e
 * password). Caso as propriedades nos arquivos tiverem os mesmos nomes esses
 * serão sobreescritos. Caso uma propriedade nao exista pode ser usada uma
 * propriedade username por exemplo de um arquivo e password de outro.
 * ignoreResourceNotFound - caso não encontre o arquivo, sobe a aplicacao sem
 * exibir erros no console.
 * 
 * @PropertySource - Essa segunda anotação usa arquivo externo
 *                 "usbase-mail.properties evitando assim que dados reais ou
 *                 temporarios fiquem expostos, quando subir a aplicação em
 *                 producao ou no github.
 * 
 */

@Configuration
@PropertySource(value = { "file:${USERPROFILE}/.mail.cursomc/cursomc-mail.properties"}, ignoreResourceNotFound = true)
public class MailConfig {

	// injeta objeto que acessa o classpath da aplicacao env
	@Autowired
	private Environment env;
	
	@Value("${spring.profiles.active}")
	private String profile;

	@Value("${spring.mail.username}")
	private String emailProd;
				
	@Value("${spring.mail.password}")
	private String passwordEmailProd;
	
	
	@Bean
	public JavaMailSender mailSender() {
		JavaMailSenderImpl mailSender = new JavaMailSenderImpl();

		// servidor de email usando gmail
		mailSender.setHost("smtp.gmail.com");
		mailSender.setPort(587);

		if(profile.equals("prod")){
			// em produção carrega dados do email do arquivo application-prod.properties
			mailSender.setUsername(emailProd);
			mailSender.setPassword(passwordEmailProd);
			
		}else if (profile.equals("dev") || profile.equals("test")){
			// em dev ou test carrega dados do arquivo externo local, cursomc.mail-properties
			mailSender.setUsername(env.getProperty("username.email"));
			mailSender.setPassword(env.getProperty("password"));
		}
					
		System.out.println(">>>> username: " + mailSender.getUsername());
		System.out.println(">>>> password: " + mailSender.getPassword());
		
				
		Properties props = new Properties();
		props.put("mail.transport.protocol", "smtp");
		props.put("mail.smtp.auth", true);
		props.put("mail.smtp.starttls.enable", true);
		props.put("mail.debug", false);
		props.put("mail.smtp.connectiontimeout", 10000); // miliseconds

		mailSender.setJavaMailProperties(props);

		return mailSender;

	}
}
