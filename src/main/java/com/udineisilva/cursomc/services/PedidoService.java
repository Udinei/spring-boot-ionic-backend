package com.udineisilva.cursomc.services;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.udineisilva.cursomc.domain.Categoria;
import com.udineisilva.cursomc.domain.Pedido;
import com.udineisilva.cursomc.repositories.PedidoRepository;
import com.udineisilva.cursomc.services.exceptions.ObjectNotFoundException;

@Service
public class PedidoService {
	
	@Autowired
	private PedidoRepository pedidoRepository;
		
	public Pedido find(Integer id){
		Optional<Pedido> obj = pedidoRepository.findById(id);
		
		// caso o objeto nao exista sera lancado uma exceção informando o id e o nome da classe do objeto
		return obj.orElseThrow(() -> new ObjectNotFoundException("Objeto não encontrado! id: " + id 
				+ ", tipo: " + Pedido.class.getName())); 
			
	}
	

}
