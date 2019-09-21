package com.udineisilva.cursomc.resources;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.udineisilva.cursomc.domain.Pedido;
import com.udineisilva.cursomc.services.PedidoService;

@RestController
@RequestMapping(value="/pedidos")
public class PedidoResource {
	
	@Autowired
	PedidoService pedidoService;
	
	
	//@RequestMapping(value="/{id}", method=RequestMethod.GET)
	@GetMapping(value="/{id}")
	public ResponseEntity<?> find(@PathVariable Integer id){
		Pedido obj = pedidoService.find(id);	
		return ResponseEntity.ok().body(obj);
	}
}
