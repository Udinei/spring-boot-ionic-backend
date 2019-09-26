package com.udineisilva.cursomc.resources;

import java.net.URI;
import java.util.List;
import java.util.stream.Collectors;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.udineisilva.cursomc.domain.Categoria;
import com.udineisilva.cursomc.dto.CategoriaDTO;
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
	public ResponseEntity<Void> insert(@Valid @RequestBody CategoriaDTO objDto){
		Categoria obj = categoriaService.fromDTO(objDto);
		
		obj = categoriaService.insert(obj);
		URI uri = ServletUriComponentsBuilder.fromCurrentRequest()
				.path("/{id}").buildAndExpand(obj.getId()).toUri();
		
		// retorna o codigo http = 201 se tudo ocorrer bem.
		return ResponseEntity.created(uri).build();
	}
	
	
	@PutMapping(value="/{id}")
	public ResponseEntity<Void> update(@Valid @RequestBody CategoriaDTO objDto, @PathVariable Integer id){
		Categoria obj = categoriaService.fromDTO(objDto);
		obj.setId(id);
		obj = categoriaService.update(obj);
		
		// retorna o codigo 204 se tudo ocorreu bem
		return ResponseEntity.noContent().build();
	}
	
	@DeleteMapping(value="/{id}")
	public ResponseEntity<Void> delete(@PathVariable Integer id){
		categoriaService.delete(id);
		
		// retorna o codigo 204 se tudo ocorreu bem
		return ResponseEntity.noContent().build();
	}
	
	@GetMapping
	public ResponseEntity<List<CategoriaDTO>> findAll(){
		List<Categoria> list = categoriaService.findAll();
		List<CategoriaDTO> listDto = list.stream().map(obj -> new CategoriaDTO(obj)).collect(Collectors.toList());
		return ResponseEntity.ok().body(listDto);
	}

	
	// @RequestParam - permite passar os parametros na forma ex:
	// /categorias/page?page=0&linesPerPage=20&orderBy=desc etc..
	@GetMapping(value="/page")
	public ResponseEntity<Page<CategoriaDTO>> findPage(
			@RequestParam(value="page", defaultValue="0") Integer page,
			@RequestParam(value="linesPerPage", defaultValue="24") Integer linesPerPage,
			@RequestParam(value="orderBy", defaultValue="nome") String orderBy,
			@RequestParam(value="direction", defaultValue="ASC") String direction){
		
		Page<Categoria> list = categoriaService.findPage(page, linesPerPage, orderBy, direction);
		Page<CategoriaDTO> listDto = list.map(obj -> new CategoriaDTO(obj));
		return ResponseEntity.ok().body(listDto);
		
	}
}
