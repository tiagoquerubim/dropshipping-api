package br.com.ecommerce.vendas.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import br.com.ecommerce.vendas.model.Fornecedor;


public interface FornecedorDao extends JpaRepository<Fornecedor, Integer>{

}
