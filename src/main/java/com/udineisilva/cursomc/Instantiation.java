package com.udineisilva.cursomc;

import java.util.Arrays;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Configuration;

import com.udineisilva.cursomc.domain.Categoria;
import com.udineisilva.cursomc.domain.Cidade;
import com.udineisilva.cursomc.domain.Cliente;
import com.udineisilva.cursomc.domain.Endereco;
import com.udineisilva.cursomc.domain.Estado;
import com.udineisilva.cursomc.domain.Produto;
import com.udineisilva.cursomc.domain.enums.TipoCliente;
import com.udineisilva.cursomc.repositories.CategoriaRepository;
import com.udineisilva.cursomc.repositories.CidadeRepository;
import com.udineisilva.cursomc.repositories.ClienteRepository;
import com.udineisilva.cursomc.repositories.EnderecoRepository;
import com.udineisilva.cursomc.repositories.EstadoRepository;
import com.udineisilva.cursomc.repositories.ProdutoRepository;

@Configuration
public class Instantiation implements CommandLineRunner{

	
	    @Autowired	
		private CategoriaRepository categoriaRepository;
		
	    @Autowired
		private ProdutoRepository produtoRepository;
		
	    @Autowired
	    private EstadoRepository estadoRepository;
	    
	    @Autowired
	    private CidadeRepository cidadeRepository;
	    
	    @Autowired
	    private EnderecoRepository enderecoRepository;
	    
	    @Autowired
	    private ClienteRepository clienteRepository;
	    
	    
		@Override
		public void run(String... args) throws Exception {
			
			Categoria cat1 = new Categoria(null, "Informática");
			Categoria cat2 = new Categoria(null, "Escritório");
		
			Produto p1 = new Produto(null, "Computador", 2000.00);
			Produto p2 = new Produto(null, "Impressora", 2000.00);
			Produto p3 = new Produto(null, "Mouse", 2000.00);
			
			cat1.getProdutos().addAll(Arrays.asList(p1,p2,p3));
			cat2.getProdutos().addAll(Arrays.asList(p2));
			
			p1.getCategorias().addAll(Arrays.asList(cat1));
			p2.getCategorias().addAll(Arrays.asList(cat1, cat2));
			p3.getCategorias().addAll(Arrays.asList(cat1));
								
			categoriaRepository.saveAll(Arrays.asList(cat1, cat2));
			produtoRepository.saveAll(Arrays.asList(p1, p2, p3));
			
			Estado est1 = new Estado(null, "Minas Gerais");
			Estado est2 = new Estado(null, "São Paulo");
			
			Cidade c1 = new Cidade(null, "Uberlândia", est1);
			Cidade c2 = new Cidade(null, "São Paulo", est2);
			Cidade c3 = new Cidade(null, "Campinas", est2);
			
			est1.getCidades().addAll(Arrays.asList(c1));
			est2.getCidades().addAll(Arrays.asList(c2, c3));
			
			estadoRepository.saveAll(Arrays.asList(est1, est2));
			cidadeRepository.saveAll(Arrays.asList(c1, c2, c3));
			
			Cliente cli1 = new Cliente(null, "Maria Silva", "54305209197", TipoCliente.PESSOAFISICA, "maria@gmail.com");
			cli1.getTelefones().addAll(Arrays.asList("27363323", "93838393"));
			
			Endereco e1 = new Endereco(null, "Rua Flores", "300", "Apto 303","Jardim", "38208834", cli1, c1);
			Endereco e2 = new Endereco(null, "Avenida Matos", "105", "Sala 800","Centro", "38777012", cli1, c2);
			 
			cli1.getEnderecos().addAll(Arrays.asList(e1, e2));
            
			clienteRepository.saveAll (Arrays.asList(cli1));
			enderecoRepository.saveAll(Arrays.asList(e1, e2));
			
	}

}
