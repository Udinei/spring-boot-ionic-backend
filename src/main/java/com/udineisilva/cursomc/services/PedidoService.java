package com.udineisilva.cursomc.services;

import java.util.Date;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.udineisilva.cursomc.domain.ItemPedido;
import com.udineisilva.cursomc.domain.PagamentoComBoleto;
import com.udineisilva.cursomc.domain.Pedido;
import com.udineisilva.cursomc.domain.enums.EstadoPagamento;
import com.udineisilva.cursomc.repositories.ItemPedidoRepository;
import com.udineisilva.cursomc.repositories.PagamentoRepository;
import com.udineisilva.cursomc.repositories.PedidoRepository;
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
	
	

	public Pedido find(Integer id) {
		Optional<Pedido> obj = pedidoRepository.findById(id);

		// caso o objeto nao exista sera lancado uma exceção informando o id e o
		// nome da classe do objeto
		return obj.orElseThrow(() -> new ObjectNotFoundException(
				"Objeto não encontrado! id: " + id + ", tipo: " + Pedido.class.getName()));

	}

	@Transactional
	public Pedido insert(Pedido pedido) {
		pedido.setId(null);
		pedido.setInstante(new Date());
		pedido.getPagamento().setEstado(EstadoPagamento.PENDENTE);
		pedido.getPagamento().setPedido(pedido);
		
		if(pedido.getPagamento() instanceof PagamentoComBoleto){
			PagamentoComBoleto pagto = (PagamentoComBoleto) pedido.getPagamento();
			boletoService.preencherPagamentoComBoleto(pagto, pedido.getInstante());
		}
		
		pedido = pedidoRepository.save(pedido);
		pagamentoRepository.save(pedido.getPagamento());
		
		for(ItemPedido ip : pedido.getItens()){
			ip.setDesconto(0.0);
			ip.setPreco(produtoService.find(ip.getProduto().getId()).getPreco());
			ip.setPedido(pedido);
		}
		
		itemPedidoRepository.saveAll(pedido.getItens());
		
		
	return pedido;
}

}
