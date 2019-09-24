package com.udineisilva.cursomc.services.exceptions;

/*** Nota: Essa classe é do meu tipo (personalizada) e de dentro do meu pacote */
public class DataIntegrityException extends RuntimeException {

		private static final long serialVersionUID = 1L;
		
		public DataIntegrityException(String msg){
			super(msg);
			
		}
		
		
		// Throwable - informa a causa do que aconteceu antes
		public DataIntegrityException(String msg, Throwable cause){
			super(msg, cause);
			
		}
		
		

}
