package br.com.ecommerce.vendas.controller;

import java.net.URI;
import java.util.List;

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
import br.com.ecommerce.vendas.model.Fornecedor;
import br.com.ecommerce.vendas.service.FornecedorService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@RestController
@RequestMapping("/api/v1/fornecedor")
@Api(value = "Fornecedor")
public class FornecedorRestController {


	@Autowired
	FornecedorService fornecedorService;	
	
	@Autowired
	private MessagesService messages;
	
	public static final String SALVO_COM_SUCESSO = "salvo.sucesso";
	public static final String EXCLUIDO = "registro.excluido";
	
	@ApiOperation(value = "Detalha um fornecedor pelo ID", notes = "Um ID válido deve ser informado", response = Fornecedor.class)
	@GetMapping("/{id}")
	public ResponseEntity<ServiceResponse<Fornecedor>> getFornecedor(@PathVariable Integer id) {
		return ResponseEntity.ok(new ServiceResponse<>(fornecedorService.getFornecedor(id)));
	}
	
	@GetMapping("/fornecedorPaginado")
	@ApiOperation(value = "Lista de fornecedores paginados", response = Fornecedor.class)
	public ServiceResponse<Page<Fornecedor>> getFornecedores(Pageable pageable) {
		return new ServiceResponse<>(fornecedorService.getFornecedores(pageable));
	}
	
	@GetMapping
	@ApiOperation(value = "Lista de fornecedores", response = Fornecedor.class)
	public ServiceResponse<List<Fornecedor>> getFornecedoresSemPaginacao( ) {
		return new ServiceResponse<>(fornecedorService.getFornecedores());
	}
	
	@PostMapping
	@ApiOperation(value = "Cria um fornecedor", response = Fornecedor.class)
	public ResponseEntity<ServiceResponse<Fornecedor>> criaFornecedor(@RequestBody @Valid Fornecedor fornecedor) {
		fornecedor = fornecedorService.salvarFornecedor(fornecedor);
		HttpHeaders headers = new HttpHeaders();
		URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(fornecedor.getId())
				.toUri();
		headers.setLocation(location);
		ServiceMessage message = new ServiceMessage(messages.get(SALVO_COM_SUCESSO));
		
		return new ResponseEntity<>(new ServiceResponse<>(fornecedor, message), headers, HttpStatus.CREATED);
	}
	
	@PutMapping("/{id}")
	@ApiOperation(value = "Altera um fornecedor", response = Fornecedor.class)
	public ResponseEntity<ServiceResponse<Fornecedor>> updateFornecedor(@PathVariable Integer id,
			@RequestBody @Valid Fornecedor fornecedor) {
		
		ServiceMessage message = new ServiceMessage(messages.get(SALVO_COM_SUCESSO));
		return new ResponseEntity<>(new ServiceResponse<>(fornecedorService.alterarFornecedor(fornecedor), message), HttpStatus.OK);

	}
	

	@DeleteMapping("/{id}")
	@ApiOperation(value = "Ativa/Desativa um fornecedor", notes = "Um ID válido deve ser informado", response = Fornecedor.class)
	public ResponseEntity<ServiceResponse<Void>> excluirFornecedor(@PathVariable Integer id) {
		fornecedorService.excluiFornecedor(id);
		ServiceMessage message = new ServiceMessage(messages.get(EXCLUIDO));
		return new ResponseEntity<>(new ServiceResponse<>(message), HttpStatus.OK);
	}
}
