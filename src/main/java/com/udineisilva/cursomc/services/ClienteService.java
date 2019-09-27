package com.udineisilva.cursomc.services;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Service;

import com.udineisilva.cursomc.domain.Cidade;
import com.udineisilva.cursomc.domain.Cliente;
import com.udineisilva.cursomc.domain.Endereco;
import com.udineisilva.cursomc.domain.enums.TipoCliente;
import com.udineisilva.cursomc.dto.ClienteDTO;
import com.udineisilva.cursomc.dto.ClienteNewDTO;
import com.udineisilva.cursomc.repositories.ClienteRepository;
import com.udineisilva.cursomc.repositories.EnderecoRepository;
import com.udineisilva.cursomc.services.exceptions.DataIntegrityException;
import com.udineisilva.cursomc.services.exceptions.ObjectNotFoundException;

@Service
public class ClienteService {
	
	@Autowired
	private ClienteRepository clienteRepository;
	
	@Autowired
	private EnderecoRepository EnderecoRepository;
	
	public Cliente find(Integer id){
		Optional<Cliente> obj = clienteRepository.findById(id);
		
		// caso o objeto nao exista sera lancado uma exceção informando o id e o nome da classe do objeto
		return obj.orElseThrow(() -> new ObjectNotFoundException("Objeto não encontrado! id: " + id 
				+ ", tipo: " + Cliente.class.getName())); 
			
	}
	

	public Cliente update(Cliente obj) {
		// verifica se o objeto existe
		Cliente newObj = find(obj.getId());
		updateData(newObj, obj);
				
		return clienteRepository.save(newObj);
	}
    
	// metodo auxiliar de update tendo em vista, que o cnpj e o tipo não pode ser alterado
	// e objeto sera exibido na tela apos a alteração, evitando assim que cpnpj e tipo 
	// sejam exibidos em branco
	private void updateData(Cliente newObj, Cliente obj) {
		newObj.setNome(obj.getNome());
		newObj.setEmail(obj.getEmail());
	}


	public void delete(Integer id) {
		// verifica se o objeto existe
		find(id);
		
		try {
			clienteRepository.deleteById(id);
			
		} catch (DataIntegrityViolationException e) {
			throw new DataIntegrityException("Não é possivel excluir um cliente porque que há entidades relacionadas");

		}
	}

	public List<Cliente> findAll() {
	   return clienteRepository.findAll();
	}
	
	public Page<Cliente> findPage(Integer page, Integer linesPerPage, String orderBy, String direction){
		PageRequest pageRequest = PageRequest.of(page, linesPerPage, Direction.valueOf(direction), orderBy);
		return clienteRepository.findAll(pageRequest);
	}
	
	// converte um objeto ClienteDTO para Cliente
	public Cliente fromDTO(ClienteDTO objDto){
		return new Cliente(objDto.getId(), objDto.getNome(), objDto.getEmail(), null, null);
	}
	
	// converte um objeto ClienteNewDTO para Cliente 
	public Cliente fromDTO(ClienteNewDTO objDto){
		Cliente cli = new Cliente(null, objDto.getNome(), objDto.getEmail(), objDto.getCpfOuCnpj(), TipoCliente.toEnum(objDto.getTipo()));
		Cidade cid = new Cidade(objDto.getCidadeId(), null, null);
		Endereco end = new Endereco(null, objDto.getLogradouro(), objDto.getNumero(), objDto.getComplemento(), objDto.getBairro(), objDto.getCep(), cli, cid);
		cli.getEnderecos().add(end);
		cli.getTelefones().add(objDto.getTelefone1());
		if(objDto.getTelefone2() != null){
			cli.getTelefones().add(objDto.getTelefone2());
		}
		if(objDto.getTelefone3() != null){
			cli.getTelefones().add(objDto.getTelefone3());
		}
		
		return cli;
	}
	
	


	public Cliente insert(Cliente obj) {
		obj.setId(null);
		obj = clienteRepository.save(obj);
		EnderecoRepository.saveAll(obj.getEnderecos());
		return obj;
	}

}
