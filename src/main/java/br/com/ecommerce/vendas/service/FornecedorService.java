package br.com.ecommerce.vendas.service;

import java.util.List;
import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import br.com.ecommerce.infra.service.MessagesService;
import br.com.ecommerce.vendas.dao.FornecedorDao;
import br.com.ecommerce.vendas.model.Fornecedor;

@Service
@Transactional
public class FornecedorService {
	
	public static final String NAO_ENCONTRADO = "nao.encontrado";
	
	@Autowired
	FornecedorDao fornecedorDao;
	
	@Autowired
	MessagesService messagesService;
	
	public List<Fornecedor> getFornecedores(){
		return fornecedorDao.findAll();
	}
	
	public Page<Fornecedor> getFornecedores(Pageable pageable){
		return fornecedorDao.findAll(pageable);
	}
	
	public Fornecedor getFornecedor(Integer id) {
		Optional<Fornecedor> fornecedor = fornecedorDao.findById(id);
		
		if(!fornecedor.isPresent()) {
			throw new RuntimeException(messagesService.get(NAO_ENCONTRADO));
		}
		
		return fornecedor.get();
	}
	
	public Fornecedor salvarFornecedor(Fornecedor fornecedor) {
		return fornecedorDao.save(fornecedor);
	}
	
	public Fornecedor alterarFornecedor(Fornecedor fornecedor) {
		Optional<Fornecedor> l = fornecedorDao.findById(fornecedor.getId());

		if(!l.isPresent()) {
			throw new RuntimeException(messagesService.get(NAO_ENCONTRADO));
		}
		
		return salvarFornecedor(fornecedor);
	}
	
	public void excluiFornecedor(Integer id) {
		Optional<Fornecedor> fornecedor = fornecedorDao.findById(id);

		if(!fornecedor.isPresent()) {
			throw new RuntimeException(messagesService.get(NAO_ENCONTRADO));
		}
		
		fornecedorDao.delete(fornecedor.get());
	}

}
