package br.com.wepdev.resource;

import java.net.URI;
import java.util.List;
import java.util.Optional;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import br.com.wepdev.event.RecursoCriadoEvent;
import br.com.wepdev.model.Categoria;
import br.com.wepdev.repository.CategoriaRepository;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

@RestController
@RequestMapping("/categorias")
public class CategoriaResource {

	@Autowired
	private CategoriaRepository categoriaRepository;

	// Responsavel por disparar os eventos
	@Autowired
	private ApplicationEventPublisher publisher;


	/**
	 * Se não tiver nenhuma categoria na lista, retorna uma lista vazia.
	 * @CrossOrigin(maxAge = 10, origins = { "http://localhost:8000" })
	 * @return
	 */
	//@CrossOrigin(maxAge = 10, origins = { "http://localhost:8000" }) // Liberando o CORs somente para esse endpoint na origem http://localhost:8000
	@GetMapping
	// Somente usuarios com essa permissao pode acessar esse endPoint
	@PreAuthorize("hasAuthority('ROLE_PESQUISAR_CATEGORIA') and hasAuthority('SCOPE_read')" )
	public List<Categoria> listar() {
		return categoriaRepository.findAll();
	}


	/**
	 * HttpServletResponse response -> E enviado dentro do herader a url desse recurso que foi criado.
	 * do Header.
	 * @param categoria
	 * @param response
	 * @return
	 */
	@PostMapping
	//@ResponseStatus(HttpStatus.CREATED)
	/**
	 * @Valid -> valida a categoria que esta sendo recebida
	 */
	@PreAuthorize("hasAuthority('ROLE_CADASTRAR_CATEGORIA') and hasAuthority('SCOPE_write')")
	public ResponseEntity<Categoria> criar(@Valid @RequestBody Categoria categoria, HttpServletResponse response) {

		Categoria categoriaSalva = categoriaRepository.save(categoria);

		/*
		trecho de codigo que retorna a url do recurso criado ao salvar uma categoria.
		Foi criado um evento e um listner para substituir o codigo abaixo
		 */
//		URI uri = ServletUriComponentsBuilder.fromCurrentRequestUri().path("/{codigo}")
//						.buildAndExpand(categoriaSalva.getCodigo()).toUri();
//		response.setHeader("Location", uri.toASCIIString());

		//return ResponseEntity.created(uri).body(categoriaSalva);

		// Chama o evento criado por nos atraves do listner tb criado por mim
	    publisher.publishEvent(new RecursoCriadoEvent(this, response, categoriaSalva.getCodigo()));

		return ResponseEntity.status(HttpStatus.CREATED).body(categoriaSalva);
	}

	
	@GetMapping("/{codigo}")
	@PreAuthorize("hasAuthority('ROLE_PESQUISAR_CATEGORIA') and hasAuthority('SCOPE_read')")
	public ResponseEntity<Categoria> buscarPeloCodigo(@PathVariable Long codigo) {

		Optional<Categoria> categoria = this.categoriaRepository.findById(codigo);

		// Se tiver categoria retorna 200 OK e a categoria, senão retorna notFound
		return categoria.isPresent() ? ResponseEntity.ok(categoria.get()) : ResponseEntity.notFound().build();

		//Abaixo o codigo de retorno mais elegante
		//return categoria.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
	}

}
