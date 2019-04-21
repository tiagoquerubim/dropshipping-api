package br.com.ecommerce.vendas.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import br.com.ecommerce.vendas.model.Cliente;


public interface ClienteDao extends JpaRepository<Cliente, Integer>{

}
