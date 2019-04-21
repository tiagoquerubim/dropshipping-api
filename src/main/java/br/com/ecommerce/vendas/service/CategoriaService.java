package br.com.ecommerce.vendas.service;

import java.util.List;
import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import br.com.ecommerce.infra.service.MessagesService;
import br.com.ecommerce.vendas.dao.CategoriaDao;
import br.com.ecommerce.vendas.model.Categoria;

@Service
@Transactional
public class CategoriaService {
	
	public static final String NAO_ENCONTRADO = "nao.encontrado";

	@Autowired
	MessagesService messagesService;
	
	@Autowired
	CategoriaDao categoriaDao;
	
	public List<Categoria> getCategorias(){
		return categoriaDao.findAll();
		
	}
	
	public Categoria getCategoria(Integer id) {
		Optional<Categoria> categoria = categoriaDao.findById(id);
		
		if(!categoria.isPresent()) {
			throw new RuntimeException(messagesService.get(NAO_ENCONTRADO));
		}
		return categoria.get();
	}
	
	public Categoria salvarCategoria(Categoria categoria){
		return categoriaDao.save(categoria);
	}
	
	public Categoria alterarCategoria(Categoria categoria) {
		Optional<Categoria> cat = categoriaDao.findById(categoria.getId());
		
		if(!cat.isPresent()) {
			throw new RuntimeException(messagesService.get(NAO_ENCONTRADO));
		}
		
		return salvarCategoria(categoria);
	}
	
	public void excluiCategoria(Integer id) {
		Optional<Categoria> categoria = categoriaDao.findById(id);

		if(!categoria.isPresent()) {
			throw new RuntimeException(messagesService.get(NAO_ENCONTRADO));
		}
		
		categoriaDao.delete(categoria.get());
	}

	public Page<Categoria> getProdutosPaginado(Pageable pageable){
		return categoriaDao.findAll(pageable);
	}

	public Page<Categoria> filtraCategoriasPaginado(String descricao, Integer id, Pageable pageable) {
		return categoriaDao.findByLikeQuery(descricao, id, pageable);
	}


	
}
