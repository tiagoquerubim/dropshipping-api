package br.com.ecommerce.vendas.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import br.com.ecommerce.vendas.model.Produto;


public interface ProdutoDao extends JpaRepository<Produto, Integer>{

}
