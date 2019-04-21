package br.com.ecommerce.vendas.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import br.com.ecommerce.vendas.model.Venda;


public interface VendaDao extends JpaRepository<Venda, Integer>{

}
