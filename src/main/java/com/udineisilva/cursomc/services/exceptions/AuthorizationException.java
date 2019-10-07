package com.udineisilva.cursomc.services.exceptions;

/*** Nota: Essa classe é do meu tipo (personalizada) e de dentro do meu pacote */
public class AuthorizationException extends RuntimeException {

		private static final long serialVersionUID = 1L;
		
		public AuthorizationException(String msg){
			super(msg);
			
		}
		
		
		// Throwable - informa a causa do que aconteceu antes
		public AuthorizationException(String msg, Throwable cause){
			super(msg, cause);
			
		}
		
		

}
