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
import org.springframework.web.bind.annotation.CrossOrigin;
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
import br.com.ecommerce.vendas.model.Loja;
import br.com.ecommerce.vendas.service.LojaService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@RestController
@CrossOrigin("*")
@RequestMapping("/api/v1/loja")
@Api(value = "Loja")
public class LojaRestController {


	@Autowired
	LojaService lojaService;	
	
	@Autowired
	private MessagesService messages;
	
	public static final String SALVO_COM_SUCESSO = "salvo.sucesso";
	public static final String EXCLUIDO = "registro.excluido";
	
	@ApiOperation(value = "Detalha uma loja pelo ID", notes = "Um ID válido deve ser informado", response = Loja.class)
	@GetMapping("/{id}")
	public ResponseEntity<ServiceResponse<Loja>> getLoja(@PathVariable Integer id) {
		return ResponseEntity.ok(new ServiceResponse<>(lojaService.getLoja(id)));
	}

	
	@GetMapping
	@ApiOperation(value = "Lista", response = Loja.class)
	public ServiceResponse<Page<Loja>> getLojas(Pageable pageable) {
		return new ServiceResponse<>(lojaService.getLojas(pageable));
	}
	
	@GetMapping(value="/ListaTodas")
	@ApiOperation(value = "ListaTodas", response = Loja.class)
	public ServiceResponse<List<Loja>> getLojas() {
		return new ServiceResponse<>(lojaService.getLojas());
	}
	

	@PostMapping
	@ApiOperation(value = "Cria uma loja", response = Loja.class)
	public ResponseEntity<ServiceResponse<Loja>> criaLoja(@RequestBody @Valid Loja loja) {
		loja = lojaService.salvarLoja(loja);
		HttpHeaders headers = new HttpHeaders();
		URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(loja.getId())
				.toUri();
		headers.setLocation(location);
		ServiceMessage message = new ServiceMessage(messages.get(SALVO_COM_SUCESSO));
		
		return new ResponseEntity<>(new ServiceResponse<>(loja, message), headers, HttpStatus.CREATED);
	}
	
	@PutMapping("/{id}")
	@ApiOperation(value = "Altera uma loja", response = Loja.class)
	public ResponseEntity<ServiceResponse<Loja>> updateLoja(@PathVariable Integer id,
			@RequestBody @Valid Loja loja) {
		
		ServiceMessage message = new ServiceMessage(messages.get(SALVO_COM_SUCESSO));
		return new ResponseEntity<>(new ServiceResponse<>(lojaService.alterarLoja(loja), message), HttpStatus.OK);

	}
	

	@DeleteMapping("/{id}")
	@ApiOperation(value = "Ativa/Desativa uma loja", notes = "Um ID válido deve ser informado", response = Loja.class)
	public ResponseEntity<ServiceResponse<Void>> desativaLoja(@PathVariable Integer id) {
		lojaService.excluiLoja(id);
		ServiceMessage message = new ServiceMessage(messages.get(EXCLUIDO));
		return new ResponseEntity<>(new ServiceResponse<>(message), HttpStatus.OK);
	}
}
