package br.com.ecommerce.vendas.controller;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URI;
import java.nio.file.Files;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
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
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import br.com.ecommerce.infra.response.ServiceMessage;
import br.com.ecommerce.infra.response.ServiceResponse;
import br.com.ecommerce.infra.service.MessagesService;
import br.com.ecommerce.vendas.model.Produto;
import br.com.ecommerce.vendas.service.ProdutoService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@RestController
@RequestMapping("/api/v1/produto")
@Api(value = "Produto")
public class ProdutoRestController {


	@Autowired
	ProdutoService produtoService;	
	
	@Autowired
	private MessagesService messages;
	
	public static final String SALVO_COM_SUCESSO = "salvo.sucesso";
	public static final String EXCLUIDO = "registro.excluido";
	
	@ApiOperation(value = "Detalha um produto pelo ID", notes = "Um ID valido deve ser informado", response = Produto.class)
	@GetMapping("/{id}")
	public ResponseEntity<ServiceResponse<Produto>> getproduto(@PathVariable Integer id) {
		return ResponseEntity.ok(new ServiceResponse<>(produtoService.getProduto(id)));
	}

	@GetMapping
	@ApiOperation(value = "Lista", response = Produto.class)
	public ServiceResponse<Page<Produto>> getProdutos(Pageable pageable) {
		return new ServiceResponse<>(produtoService.getProdutos(pageable));
	}
	
	@PostMapping
	@ApiOperation(value = "Cria um produto", response = Produto.class)
	public ResponseEntity<ServiceResponse<Produto>> criaproduto(@RequestBody @Valid Produto produto) {
		produto = produtoService.salvarProduto(produto);
		HttpHeaders headers = new HttpHeaders();
		URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(produto.getId())
				.toUri();
		headers.setLocation(location);
		ServiceMessage message = new ServiceMessage(messages.get(SALVO_COM_SUCESSO));
		
		return new ResponseEntity<>(new ServiceResponse<>(produto, message), headers, HttpStatus.CREATED);
	}
	
	@PutMapping("/{id}")
	@ApiOperation(value = "Altera um produto", response = Produto.class)
	public ResponseEntity<ServiceResponse<Produto>> updateproduto(@PathVariable Integer id,
			@RequestBody @Valid Produto produto) {
		
		ServiceMessage message = new ServiceMessage(messages.get(SALVO_COM_SUCESSO));
		return new ResponseEntity<>(new ServiceResponse<>(produtoService.alterarProduto(produto), message), HttpStatus.OK);

	}
	

	@DeleteMapping("/{id}")
	@ApiOperation(value = "Ativa/Desativa um produto", notes = "Um ID valido deve ser informado", response = Produto.class)
	public ResponseEntity<ServiceResponse<Void>> desativaProduto(@PathVariable Integer id) {
		produtoService.excluiProduto(id);
		ServiceMessage message = new ServiceMessage(messages.get(EXCLUIDO));
		return new ResponseEntity<>(new ServiceResponse<>(message), HttpStatus.OK);
	}
	
	@ApiOperation(value = "Imagem", notes = "Imagem", response = Produto.class)
	@PostMapping(name="imagem", value="/imagem")
	public ResponseEntity<ServiceResponse<String>>  save(@RequestParam(value="files") MultipartFile imagem) {

		 try {
				FileOutputStream fos = new FileOutputStream("/imgs/"+imagem.getOriginalFilename()); 
			    fos.write(imagem.getBytes());
			    fos.close(); 
			} catch (IOException e) {
				e.printStackTrace();
			} 
		 
			return new ResponseEntity<>(new ServiceResponse<>(imagem.getOriginalFilename(), new ServiceMessage(messages.get(SALVO_COM_SUCESSO))), HttpStatus.OK);
					
	}
	
	@ApiOperation(value = "Imagemget", notes = "Imagemget")
	@GetMapping("/imagem/{imagem}")
	public ResponseEntity<Resource> form(String imagem) {
		try {
			HttpHeaders httpHeaders = new HttpHeaders();
			httpHeaders.setContentType(MediaType.IMAGE_PNG);
			
			File fi = new File("/imgs/"+imagem);
			byte[] fileContent;
			try {
				fileContent = Files.readAllBytes(fi.toPath());
			} catch (IOException e) {
				return null;
			}
			
			ResponseEntity<Resource> responseEntity;
				responseEntity =  new ResponseEntity<Resource>(
							new ByteArrayResource(fileContent), httpHeaders, HttpStatus.OK);
			
			return responseEntity;
			
		} catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity<Resource>(HttpStatus.BAD_REQUEST);
		}
	}
	
}
