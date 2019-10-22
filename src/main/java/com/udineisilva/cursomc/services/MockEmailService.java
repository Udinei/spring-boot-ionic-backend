package com.udineisilva.cursomc.services;

import javax.mail.internet.MimeMessage;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.mail.SimpleMailMessage;

/** Essa classe somente sera executa automaticamente no envio de emails,
 *  quando o profile text estiver ativo no application.properties*/
public class MockEmailService extends AbstractEmailService {
	
	private static final Logger LOG = LoggerFactory.getLogger(MockEmailService.class);	

	public void sendEmail(SimpleMailMessage msg){
		LOG.info("Simulando o envio de email simples...");
		LOG.info(msg.toString());
		LOG.info("Email simples enviado");
	}

	@Override
	public void sendHtmlEmail(MimeMessage msg) {
		LOG.info("Simulando o envio de email HTML...");
		LOG.info(msg.toString());
		LOG.info("Email HTML enviado");
		
	}
	
}
