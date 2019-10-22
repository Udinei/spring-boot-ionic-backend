package com.udineisilva.cursomc.services;

import java.util.Date;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.udineisilva.cursomc.domain.Cliente;
import com.udineisilva.cursomc.domain.ItemPedido;
import com.udineisilva.cursomc.domain.PagamentoComBoleto;
import com.udineisilva.cursomc.domain.Pedido;
import com.udineisilva.cursomc.domain.enums.EstadoPagamento;
import com.udineisilva.cursomc.repositories.ItemPedidoRepository;
import com.udineisilva.cursomc.repositories.PagamentoRepository;
import com.udineisilva.cursomc.repositories.PedidoRepository;
import com.udineisilva.cursomc.security.UserSS;
import com.udineisilva.cursomc.services.exceptions.AuthorizationException;
import com.udineisilva.cursomc.services.exceptions.ObjectNotFoundException;

@Service
public class PedidoService {

		
	@Autowired
	private PedidoRepository pedidoRepository;

	@Autowired
	private BoletoService boletoService;

	@Autowired
	private PagamentoRepository pagamentoRepository;
	
	@Autowired
	private ProdutoService produtoService;
	
	@Autowired
	private ItemPedidoRepository itemPedidoRepository;
	
	@Autowired
	private ClienteService clienteService;
	
	// EmailService é uma interface, que também sera instanciada durante os testes como um Bean na classe TestConfig para MockEmailService */  
	@Autowired
	private EmailService emailService;

	public Pedido find(Integer id) {
		Optional<Pedido> obj = pedidoRepository.findById(id);

		// caso o objeto nao exista sera lancado uma exceção informando o id e o
		// nome da classe do objeto
		return obj.orElseThrow(() -> new ObjectNotFoundException(
				"Objeto não encontrado! id: " + id + ", tipo: " + Pedido.class.getName()));

	}

	@Transactional
	public Pedido insert(Pedido obj) {
		obj.setId(null);
		obj.setInstante(new Date());
		obj.setCliente(clienteService.find(obj.getCliente().getId()));
		obj.getPagamento().setEstado(EstadoPagamento.PENDENTE);
		obj.getPagamento().setPedido(obj);
		
		if(obj.getPagamento() instanceof PagamentoComBoleto){
			PagamentoComBoleto pagto = (PagamentoComBoleto) obj.getPagamento();
			boletoService.preencherPagamentoComBoleto(pagto, obj.getInstante());
		}
		
		obj = pedidoRepository.save(obj);
		pagamentoRepository.save(obj.getPagamento());
		
		for(ItemPedido ip : obj.getItens()){
			ip.setDesconto(0.0);
			ip.setProduto(produtoService.find(ip.getProduto().getId()));
			ip.setPreco(ip.getProduto().getPreco());
			ip.setPedido(obj);
		}
		
		itemPedidoRepository.saveAll(obj.getItens());
		emailService.sendOrderConfirmationHtmlEmail(obj);
				
		return obj;
	}
	
	public Page<Pedido> findPage(Integer page, Integer linesPerPage, String orderBy, String direction){

		//verifica se o usuario esta autenticado
		UserSS user = UserService.authenticated();
		if(user == null){
		   throw new AuthorizationException("Acesso negado!");	
		}
		
		PageRequest pageRequest = PageRequest.of(page, linesPerPage, Direction.valueOf(direction), orderBy);
		Cliente cliente = clienteService.find(user.getId());
		return pedidoRepository.findByCliente(cliente, pageRequest);
	}

}
