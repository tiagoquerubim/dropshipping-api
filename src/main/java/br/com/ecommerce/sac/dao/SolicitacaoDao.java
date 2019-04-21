package br.com.ecommerce.sac.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import br.com.ecommerce.sac.model.Solicitacao;


public interface SolicitacaoDao extends JpaRepository<Solicitacao, Integer>{

}
