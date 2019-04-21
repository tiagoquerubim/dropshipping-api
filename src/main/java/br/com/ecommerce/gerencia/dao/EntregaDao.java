package br.com.ecommerce.gerencia.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import br.com.ecommerce.gerencia.model.Entrega;


public interface EntregaDao extends JpaRepository<Entrega, Integer>{

}
