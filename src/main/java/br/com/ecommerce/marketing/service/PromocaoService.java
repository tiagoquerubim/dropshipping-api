package br.com.ecommerce.marketing.service;

import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import br.com.ecommerce.infra.service.MessagesService;
import br.com.ecommerce.marketing.dao.PromocaoDao;
import br.com.ecommerce.marketing.model.Promocao;

@Service
@Transactional
public class PromocaoService {
	
	public static final String NAO_ENCONTRADO = "nao.encontrado";
	
	@Autowired
	PromocaoDao promocaoDao;
	
	@Autowired
	MessagesService messagesService;
	
	public Page<Promocao> getPromocaos(Pageable pageable){
		return promocaoDao.findAll(pageable);
	}
	
	public Promocao getPromocao(Integer id) {
		Optional<Promocao> promocao = promocaoDao.findById(id);
		
		if(!promocao.isPresent()) {
			throw new RuntimeException(messagesService.get(NAO_ENCONTRADO));
		}
		
		return promocao.get();
	}
	
	public Promocao salvarPromocao(Promocao promocao) {
		return promocaoDao.save(promocao);
	}
	
	public Promocao alterarPromocao(Promocao promocao) {
		Optional<Promocao> l = promocaoDao.findById(promocao.getId());

		if(!l.isPresent()) {
			throw new RuntimeException(messagesService.get(NAO_ENCONTRADO));
		}
		
		return salvarPromocao(promocao);
	}
	
	public void excluiPromocao(Integer id) {
		Optional<Promocao> promocao = promocaoDao.findById(id);

		if(!promocao.isPresent()) {
			throw new RuntimeException(messagesService.get(NAO_ENCONTRADO));
		}
		
		promocaoDao.delete(promocao.get());
	}

}
