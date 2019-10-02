package com.udineisilva.cursomc.services;

import org.springframework.mail.SimpleMailMessage;

import com.udineisilva.cursomc.domain.Pedido;


public interface EmailService {
	
	void senderOrderConfirmationEmail(Pedido obj);
	void sendEmail(SimpleMailMessage msg);

}
