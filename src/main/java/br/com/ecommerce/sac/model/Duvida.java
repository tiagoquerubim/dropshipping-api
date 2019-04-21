package br.com.ecommerce.sac.model;

import java.time.LocalDateTime;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import br.com.ecommerce.vendas.model.Cliente;
import br.com.ecommerce.vendas.model.Loja;
import br.com.ecommerce.vendas.model.Produto;

@Entity
@Table(name = "duvida")
public class Duvida {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	
	@ManyToOne
	private Cliente cliente;
	
	@ManyToOne
	private Loja loja;
	
	private LocalDateTime dataCriacao;
	
	private LocalDateTime dataAtualizacao;

	@ManyToOne
	private Produto produto;

	private Boolean ativo;
	
	private StatusDuvida statusDuvida;
	
	public LocalDateTime getDataAtualizacao() {
		return dataAtualizacao;
	}

	public void setDataAtualizacao(LocalDateTime dataAtualizacao) {
		this.dataAtualizacao = dataAtualizacao;
	}

	public LocalDateTime getDataCriacao() {
		return dataCriacao;
	}

	public void setDataCriacao(LocalDateTime dataCriacao) {
		this.dataCriacao = dataCriacao;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Cliente getCliente() {
		return cliente;
	}

	public void setCliente(Cliente cliente) {
		this.cliente = cliente;
	}

	public Loja getLoja() {
		return loja;
	}

	public void setLoja(Loja loja) {
		this.loja = loja;
	}

	public Boolean getAtivo() {
		return ativo;
	}

	public void setAtivo(Boolean ativo) {
		this.ativo = ativo;
	}
	
	public Produto getProduto() {
		return produto;
	}

	public void setProduto(Produto produto) {
		this.produto = produto;
	}

	public StatusDuvida getStatusDuvida() {
		return statusDuvida;
	}

	public void setStatusDuvida(StatusDuvida statusDuvida) {
		this.statusDuvida = statusDuvida;
	}

}



