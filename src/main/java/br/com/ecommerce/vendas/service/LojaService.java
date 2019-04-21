package br.com.ecommerce.vendas.service;

import java.util.List;
import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import br.com.ecommerce.infra.service.MessagesService;
import br.com.ecommerce.vendas.dao.LojaDao;
import br.com.ecommerce.vendas.model.Loja;

@Service
@Transactional
public class LojaService {
	
	public static final String NAO_ENCONTRADO = "nao.encontrado";
	
	@Autowired
	LojaDao lojaDao;
	
	@Autowired
	MessagesService messagesService;
	
	public Page<Loja> getLojas(Pageable pageable){
		return lojaDao.findAll(pageable);
	}
	
	public List<Loja> getLojas(){
		return lojaDao.findAll();
	}
	
	public Loja getLoja(Integer id) {
		Optional<Loja> loja = lojaDao.findById(id);
		
		if(!loja.isPresent()) {
			throw new RuntimeException(messagesService.get(NAO_ENCONTRADO));
		}
		
		return loja.get();
	}
	
	public Loja salvarLoja(Loja loja) {
		return lojaDao.save(loja);
	}
	
	public Loja alterarLoja(Loja loja) {
		Optional<Loja> l = lojaDao.findById(loja.getId());

		if(!l.isPresent()) {
			throw new RuntimeException(messagesService.get(NAO_ENCONTRADO));
		}
		
		return salvarLoja(loja);
	}
	
	public void excluiLoja(Integer id) {
		Optional<Loja> loja = lojaDao.findById(id);

		if(!loja.isPresent()) {
			throw new RuntimeException(messagesService.get(NAO_ENCONTRADO));
		}
		
		lojaDao.delete(loja.get());
	}

}
