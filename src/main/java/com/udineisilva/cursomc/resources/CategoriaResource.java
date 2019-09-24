package com.udineisilva.cursomc.resources;

import java.net.URI;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.udineisilva.cursomc.domain.Categoria;
import com.udineisilva.cursomc.services.CategoriaService;

@RestController
@RequestMapping(value="/categorias")
public class CategoriaResource {
	
	@Autowired
	CategoriaService categoriaService;
	
	

	//@PathVariable - permite enviar um paramentro na url
	@GetMapping(value="/{id}")
	public ResponseEntity<Categoria> find(@PathVariable Integer id){
		Categoria obj = categoriaService.find(id);	
		return ResponseEntity.ok().body(obj);
	}
	
	
	// ResponseEntity<void> - será retornado uma entidade sem corpo
	// URI - retorna a uri do novo recurso criado 
	@PostMapping
	public ResponseEntity<Void> insert(@RequestBody Categoria obj){
		obj = categoriaService.insert(obj);
		URI uri = ServletUriComponentsBuilder.fromCurrentRequest()
				.path("/{id}").buildAndExpand(obj.getId()).toUri();
		
		// retorna o codigo http = 201 se tudo ocorrer bem.
		return ResponseEntity.created(uri).build();
	}
	
	@PutMapping(value="/{id}")
	public ResponseEntity<Void> update(@RequestBody Categoria obj, @PathVariable Integer id){
		obj.setId(id);
		obj = categoriaService.update(obj);
		
		// retorna o codigo 204 se tudo ocorreu bem
		return ResponseEntity.noContent().build();
	}
	
	
}
