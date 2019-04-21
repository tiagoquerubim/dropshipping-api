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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import br.com.ecommerce.infra.response.ServiceMessage;
import br.com.ecommerce.infra.response.ServiceResponse;
import br.com.ecommerce.infra.service.MessagesService;
import br.com.ecommerce.vendas.model.Categoria;
import br.com.ecommerce.vendas.service.CategoriaService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@RestController
@RequestMapping("/api/v1/categoria")
@Api(value = "Categoria")
public class CategoriaRestController {

	@Autowired
	private MessagesService messages;
	
	@Autowired
	CategoriaService categoriaService;
	
	public static final String SALVO_COM_SUCESSO = "salvo.sucesso";
	public static final String EXCLUIDO = "registro.excluido";
	
	@ApiOperation(value = "Detalha uma categoria pelo ID", notes = "Um ID válido deve ser informado", response = Categoria.class)
	@GetMapping("/{id}")
	public ResponseEntity<ServiceResponse<Categoria>> getCategoria(@PathVariable Integer id) {
		return ResponseEntity.ok(new ServiceResponse<>(categoriaService.getCategoria(id)));
	}
	
	@GetMapping
	@ApiOperation(value = "Lista", response = Categoria.class)
	public ServiceResponse<List<Categoria>> getCategoria(Pageable pageable) {
		return new ServiceResponse<>(categoriaService.getCategorias());
	}
	
	@GetMapping("/categoriasPaginado")
	@ApiOperation(value = "Lista de categorias paginado", response = Categoria.class)
	public ServiceResponse<Page<Categoria>> getProdutosPaginado(Pageable pageable) {
		return new ServiceResponse<>(categoriaService.getProdutosPaginado(pageable));
	}
	
	@GetMapping("/filtroPaginado")
	@ApiOperation(value = "Lista os categorias com filtro", response = Categoria.class)
	public ServiceResponse<Page<Categoria>> listaCategoriasFiltradoPaginado(@RequestParam(value = "descricao", required = false) String descricao,
			@RequestParam(value = "id", required = false) Integer id, Pageable pageable) {
		return new ServiceResponse<>(
				categoriaService.filtraCategoriasPaginado(descricao != null ? descricao : "",
						id,	pageable));
	}
	
	
	@PostMapping
	@ApiOperation(value = "Cria um categoria", response = Categoria.class)
	public ResponseEntity<ServiceResponse<Categoria>> criaCategoria(@RequestBody @Valid Categoria categoria) {
		categoria = categoriaService.salvarCategoria(categoria);
		HttpHeaders headers = new HttpHeaders();
		URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(categoria.getId())
				.toUri();
		headers.setLocation(location);
		ServiceMessage message = new ServiceMessage(messages.get(SALVO_COM_SUCESSO));
		
		return new ResponseEntity<>(new ServiceResponse<>(categoria, message), headers, HttpStatus.CREATED);
	}
	
	@PutMapping("/{id}")
	@ApiOperation(value = "Altera uma categoria", response = Categoria.class)
	public ResponseEntity<ServiceResponse<Categoria>> updateCategoria(@PathVariable Integer id,
			@RequestBody @Valid Categoria categoria) {
		
		ServiceMessage message = new ServiceMessage(messages.get(SALVO_COM_SUCESSO));
		return new ResponseEntity<>(new ServiceResponse<>(categoriaService.alterarCategoria(categoria), message), HttpStatus.OK);

	}
	
	@DeleteMapping("/{id}")
	@ApiOperation(value = "Ativa/Desativa uma categoria", notes = "Um ID válido deve ser informado", response = Categoria.class)
	public ResponseEntity<ServiceResponse<Void>> desativaCategoria(@PathVariable Integer id) {
		categoriaService.excluiCategoria(id);
		ServiceMessage message = new ServiceMessage(messages.get(EXCLUIDO));
		return new ResponseEntity<>(new ServiceResponse<>(message), HttpStatus.OK);
	}
	
}
