package br.com.ecommerce.vendas.service;

import java.util.List;
import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import br.com.ecommerce.infra.service.MessagesService;
import br.com.ecommerce.vendas.dao.ClienteDao;
import br.com.ecommerce.vendas.model.Cliente;

@Service
@Transactional
public class ClienteService {
	
	public static final String NAO_ENCONTRADO = "nao.encontrado";
	
	@Autowired
	ClienteDao clienteDao;
	
	@Autowired
	MessagesService messagesService;
	
	public List<Cliente> getClientes(){
		return clienteDao.findAll();
	}
	
	public Cliente getCliente(Integer id) {
		Optional<Cliente> cliente = clienteDao.findById(id);
		
		if(!cliente.isPresent()) {
			throw new RuntimeException(messagesService.get(NAO_ENCONTRADO));
		}
		
		return cliente.get();
	}
	
	public Cliente salvarCliente(Cliente cliente) {
		return clienteDao.save(cliente);
	}
	
	public Cliente alterarCliente(Cliente cliente) {
		Optional<Cliente> l = clienteDao.findById(cliente.getId());

		if(!l.isPresent()) {
			throw new RuntimeException(messagesService.get(NAO_ENCONTRADO));
		}
		
		return salvarCliente(cliente);
	}
	
	public void excluiCliente(Integer id) {
		Optional<Cliente> cliente = clienteDao.findById(id);

		if(!cliente.isPresent()) {
			throw new RuntimeException(messagesService.get(NAO_ENCONTRADO));
		}
		
		clienteDao.delete(cliente.get());
	}

	public Page<Cliente> getClientes(Pageable pageable) {
		return clienteDao.findAll(pageable);
	}

}
