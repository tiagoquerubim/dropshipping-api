package br.com.ecommerce.vendas.controller;

import java.net.URI;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import br.com.ecommerce.infra.response.ServiceMessage;
import br.com.ecommerce.infra.response.ServiceResponse;
import br.com.ecommerce.infra.service.MessagesService;
import br.com.ecommerce.vendas.model.Venda;
import br.com.ecommerce.vendas.service.VendaService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@RestController
@CrossOrigin("*")
@RequestMapping("/api/v1/venda")
@Api(value = "venda")
public class VendaRestController {

	@Autowired
	private VendaService vendaService;

	@Autowired
	private MessagesService messages;

	public static final String SALVO_COM_SUCESSO = "salvo.sucesso";
	public static final String EXCLUIDO = "registro.excluido";

	@ApiOperation(value = "Detalha uma venda pelo ID", notes = "Um ID válido deve ser informado", response = Venda.class)
	@GetMapping("/{id}")
	public ResponseEntity<ServiceResponse<Venda>> getVenda(@PathVariable Integer id) {
		return ResponseEntity.ok(new ServiceResponse<>(vendaService.getVenda(id)));
	}

	@PostMapping
	@ApiOperation(value = "Cria uma venda", response = Venda.class)
	public ResponseEntity<ServiceResponse<Venda>> criaLoja(@RequestBody @Valid Venda venda) {
		venda = vendaService.salvarVenda(venda);
		HttpHeaders headers = new HttpHeaders();
		URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(venda.getId())
				.toUri();
		headers.setLocation(location);
		ServiceMessage message = new ServiceMessage(messages.get(SALVO_COM_SUCESSO));

		return new ResponseEntity<>(new ServiceResponse<>(venda, message), headers, HttpStatus.CREATED);
		
	}
}
