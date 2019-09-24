package com.udineisilva.cursomc.services;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.udineisilva.cursomc.domain.Categoria;
import com.udineisilva.cursomc.repositories.CategoriaRepository;
import com.udineisilva.cursomc.services.exceptions.ObjectNotFoundException;

@Service
public class CategoriaService {
	
	@Autowired
	private CategoriaRepository categoriaRepository;
	
	public Categoria find(Integer id){
		Optional<Categoria> obj = categoriaRepository.findById(id);
		
		// caso o objeto nao exista sera lancado uma exceção informando o id e o nome da classe do objeto
		return obj.orElseThrow(() -> new ObjectNotFoundException("Objeto não encontrado! id: " + id 
				+ ", tipo: " + Categoria.class.getName())); 
			
	}

	public Categoria insert(Categoria obj) {
         obj.setId(null);
         return categoriaRepository.save(obj);
		
	}
	

}
