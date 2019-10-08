package com.udineisilva.cursomc.services.validation;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.HandlerMapping;

import com.udineisilva.cursomc.domain.Cliente;
import com.udineisilva.cursomc.dto.ClienteDTO;
import com.udineisilva.cursomc.repositories.ClienteRepository;
import com.udineisilva.cursomc.resources.exception.FieldMessage;

public class ClienteUpdateValidator implements ConstraintValidator<ClienteUpdate, ClienteDTO> {
	
	@Autowired
	private HttpServletRequest request;
	
	@Autowired
	ClienteRepository clienteRepository;
	
	@Override
	public void initialize(ClienteUpdate ann) {
	}

	@Override
	public boolean isValid(ClienteDTO objDto, ConstraintValidatorContext context) {
		
		// Obtem o id do objeto Cliente enviado na uri durante a execução
		// do metodo PUT (alteracao). No corpo do objeto Cliente(Json), 
		// e enviado somente os dados que se deseja alterar 
		@SuppressWarnings("unchecked")
		Map<String, String> map = (Map<String, String>) request.getAttribute(HandlerMapping.URI_TEMPLATE_VARIABLES_ATTRIBUTE);
		Integer uriId = Integer.parseInt(map.get("id"));
		
		List<FieldMessage> list = new ArrayList<>();
			
		Cliente aux =  clienteRepository.findByEmail(objDto.getEmail());
		// verifica se é uma alteração, nesse caso se o id e o email do cliente são os mesmos, é uma alteração não sobe a msg de erro  
		if(aux != null && !aux.getId().equals(uriId)){
			list.add(new FieldMessage("email", "Email já existente"));
		}
		
		for (FieldMessage e : list) {
			context.disableDefaultConstraintViolation();
			context.buildConstraintViolationWithTemplate(e.getMessage()).addPropertyNode(e.getFieldName())
					.addConstraintViolation();
		}
		return list.isEmpty();
	}
}
