package br.com.ecommerce.gerencia.controller;

import java.net.URI;
import java.time.LocalDateTime;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import br.com.ecommerce.gerencia.model.Entrega;
import br.com.ecommerce.gerencia.model.StatusEntrega;
import br.com.ecommerce.gerencia.service.EntregaService;
import br.com.ecommerce.infra.response.ServiceMessage;
import br.com.ecommerce.infra.response.ServiceResponse;
import br.com.ecommerce.infra.service.MessagesService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@RestController
@CrossOrigin("*")
@RequestMapping("/api/v1/entrega")
@Api(value = "Entrega")
public class EntregaRestController {

	@Autowired
	EntregaService entregaService;

	@Autowired
	private MessagesService messages;

	public static final String SALVO_COM_SUCESSO = "salvo.sucesso";
	public static final String EXCLUIDO = "registro.excluido";

	@ApiOperation(value = "Detalha uma entrega pelo ID", notes = "Um ID válido deve ser informado", response = Entrega.class)
	@GetMapping("/{id}")
	public ResponseEntity<ServiceResponse<Entrega>> getEntrega(@PathVariable Integer id) {
		return ResponseEntity.ok(new ServiceResponse<>(entregaService.getEntrega(id)));
	}

	@PostMapping
	@ApiOperation(value = "Cria uma entrega", response = Entrega.class)
	public ResponseEntity<ServiceResponse<Entrega>> criaEntrega(@RequestBody @Valid Entrega entrega) {
		entrega = entregaService.salvarEntrega(entrega);
		HttpHeaders headers = new HttpHeaders();
		URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(entrega.getId())
				.toUri();
		headers.setLocation(location);
		ServiceMessage message = new ServiceMessage(messages.get(SALVO_COM_SUCESSO));

		entrega.setDataCriacao(LocalDateTime.now());
		entrega.setStatusEntrega(StatusEntrega.CRIADA);

		return new ResponseEntity<>(new ServiceResponse<>(entrega, message), headers, HttpStatus.CREATED);
	}

	@PutMapping("/atualizaStatus/{id}")
	@ApiOperation(value = "Altera uma entrega", response = Entrega.class)
	public ResponseEntity<ServiceResponse<Entrega>> updateStatusEntrega(@PathVariable Integer id,
			@RequestBody @Valid StatusEntrega status) {

		Entrega entrega = entregaService.getEntrega(id);
		entrega.setDataAtualizacao(LocalDateTime.now());
		entrega.setStatusEntrega(status);

		ServiceMessage message = new ServiceMessage(messages.get(SALVO_COM_SUCESSO));
		return new ResponseEntity<>(new ServiceResponse<>(entregaService.alterarEntrega(entrega), message),
				HttpStatus.OK);

	}
}
