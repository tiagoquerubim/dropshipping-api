package br.com.ecommerce.vendas.service;

import java.util.List;
import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import br.com.ecommerce.infra.service.MessagesService;
import br.com.ecommerce.vendas.dao.ProdutoDao;
import br.com.ecommerce.vendas.model.Produto;

@Service
@Transactional
public class ProdutoService {
	
	public static final String NAO_ENCONTRADO = "nao.encontrado";
	
	@Autowired
	ProdutoDao produtoDao;
	
	@Autowired
	MessagesService messagesService;
	
	public List<Produto> getProdutos(){
		return produtoDao.findAll();
	}
	
	public Produto getProduto(Integer id) {
		Optional<Produto> produto = produtoDao.findById(id);
		
		if(!produto.isPresent()) {
			throw new RuntimeException(messagesService.get(NAO_ENCONTRADO));
		}
		
		return produto.get();
	}
	
	public Produto salvarProduto(Produto produto) {
		return produtoDao.save(produto);
	}
	
	public Produto alterarProduto(Produto produto) {
		Optional<Produto> l = produtoDao.findById(produto.getId());

		if(!l.isPresent()) {
			throw new RuntimeException(messagesService.get(NAO_ENCONTRADO));
		}
		
		return salvarProduto(produto);
	}
	
	public void excluiProduto(Integer id) {
		Optional<Produto> produto = produtoDao.findById(id);

		if(!produto.isPresent()) {
			throw new RuntimeException(messagesService.get(NAO_ENCONTRADO));
		}
		
		produtoDao.delete(produto.get());
	}
	
	public Page<Produto> getProdutos(Pageable pageable) {
		return produtoDao.findAll(pageable);
	}

}
