package br.com.ecommerce.marketing.service;

import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import br.com.ecommerce.infra.service.MessagesService;
import br.com.ecommerce.marketing.dao.PropagandaDao;
import br.com.ecommerce.marketing.model.Propaganda;

@Service
@Transactional
public class PropagandaService {
	
	public static final String NAO_ENCONTRADO = "nao.encontrado";
	
	@Autowired
	PropagandaDao propagandaDao;
	
	@Autowired
	MessagesService messagesService;
	
	public Page<Propaganda> getPropagandas(Pageable pageable){
		return propagandaDao.findAll(pageable);
	}
	
	public Propaganda getPropaganda(Integer id) {
		Optional<Propaganda> propaganda = propagandaDao.findById(id);
		
		if(!propaganda.isPresent()) {
			throw new RuntimeException(messagesService.get(NAO_ENCONTRADO));
		}
		
		return propaganda.get();
	}
	
	public Propaganda salvarPropaganda(Propaganda propaganda) {
		return propagandaDao.save(propaganda);
	}
	
	public Propaganda alterarPropaganda(Propaganda propaganda) {
		Optional<Propaganda> l = propagandaDao.findById(propaganda.getId());

		if(!l.isPresent()) {
			throw new RuntimeException(messagesService.get(NAO_ENCONTRADO));
		}
		
		return salvarPropaganda(propaganda);
	}
	
	public void excluiPropaganda(Integer id) {
		Optional<Propaganda> propaganda = propagandaDao.findById(id);

		if(!propaganda.isPresent()) {
			throw new RuntimeException(messagesService.get(NAO_ENCONTRADO));
		}
		
		propagandaDao.delete(propaganda.get());
	}

}
