package br.com.ecommerce.vendas.service;

import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.ecommerce.infra.service.MessagesService;
import br.com.ecommerce.vendas.dao.VendaDao;
import br.com.ecommerce.vendas.model.Venda;

@Service
@Transactional
public class VendaService {
	
	public static final String NAO_ENCONTRADO = "nao.encontrado";
	
	@Autowired
	private VendaDao vendaDao;
	
	@Autowired
	MessagesService messagesService;
	
	
	public Venda getVenda(Integer id) {
		Optional<Venda> venda = vendaDao.findById(id);
		
		if(!venda.isPresent()) {
			throw new RuntimeException(messagesService.get(NAO_ENCONTRADO));
		}
		
		return venda.get();
	}
	
	public Venda salvarVenda(Venda venda) {
		return vendaDao.save(venda);
	}
	
}
