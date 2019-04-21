package br.com.ecommerce.vendas.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import br.com.ecommerce.vendas.model.Loja;


public interface LojaDao extends JpaRepository<Loja, Integer>{

}
