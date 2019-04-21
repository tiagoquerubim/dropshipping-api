package br.com.ecommerce.marketing.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import br.com.ecommerce.marketing.model.Promocao;


public interface PromocaoDao extends JpaRepository<Promocao, Integer>{

}
