package br.com.ecommerce.sac.service;

import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import br.com.ecommerce.infra.service.MessagesService;
import br.com.ecommerce.sac.dao.SolicitacaoDao;
import br.com.ecommerce.sac.model.Solicitacao;

@Service
@Transactional
public class SolicitacaoService {
	
	public static final String NAO_ENCONTRADO = "nao.encontrado";
	
	@Autowired
	SolicitacaoDao solicitacaoDao;
	
	@Autowired
	MessagesService messagesService;
	
	public Page<Solicitacao> getSolicitacaos(Pageable pageable){
		return solicitacaoDao.findAll(pageable);
	}
	
	public Solicitacao getSolicitacao(Integer id) {
		Optional<Solicitacao> solicitacao = solicitacaoDao.findById(id);
		
		if(!solicitacao.isPresent()) {
			throw new RuntimeException(messagesService.get(NAO_ENCONTRADO));
		}
		
		return solicitacao.get();
	}
	
	public Solicitacao salvarSolicitacao(Solicitacao solicitacao) {
		return solicitacaoDao.save(solicitacao);
	}
	
	public Solicitacao alterarSolicitacao(Solicitacao solicitacao) {
		Optional<Solicitacao> l = solicitacaoDao.findById(solicitacao.getId());

		if(!l.isPresent()) {
			throw new RuntimeException(messagesService.get(NAO_ENCONTRADO));
		}
		
		return salvarSolicitacao(solicitacao);
	}
	
	public void excluiSolicitacao(Integer id) {
		Optional<Solicitacao> solicitacao = solicitacaoDao.findById(id);

		if(!solicitacao.isPresent()) {
			throw new RuntimeException(messagesService.get(NAO_ENCONTRADO));
		}
		
		solicitacaoDao.delete(solicitacao.get());
	}

}
