package br.com.ecommerce.sac.controller;

import java.net.URI;
import java.time.LocalDateTime;

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
import br.com.ecommerce.sac.model.Duvida;
import br.com.ecommerce.sac.model.StatusDuvida;
import br.com.ecommerce.sac.service.DuvidaService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@RestController
@CrossOrigin("*")
@RequestMapping("/api/v1/sac/duvida")
@Api(value = "Promocao")
public class DuvidaRestController {


	@Autowired
	DuvidaService duvidaService;	
	
	@Autowired
	private MessagesService messages;
	
	public static final String SALVO_COM_SUCESSO = "salvo.sucesso";
	public static final String EXCLUIDO = "registro.excluido";
	
	@ApiOperation(value = "Detalha uma duvida pelo ID", notes = "Um ID válido deve ser informado", response = Duvida.class)
	@GetMapping("/{id}")
	public ResponseEntity<ServiceResponse<Duvida>> getDuvida(@PathVariable Integer id) {
		return ResponseEntity.ok(new ServiceResponse<>(duvidaService.getDuvida(id)));
	}

	
	@GetMapping
	@ApiOperation(value = "Lista", response = Duvida.class)
	public ServiceResponse<Page<Duvida>> getDuvidas(Pageable pageable) {
		return new ServiceResponse<>(duvidaService.getDuvidas(pageable));
	}
	

	@PostMapping
	@ApiOperation(value = "Cria uma duvida", response = Duvida.class)
	public ResponseEntity<ServiceResponse<Duvida>> criaDuvida(@RequestBody @Valid Duvida duvida) {
		duvida = duvidaService.salvarDuvida(duvida);
		HttpHeaders headers = new HttpHeaders();
		URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(duvida.getId())
				.toUri();
		headers.setLocation(location);
		ServiceMessage message = new ServiceMessage(messages.get(SALVO_COM_SUCESSO));
		
		duvida.setDataCriacao(LocalDateTime.now());
		duvida.setStatusDuvida(StatusDuvida.CRIADA);
		
		return new ResponseEntity<>(new ServiceResponse<>(duvida, message), headers, HttpStatus.CREATED);
	}
	
	@PutMapping("/{id}")
	@ApiOperation(value = "Altera uma duvida", response = Duvida.class)
	public ResponseEntity<ServiceResponse<Duvida>> updateDuvida(@PathVariable Integer id,
			@RequestBody @Valid Duvida duvida) {
		
		ServiceMessage message = new ServiceMessage(messages.get(SALVO_COM_SUCESSO));
		return new ResponseEntity<>(new ServiceResponse<>(duvidaService.alterarDuvida(duvida), message), HttpStatus.OK);

	}
	
	@PutMapping("/atualizaStatus/{id}")
	@ApiOperation(value = "Altera uma duvida", response = Duvida.class)
	public ResponseEntity<ServiceResponse<Duvida>> updateStatusDuvida(@PathVariable Integer id,
			@RequestBody @Valid StatusDuvida status) {
		
		Duvida duvida = duvidaService.getDuvida(id);
		duvida.setDataAtualizacao(LocalDateTime.now());
		duvida.setStatusDuvida(status);
		
		ServiceMessage message = new ServiceMessage(messages.get(SALVO_COM_SUCESSO));
		return new ResponseEntity<>(new ServiceResponse<>(duvidaService.alterarDuvida(duvida), message), HttpStatus.OK);

	}
	
	@DeleteMapping("/{id}")
	@ApiOperation(value = "Ativa/Desativa uma duvida", notes = "Um ID válido deve ser informado", response = Duvida.class)
	public ResponseEntity<ServiceResponse<Void>> desativaDuvida(@PathVariable Integer id) {
		duvidaService.excluiDuvida(id);
		ServiceMessage message = new ServiceMessage(messages.get(EXCLUIDO));
		return new ResponseEntity<>(new ServiceResponse<>(message), HttpStatus.OK);
	}
}

