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
import br.com.ecommerce.sac.model.Solicitacao;
import br.com.ecommerce.sac.model.StatusSolicitacao;
import br.com.ecommerce.sac.service.SolicitacaoService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@RestController
@CrossOrigin("*")
@RequestMapping("/api/v1/sac/solicitacao")
@Api(value = "Solicitacao")
public class SolicitacaoRestController {


	@Autowired
	SolicitacaoService solicitacaoService;	
	
	@Autowired
	private MessagesService messages;
	
	public static final String SALVO_COM_SUCESSO = "salvo.sucesso";
	public static final String EXCLUIDO = "registro.excluido";
	
	@ApiOperation(value = "Detalha uma solicitacao pelo ID", notes = "Um ID válido deve ser informado", response = Solicitacao.class)
	@GetMapping("/{id}")
	public ResponseEntity<ServiceResponse<Solicitacao>> getSolicitacao(@PathVariable Integer id) {
		return ResponseEntity.ok(new ServiceResponse<>(solicitacaoService.getSolicitacao(id)));
	}

	
	@GetMapping
	@ApiOperation(value = "Lista", response = Solicitacao.class)
	public ServiceResponse<Page<Solicitacao>> getSolicitacaos(Pageable pageable) {
		return new ServiceResponse<>(solicitacaoService.getSolicitacaos(pageable));
	}
	

	@PostMapping
	@ApiOperation(value = "Cria uma solicitacao", response = Solicitacao.class)
	public ResponseEntity<ServiceResponse<Solicitacao>> criaSolicitacao(@RequestBody @Valid Solicitacao solicitacao) {
		solicitacao = solicitacaoService.salvarSolicitacao(solicitacao);
		HttpHeaders headers = new HttpHeaders();
		URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(solicitacao.getId())
				.toUri();
		headers.setLocation(location);
		ServiceMessage message = new ServiceMessage(messages.get(SALVO_COM_SUCESSO));
		
		solicitacao.setDataCriacao(LocalDateTime.now());
		solicitacao.setStatusSolicitacao(StatusSolicitacao.CRIADA);
		
		return new ResponseEntity<>(new ServiceResponse<>(solicitacao, message), headers, HttpStatus.CREATED);
	}
	
	@PutMapping("/{id}")
	@ApiOperation(value = "Altera uma solicitacao", response = Solicitacao.class)
	public ResponseEntity<ServiceResponse<Solicitacao>> updateSolicitacao(@PathVariable Integer id,
			@RequestBody @Valid Solicitacao solicitacao) {
		
		ServiceMessage message = new ServiceMessage(messages.get(SALVO_COM_SUCESSO));
		return new ResponseEntity<>(new ServiceResponse<>(solicitacaoService.alterarSolicitacao(solicitacao), message), HttpStatus.OK);

	}
	
	@PutMapping("/atualizaStatus/{id}")
	@ApiOperation(value = "Altera uma solicitacao", response = Solicitacao.class)
	public ResponseEntity<ServiceResponse<Solicitacao>> updateStatusSolicitacao(@PathVariable Integer id,
			@RequestBody @Valid StatusSolicitacao status) {
		
		Solicitacao solicitacao = solicitacaoService.getSolicitacao(id);
		solicitacao.setDataAtualizacao(LocalDateTime.now());
		solicitacao.setStatusSolicitacao(status);
		
		ServiceMessage message = new ServiceMessage(messages.get(SALVO_COM_SUCESSO));
		return new ResponseEntity<>(new ServiceResponse<>(solicitacaoService.alterarSolicitacao(solicitacao), message), HttpStatus.OK);

	}
	
	@DeleteMapping("/{id}")
	@ApiOperation(value = "Ativa/Desativa uma solicitacao", notes = "Um ID válido deve ser informado", response = Solicitacao.class)
	public ResponseEntity<ServiceResponse<Void>> desativaSolicitacao(@PathVariable Integer id) {
		solicitacaoService.excluiSolicitacao(id);
		ServiceMessage message = new ServiceMessage(messages.get(EXCLUIDO));
		return new ResponseEntity<>(new ServiceResponse<>(message), HttpStatus.OK);
	}
}
