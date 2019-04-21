package br.com.ecommerce.marketing.controller;

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
import br.com.ecommerce.marketing.model.Propaganda;
import br.com.ecommerce.marketing.model.StatusPropaganda;
import br.com.ecommerce.marketing.service.PropagandaService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@RestController
@CrossOrigin("*")
@RequestMapping("/api/v1/marketing/propaganda")
@Api(value = "Propaganda")
public class PropagandaRestController {


	@Autowired
	PropagandaService propagandaService;	
	
	@Autowired
	private MessagesService messages;
	
	public static final String SALVO_COM_SUCESSO = "salvo.sucesso";
	public static final String EXCLUIDO = "registro.excluido";
	
	@ApiOperation(value = "Detalha uma propaganda pelo ID", notes = "Um ID válido deve ser informado", response = Propaganda.class)
	@GetMapping("/{id}")
	public ResponseEntity<ServiceResponse<Propaganda>> getPropaganda(@PathVariable Integer id) {
		return ResponseEntity.ok(new ServiceResponse<>(propagandaService.getPropaganda(id)));
	}

	
	@GetMapping
	@ApiOperation(value = "Lista", response = Propaganda.class)
	public ServiceResponse<Page<Propaganda>> getPropagandas(Pageable pageable) {
		return new ServiceResponse<>(propagandaService.getPropagandas(pageable));
	}
	

	@PostMapping
	@ApiOperation(value = "Cria uma propaganda", response = Propaganda.class)
	public ResponseEntity<ServiceResponse<Propaganda>> criaPropaganda(@RequestBody @Valid Propaganda propaganda) {
		propaganda = propagandaService.salvarPropaganda(propaganda);
		HttpHeaders headers = new HttpHeaders();
		URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(propaganda.getId())
				.toUri();
		headers.setLocation(location);
		ServiceMessage message = new ServiceMessage(messages.get(SALVO_COM_SUCESSO));
		
		propaganda.setDataCriacao(LocalDateTime.now());
		propaganda.setStatusPropaganda(StatusPropaganda.CRIADA);
		
		return new ResponseEntity<>(new ServiceResponse<>(propaganda, message), headers, HttpStatus.CREATED);
	}
	
	@PutMapping("/{id}")
	@ApiOperation(value = "Altera uma propaganda", response = Propaganda.class)
	public ResponseEntity<ServiceResponse<Propaganda>> updatePropaganda(@PathVariable Integer id,
			@RequestBody @Valid Propaganda propaganda) {
		
		ServiceMessage message = new ServiceMessage(messages.get(SALVO_COM_SUCESSO));
		return new ResponseEntity<>(new ServiceResponse<>(propagandaService.alterarPropaganda(propaganda), message), HttpStatus.OK);

	}
	
	@PutMapping("/atualizaStatus/{id}")
	@ApiOperation(value = "Altera uma propaganda", response = Propaganda.class)
	public ResponseEntity<ServiceResponse<Propaganda>> updateStatusPropaganda(@PathVariable Integer id,
			@RequestBody @Valid StatusPropaganda status) {
		
		Propaganda propaganda = propagandaService.getPropaganda(id);
		propaganda.setDataAtualizacao(LocalDateTime.now());
		propaganda.setStatusPropaganda(status);
		
		ServiceMessage message = new ServiceMessage(messages.get(SALVO_COM_SUCESSO));
		return new ResponseEntity<>(new ServiceResponse<>(propagandaService.alterarPropaganda(propaganda), message), HttpStatus.OK);

	}
	
	@DeleteMapping("/{id}")
	@ApiOperation(value = "Ativa/Desativa uma propaganda", notes = "Um ID válido deve ser informado", response = Propaganda.class)
	public ResponseEntity<ServiceResponse<Void>> desativaPropaganda(@PathVariable Integer id) {
		propagandaService.excluiPropaganda(id);
		ServiceMessage message = new ServiceMessage(messages.get(EXCLUIDO));
		return new ResponseEntity<>(new ServiceResponse<>(message), HttpStatus.OK);
	}
}

