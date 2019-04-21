package br.com.ecommerce.sac.service;

import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import br.com.ecommerce.infra.service.MessagesService;
import br.com.ecommerce.sac.dao.DuvidaDao;
import br.com.ecommerce.sac.model.Duvida;

@Service
@Transactional
public class DuvidaService {
	
	public static final String NAO_ENCONTRADO = "nao.encontrado";
	
	@Autowired
	DuvidaDao duvidaDao;
	
	@Autowired
	MessagesService messagesService;
	
	public Page<Duvida> getDuvidas(Pageable pageable){
		return duvidaDao.findAll(pageable);
	}
	
	public Duvida getDuvida(Integer id) {
		Optional<Duvida> duvida = duvidaDao.findById(id);
		
		if(!duvida.isPresent()) {
			throw new RuntimeException(messagesService.get(NAO_ENCONTRADO));
		}
		
		return duvida.get();
	}
	
	public Duvida salvarDuvida(Duvida duvida) {
		return duvidaDao.save(duvida);
	}
	
	public Duvida alterarDuvida(Duvida duvida) {
		Optional<Duvida> l = duvidaDao.findById(duvida.getId());

		if(!l.isPresent()) {
			throw new RuntimeException(messagesService.get(NAO_ENCONTRADO));
		}
		
		return salvarDuvida(duvida);
	}
	
	public void excluiDuvida(Integer id) {
		Optional<Duvida> duvida = duvidaDao.findById(id);

		if(!duvida.isPresent()) {
			throw new RuntimeException(messagesService.get(NAO_ENCONTRADO));
		}
		
		duvidaDao.delete(duvida.get());
	}

}
