package com.udineisilva.cursomc.services.exceptions;

/*** Nota: Essa classe é do meu tipo (personalizada) e de dentro do meu pacote */
public class ObjectNotFoundException extends RuntimeException {

		private static final long serialVersionUID = 1L;
		
		public ObjectNotFoundException(String msg){
			super(msg);
			
		}
		
		
		// Throwable - informa a causa do que aconteceu antes
		public ObjectNotFoundException(String msg, Throwable cause){
			super(msg, cause);
			
		}
		
		

}
