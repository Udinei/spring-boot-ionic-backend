package com.udineisilva.cursomc.services;

import java.awt.image.BufferedImage;
import java.net.URI;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.udineisilva.cursomc.domain.Cidade;
import com.udineisilva.cursomc.domain.Cliente;
import com.udineisilva.cursomc.domain.Endereco;
import com.udineisilva.cursomc.domain.enums.Perfil;
import com.udineisilva.cursomc.domain.enums.TipoCliente;
import com.udineisilva.cursomc.dto.ClienteDTO;
import com.udineisilva.cursomc.dto.ClienteNewDTO;
import com.udineisilva.cursomc.repositories.ClienteRepository;
import com.udineisilva.cursomc.repositories.EnderecoRepository;
import com.udineisilva.cursomc.security.UserSS;
import com.udineisilva.cursomc.services.exceptions.AuthorizationException;
import com.udineisilva.cursomc.services.exceptions.DataIntegrityException;
import com.udineisilva.cursomc.services.exceptions.ObjectNotFoundException;

@Service
public class ClienteService {
	
	@Autowired
	private ClienteRepository clienteRepository;
	
	@Autowired
	private EnderecoRepository EnderecoRepository;
	
	@Autowired
	private BCryptPasswordEncoder bCryptPassword;
	
	@Autowired
	private S3Service s3Service;
	
	@Autowired
	private ImageService imageService;
	
	@Value("${img.prefix.client.profile}")
	private String prefix;
	
	@Value("${img.profile.size}")
	private Integer size;
	
	
	public Cliente find(Integer id){
		UserSS user = UserService.authenticated();
		if(user == null || !user.hasRole(Perfil.ADMIN) && !id.equals(user.getId())){
			throw new AuthorizationException("Acesso negado");
		}
		
		
		Optional<Cliente> obj = clienteRepository.findById(id);
		
		// caso o objeto nao exista sera lancado uma exceção informando o id e o nome da classe do objeto
		return obj.orElseThrow(() -> new ObjectNotFoundException("Objeto não encontrado! id: " + id 
				+ ", tipo: " + Cliente.class.getName())); 
			
	}
	
	@Transactional
	public Cliente insert(Cliente obj) {
		obj.setId(null);
		obj = clienteRepository.save(obj);
		EnderecoRepository.saveAll(obj.getEnderecos());
		return obj;
	}

	public Cliente update(Cliente obj) {
		// verifica se o objeto existe
		Cliente newObj = find(obj.getId());
		updateData(newObj, obj);
				
		return clienteRepository.save(newObj);
	}
    

	public void delete(Integer id) {
		// verifica se o objeto existe
		find(id);
		
		try {
			clienteRepository.deleteById(id);
			
		} catch (DataIntegrityViolationException e) {
			throw new DataIntegrityException("Não é possivel excluir um cliente porque que há pedidos relacionados");

		}
	}

	public List<Cliente> findAll() {
	   return clienteRepository.findAll();
	}
	
	// obtem cliente por email
	public Cliente findByEmail(String email) {
		UserSS user = UserService.authenticated();
		if (user == null || !user.hasRole(Perfil.ADMIN) && !email.equals(user.getUsername())) {
			throw new AuthorizationException("Acesso negado");
		}

		Cliente obj = clienteRepository.findByEmail(email);
		if (obj == null) {
			throw new ObjectNotFoundException(
					"Objeto não encontrado! Id: " + user.getId() + ", Tipo: " + Cliente.class.getName());
		}
		return obj;
	}
	
	public Page<Cliente> findPage(Integer page, Integer linesPerPage, String orderBy, String direction){
		PageRequest pageRequest = PageRequest.of(page, linesPerPage, Direction.valueOf(direction), orderBy);
		return clienteRepository.findAll(pageRequest);
	}
	
	// converte um objeto ClienteDTO para Cliente
	public Cliente fromDTO(ClienteDTO objDto){
		return new Cliente(objDto.getId(), objDto.getNome(), objDto.getEmail(), null, null, null);
	}
	
	// converte um objeto ClienteNewDTO para Cliente 
	public Cliente fromDTO(ClienteNewDTO objDto){
		Cliente cli = new Cliente(null, objDto.getNome(), objDto.getEmail(), objDto.getCpfOuCnpj(), TipoCliente.toEnum(objDto.getTipo()), bCryptPassword.encode(objDto.getSenha()));
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

	// metodo auxiliar de update tendo em vista, que o cnpj e o tipo não pode ser alterado
	// e objeto sera exibido na tela apos a alteração, evitando assim que cpnpj e tipo 
	// sejam exibidos em branco
	private void updateData(Cliente newObj, Cliente obj) {
		newObj.setNome(obj.getNome());
		newObj.setEmail(obj.getEmail());
	}
	
	
	// faz o upload do profile do cliente para a amazon S3 
	public URI uploadProfilePicture(MultipartFile multipartFile){
		UserSS user = UserService.authenticated();
		
		if(user == null){
			throw new AuthorizationException("Acesso negado");
		}
		
		BufferedImage jpgImage = imageService.getJpgImageFromFile(multipartFile);
		
		// cortando e redimensionando a imagem
		jpgImage = imageService.cropSquare(jpgImage);
		jpgImage = imageService.resize(jpgImage, size);
		
		String fileName = prefix + user.getId() + ".jpg";
				
		return s3Service.uploadFile(imageService.getInputStream(jpgImage, "jpg"), fileName, "image");
	}
}
