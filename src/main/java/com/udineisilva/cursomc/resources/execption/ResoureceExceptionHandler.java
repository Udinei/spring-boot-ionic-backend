package com.udineisilva.cursomc.resources.execption;

import javax.servlet.http.HttpServletRequest;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import com.udineisilva.cursomc.services.exceptions.DataIntegrityException;
import com.udineisilva.cursomc.services.exceptions.ObjectNotFoundException;

/** Essa classe é um manipulador de exceção dos resources da aplicação.
 *  Invocara um metodo sempre que uma exceção for lancada, sendo que e essa exceção a ser tratada
 *  deve estar na anotation @ExceptionHandler(nomeClasseExcecao.class) na definição de um metodo, 
 *  que sera executado quando a excecao for lancada.
 *  */
@ControllerAdvice
public class ResoureceExceptionHandler {
	
	// Quando a excecao ObjectNotFoundException que eu criei for lancada chama o metodo - objectNotFoundExcepciont
	// e os dados vindos da requisicao em HttpServletRequest
	@ExceptionHandler(ObjectNotFoundException.class) 
	public ResponseEntity<StandardError> objectNotFoundException(ObjectNotFoundException e, HttpServletRequest request){
		
		// preenche o objeto StandardError com as informações de erro fornecidas pelo request, e pelo metodo que lancou o erro, e o momento 
		StandardError err = new StandardError(HttpStatus.NOT_FOUND.value(), e.getMessage(), System.currentTimeMillis());
		
		// retorna a classe StandardError no formato json com os dados do erro pra o browser 
		return ResponseEntity.status(HttpStatus.NOT_FOUND).body(err);
	}
	
	@ExceptionHandler(DataIntegrityException.class) 
	public ResponseEntity<StandardError> ConstraintViolation(DataIntegrityException e, HttpServletRequest request){
		
		// lança codigo http =  BAD_REQUEST  
		StandardError err = new StandardError(HttpStatus.BAD_REQUEST.value(), e.getMessage(), System.currentTimeMillis());
		
		// retorna a classe StandardError no formato json com os dados do erro pra o browser 
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(err);
	}

}
