package com.udineisilva.cursomc.services;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Service;

import com.udineisilva.cursomc.domain.Categoria;
import com.udineisilva.cursomc.domain.Produto;
import com.udineisilva.cursomc.repositories.CategoriaRepository;
import com.udineisilva.cursomc.repositories.ProdutoRepository;
import com.udineisilva.cursomc.services.exceptions.ObjectNotFoundException;

@Service
public class ProdutoService {
	
	@Autowired
	private ProdutoRepository produtoRepository;
	
	@Autowired
	private CategoriaRepository categoriaRepository;
		
	public Produto find(Integer id){
		Optional<Produto> obj = produtoRepository.findById(id);
		
		// caso o objeto nao exista sera lancado uma exceção informando o id e o nome da classe do objeto
		return obj.orElseThrow(() -> new ObjectNotFoundException("Objeto não encontrado! id: " + id 
				+ ", tipo: " + Produto.class.getName())); 
			
	}
	
	public Page<Produto> search(String nome, List<Integer> ids,Integer page, Integer linesPerPage, String orderBy, String direction){
		PageRequest pageRequest = PageRequest.of(page, linesPerPage, Direction.valueOf(direction), orderBy);
		
		List<Categoria> categorias = categoriaRepository.findAllById(ids);
				
		return produtoRepository.findDistinctByNomeContainingAndCategorias(nome, categorias, pageRequest);
		
		// usando a consulta anterior
		//return produtoRepository.search(nome, categorias, pageRequest);
		
	}

}
