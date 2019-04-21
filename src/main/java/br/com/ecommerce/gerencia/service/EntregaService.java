package br.com.ecommerce.gerencia.service;

import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import br.com.ecommerce.gerencia.dao.EntregaDao;
import br.com.ecommerce.gerencia.model.Entrega;
import br.com.ecommerce.infra.service.MessagesService;

@Service
@Transactional
public class EntregaService {
	
	public static final String NAO_ENCONTRADO = "nao.encontrado";
	
	@Autowired
	EntregaDao entregaDao;
	
	@Autowired
	MessagesService messagesService;
	
	public Page<Entrega> getEntregas(Pageable pageable){
		return entregaDao.findAll(pageable);
	}
	
	public Entrega getEntrega(Integer id) {
		Optional<Entrega> entrega = entregaDao.findById(id);
		
		if(!entrega.isPresent()) {
			throw new RuntimeException(messagesService.get(NAO_ENCONTRADO));
		}
		
		return entrega.get();
	}
	
	public Entrega salvarEntrega(Entrega entrega) {
		return entregaDao.save(entrega);
	}
	
	public Entrega alterarEntrega(Entrega entrega) {
		Optional<Entrega> l = entregaDao.findById(entrega.getId());

		if(!l.isPresent()) {
			throw new RuntimeException(messagesService.get(NAO_ENCONTRADO));
		}
		
		return salvarEntrega(entrega);
	}
	
	public void excluiEntrega(Integer id) {
		Optional<Entrega> entrega = entregaDao.findById(id);

		if(!entrega.isPresent()) {
			throw new RuntimeException(messagesService.get(NAO_ENCONTRADO));
		}
		
		entregaDao.delete(entrega.get());
	}

}
