package br.com.ecommerce.vendas.dao;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import br.com.ecommerce.vendas.model.Categoria;

public interface CategoriaDao extends JpaRepository<Categoria, Integer>{

	@Query("select cat from Categoria cat where cat.descricao like %:descricao% and (:id is null or cat.id = :id)")
	Page<Categoria> findByLikeQuery(@Param("descricao") String descricao,@Param("id") Integer id, Pageable pageable);

}
