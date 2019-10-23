package com.udineisilva.cursomc.resources;

import java.net.URI;
import java.util.List;
import java.util.stream.Collectors;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
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

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

@RestController
@RequestMapping(value="/categorias")
public class CategoriaResource {
	
	@Autowired
	CategoriaService categoriaService;
	
	

	//@PathVariable - permite enviar um paramentro na url
	@ApiOperation(value="Busca categorias por id")
	@GetMapping(value="/{id}")
	public ResponseEntity<Categoria> find(@PathVariable Integer id){
		Categoria obj = categoriaService.find(id);	
		return ResponseEntity.ok().body(obj);
	}
	
	
	// ResponseEntity<void> - será retornado uma entidade sem corpo
	// URI - retorna a uri do novo recurso criado 
	@ApiOperation(value="Insere categoria")
	@PreAuthorize("hasAnyRole('ADMIN')")
	@PostMapping
	public ResponseEntity<Void> insert(@Valid @RequestBody CategoriaDTO objDto){
		Categoria obj = categoriaService.fromDTO(objDto);
		
		obj = categoriaService.insert(obj);
		URI uri = ServletUriComponentsBuilder.fromCurrentRequest()
				.path("/{id}").buildAndExpand(obj.getId()).toUri();
		
		// retorna o codigo http = 201 se tudo ocorrer bem.
		return ResponseEntity.created(uri).build();
	}
	
	@ApiOperation(value="Atualiza categoria por id")
	@PreAuthorize("hasAnyRole('ADMIN')")
	@PutMapping(value="/{id}")
	public ResponseEntity<Void> update(@Valid @RequestBody CategoriaDTO objDto, @PathVariable Integer id){
		Categoria obj = categoriaService.fromDTO(objDto);
		obj.setId(id);
		obj = categoriaService.update(obj);
		
		// retorna o codigo 204 se tudo ocorreu bem
		return ResponseEntity.noContent().build();
	}
	
	@ApiOperation(value="Remove categorias por id") // mensagem que sera exibida na documentacao da api na pagina do swagger
	@PreAuthorize("hasAnyRole('ADMIN')")
	@ApiResponses(value = { // mensagem que sera exibida na documentacao da api na pagina do swagger
			@ApiResponse(code = 400, message = "Não é possível excluir uma categoria que possui produtos"),
			@ApiResponse(code = 404, message = "Código inexistente") })
	@DeleteMapping(value="/{id}")
	public ResponseEntity<Void> delete(@PathVariable Integer id){
		categoriaService.delete(id);
		
		// retorna o codigo 204 se tudo ocorreu bem
		return ResponseEntity.noContent().build();
	}
	
	@ApiOperation(value="Retorna todas as categorias")
	@GetMapping
	public ResponseEntity<List<CategoriaDTO>> findAll(){
		List<Categoria> list = categoriaService.findAll();
		List<CategoriaDTO> listDto = list.stream().map(obj -> new CategoriaDTO(obj)).collect(Collectors.toList());
		return ResponseEntity.ok().body(listDto);
	}

	
	// @RequestParam - permite passar os parametros na forma ex:
	// /categorias/page?page=0&linesPerPage=20&orderBy=desc etc..
	@ApiOperation(value="Retorna todas categorias com paginação")
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
