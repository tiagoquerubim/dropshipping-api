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
import br.com.ecommerce.marketing.model.Promocao;
import br.com.ecommerce.marketing.model.StatusPromocao;
import br.com.ecommerce.marketing.service.PromocaoService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@RestController
@CrossOrigin("*")
@RequestMapping("/api/v1/marketing/promocao")
@Api(value = "Promocao")
public class PromocaoRestController {


	@Autowired
	PromocaoService promocaoService;	
	
	@Autowired
	private MessagesService messages;
	
	public static final String SALVO_COM_SUCESSO = "salvo.sucesso";
	public static final String EXCLUIDO = "registro.excluido";
	
	@ApiOperation(value = "Detalha uma promocao pelo ID", notes = "Um ID válido deve ser informado", response = Promocao.class)
	@GetMapping("/{id}")
	public ResponseEntity<ServiceResponse<Promocao>> getPromocao(@PathVariable Integer id) {
		return ResponseEntity.ok(new ServiceResponse<>(promocaoService.getPromocao(id)));
	}

	
	@GetMapping
	@ApiOperation(value = "Lista", response = Promocao.class)
	public ServiceResponse<Page<Promocao>> getPromocaos(Pageable pageable) {
		return new ServiceResponse<>(promocaoService.getPromocaos(pageable));
	}
	

	@PostMapping
	@ApiOperation(value = "Cria uma promocao", response = Promocao.class)
	public ResponseEntity<ServiceResponse<Promocao>> criaPromocao(@RequestBody @Valid Promocao promocao) {
		promocao = promocaoService.salvarPromocao(promocao);
		HttpHeaders headers = new HttpHeaders();
		URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(promocao.getId())
				.toUri();
		headers.setLocation(location);
		ServiceMessage message = new ServiceMessage(messages.get(SALVO_COM_SUCESSO));
		
		promocao.setDataCriacao(LocalDateTime.now());
		promocao.setStatusPromocao(StatusPromocao.CRIADA);
		
		return new ResponseEntity<>(new ServiceResponse<>(promocao, message), headers, HttpStatus.CREATED);
	}
	
	@PutMapping("/{id}")
	@ApiOperation(value = "Altera uma promocao", response = Promocao.class)
	public ResponseEntity<ServiceResponse<Promocao>> updatePromocao(@PathVariable Integer id,
			@RequestBody @Valid Promocao promocao) {
		
		ServiceMessage message = new ServiceMessage(messages.get(SALVO_COM_SUCESSO));
		return new ResponseEntity<>(new ServiceResponse<>(promocaoService.alterarPromocao(promocao), message), HttpStatus.OK);

	}
	
	@PutMapping("/atualizaStatus/{id}")
	@ApiOperation(value = "Altera uma promocao", response = Promocao.class)
	public ResponseEntity<ServiceResponse<Promocao>> updateStatusPromocao(@PathVariable Integer id,
			@RequestBody @Valid StatusPromocao status) {
		
		Promocao promocao = promocaoService.getPromocao(id);
		promocao.setDataAtualizacao(LocalDateTime.now());
		promocao.setStatusPromocao(status);
		
		ServiceMessage message = new ServiceMessage(messages.get(SALVO_COM_SUCESSO));
		return new ResponseEntity<>(new ServiceResponse<>(promocaoService.alterarPromocao(promocao), message), HttpStatus.OK);

	}
	
	@DeleteMapping("/{id}")
	@ApiOperation(value = "Ativa/Desativa uma promocao", notes = "Um ID válido deve ser informado", response = Promocao.class)
	public ResponseEntity<ServiceResponse<Void>> desativaPromocao(@PathVariable Integer id) {
		promocaoService.excluiPromocao(id);
		ServiceMessage message = new ServiceMessage(messages.get(EXCLUIDO));
		return new ResponseEntity<>(new ServiceResponse<>(message), HttpStatus.OK);
	}
}

