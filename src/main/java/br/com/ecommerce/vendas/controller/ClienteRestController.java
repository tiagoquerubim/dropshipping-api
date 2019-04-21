package br.com.ecommerce.vendas.controller;

import java.net.URI;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import br.com.ecommerce.infra.response.ServiceMessage;
import br.com.ecommerce.infra.response.ServiceResponse;
import br.com.ecommerce.infra.service.MessagesService;
import br.com.ecommerce.vendas.model.Cliente;
import br.com.ecommerce.vendas.service.ClienteService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@RestController
@RequestMapping("/api/v1/cliente")
@Api(value = "Cliente")
public class ClienteRestController {


	@Autowired
	ClienteService clienteService;	
	
	@Autowired
	private MessagesService messages;
	
	public static final String SALVO_COM_SUCESSO = "salvo.sucesso";
	public static final String EXCLUIDO = "registro.excluido";
	
	@ApiOperation(value = "Detalha um cliente pelo ID", notes = "Um ID válido deve ser informado", response = Cliente.class)
	@GetMapping("/{id}")
	public ResponseEntity<ServiceResponse<Cliente>> getCliente(@PathVariable Integer id) {
		return ResponseEntity.ok(new ServiceResponse<>(clienteService.getCliente(id)));
	}
	
	@GetMapping
	@ApiOperation(value = "Lista", response = Cliente.class)
	public ServiceResponse<Page<Cliente>> getClientes(Pageable pageable) {
		return new ServiceResponse<>(clienteService.getClientes(pageable));
	}
	
	@PostMapping
	@ApiOperation(value = "Cria um cliente", response = Cliente.class)
	public ResponseEntity<ServiceResponse<Cliente>> criaCliente(@RequestBody @Valid Cliente cliente) {
		cliente = clienteService.salvarCliente(cliente);
		HttpHeaders headers = new HttpHeaders();
		URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(cliente.getId())
				.toUri();
		headers.setLocation(location);
		ServiceMessage message = new ServiceMessage(messages.get(SALVO_COM_SUCESSO));
		
		return new ResponseEntity<>(new ServiceResponse<>(cliente, message), headers, HttpStatus.CREATED);
	}
	
	@PutMapping("/{id}")
	@ApiOperation(value = "Altera um cliente", response = Cliente.class)
	public ResponseEntity<ServiceResponse<Cliente>> updateCliente(@PathVariable Integer id,
			@RequestBody @Valid Cliente cliente) {
		
		ServiceMessage message = new ServiceMessage(messages.get(SALVO_COM_SUCESSO));
		return new ResponseEntity<>(new ServiceResponse<>(clienteService.alterarCliente(cliente), message), HttpStatus.OK);

	}
	

	@DeleteMapping("/{id}")
	@ApiOperation(value = "Ativa/Desativa um cliente", notes = "Um ID válido deve ser informado", response = Cliente.class)
	public ResponseEntity<ServiceResponse<Void>> excluiCliente(@PathVariable Integer id) {
		clienteService.excluiCliente(id);
		ServiceMessage message = new ServiceMessage(messages.get(EXCLUIDO));
		return new ResponseEntity<>(new ServiceResponse<>(message), HttpStatus.OK);
	}
}
