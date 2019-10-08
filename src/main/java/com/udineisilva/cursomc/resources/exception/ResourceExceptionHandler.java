package com.udineisilva.cursomc.resources.exception;

import javax.servlet.http.HttpServletRequest;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import com.amazonaws.AmazonClientException;
import com.amazonaws.AmazonServiceException;
import com.amazonaws.services.s3.model.AmazonS3Exception;
import com.udineisilva.cursomc.services.exceptions.AuthorizationException;
import com.udineisilva.cursomc.services.exceptions.DataIntegrityException;
import com.udineisilva.cursomc.services.exceptions.FileException;
import com.udineisilva.cursomc.services.exceptions.ObjectNotFoundException;

/** Essa classe é um manipulador de exceção dos resources da aplicação.
 *  Invocara um metodo sempre que uma exceção for lancada, sendo que e essa exceção a ser tratada
 *  deve estar na anotation @ExceptionHandler(nomeClasseExcecao.class) na definição de um metodo, 
 *  que sera executado quando a excecao for lancada.
 *  */
@ControllerAdvice
public class ResourceExceptionHandler {
	
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
	public ResponseEntity<StandardError> constraintViolation(DataIntegrityException e, HttpServletRequest request){
		
		// lança codigo http =  BAD_REQUEST  
		StandardError err = new StandardError(HttpStatus.BAD_REQUEST.value(), e.getMessage(), System.currentTimeMillis());
		
		// retorna a classe StandardError no formato json com os dados do erro pra o browser 
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(err);
	}

	@ExceptionHandler(MethodArgumentNotValidException.class) 
	public ResponseEntity<StandardError> validation(MethodArgumentNotValidException e, HttpServletRequest request){
		
		
		ValidationError err = new ValidationError(HttpStatus.BAD_REQUEST.value(), "Erro de validação", System.currentTimeMillis());
		// acessando todos os erros de campo do erro gerado e
		for(FieldError x : e.getBindingResult().getFieldErrors()){
			err.addError(x.getField(), x.getDefaultMessage());
		}
		
		// retorna a classe StandardError no formato json com os dados do erro pra o browser 
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(err);
	}
	
	
	@ExceptionHandler(AuthorizationException.class) 
	public ResponseEntity<StandardError> authorization(AuthorizationException e, HttpServletRequest request){
		
		// lança codigo http =  
		StandardError err = new StandardError(HttpStatus.FORBIDDEN.value(), e.getMessage(), System.currentTimeMillis());
		
		// retorna a classe StandardError no formato json com os dados do erro pra o browser 
		return ResponseEntity.status(HttpStatus.FORBIDDEN).body(err);
		
	}
	
	@ExceptionHandler(FileException.class) 
	public ResponseEntity<StandardError> file(FileException e, HttpServletRequest request){
		
		// lança codigo http =  
		StandardError err = new StandardError(HttpStatus.BAD_REQUEST.value(), e.getMessage(), System.currentTimeMillis());
		
		// retorna a classe StandardError no formato json com os dados do erro pra o browser 
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(err);
		
	}
	
	@ExceptionHandler(AmazonServiceException.class) 
	public ResponseEntity<StandardError> amazonService(AmazonServiceException e, HttpServletRequest request){
		
		HttpStatus code = HttpStatus.valueOf(e.getErrorCode());
		
		// lança codigo http =  
		StandardError err = new StandardError(code.value(), e.getMessage(), System.currentTimeMillis());
		
		// retorna a classe StandardError no formato json com os dados do erro pra o browser 
		return ResponseEntity.status(code).body(err);
	}
	
	@ExceptionHandler(AmazonClientException.class) 
	public ResponseEntity<StandardError> amazonClient(AmazonClientException e, HttpServletRequest request){
		
		// lança codigo http =  
		StandardError err = new StandardError(HttpStatus.BAD_REQUEST.value(), e.getMessage(), System.currentTimeMillis());
		
		// retorna a classe StandardError no formato json com os dados do erro pra o browser 
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(err);
		
	}
	
	@ExceptionHandler(AmazonS3Exception.class) 
	public ResponseEntity<StandardError> amazonS3(AmazonS3Exception e, HttpServletRequest request){
		
		// lança codigo http =  
		StandardError err = new StandardError(HttpStatus.BAD_REQUEST.value(), e.getMessage(), System.currentTimeMillis());
		
		// retorna a classe StandardError no formato json com os dados do erro pra o browser 
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(err);
		
	}

	
}
