package br.com.wepdev.resource;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.wepdev.event.RecursoCriadoEvent;
import br.com.wepdev.exceptionhandler.AlgamoneyExceptionHandler.Erro;
import br.com.wepdev.model.Lancamento;
import br.com.wepdev.repository.LancamentoRepository;
import br.com.wepdev.repository.filter.LancamentoFilter;
import br.com.wepdev.service.LancamentoService;
import br.com.wepdev.service.exception.PessoaInexistenteOuInativaException;

@RestController
@RequestMapping("/lancamentos")
public class LancamentoResource {

	
	@Autowired
	private LancamentoRepository lancamentoRepository;
	
	@Autowired
	private LancamentoService lancamentoService;
	
	// Responsavel por disparar os eventos
	@Autowired
	private ApplicationEventPublisher publisher;
	
	
	@Autowired
	private MessageSource messageSource;
	
	
	@GetMapping
	public List<Lancamento> pesquisar(LancamentoFilter lancamentofilter) {
		return lancamentoRepository.filtrar(lancamentofilter);
	}
	
	
	@GetMapping("/{codigo}")
	public ResponseEntity<Lancamento> buscarPeloCodigo(@PathVariable Long codigo) {
		Optional<Lancamento> lancamento = lancamentoRepository.findById(codigo);
		return lancamento.isPresent() ? ResponseEntity.ok(lancamento.get()) : ResponseEntity.notFound().build();
	}
	
	
	@PostMapping
	public ResponseEntity<Lancamento> criar(@Valid @RequestBody Lancamento lancamento, HttpServletResponse response) {
	    Lancamento lancamentoSalvo = lancamentoService.salvar(lancamento);
	    publisher.publishEvent(new RecursoCriadoEvent(this, response, lancamentoSalvo.getCodigo()));
	    return ResponseEntity.status(HttpStatus.CREATED).body(lancamentoSalvo);
	}
	
	
	@ExceptionHandler({PessoaInexistenteOuInativaException.class}) // Como essa excessão e uma exception especifica de lançamento, ela sera tratada diretamente aqui
	public ResponseEntity<Object> handlePessoaInexistenteOuInativaException(PessoaInexistenteOuInativaException ex){
		
		String mensagemUsuario = messageSource.getMessage("pessoa.inexistente-ou-inativa", null,
				LocaleContextHolder.getLocale());
		String mensagemDesenvolvedor = ex.toString();

		List<Erro> erros = Arrays.asList(new Erro(mensagemUsuario, mensagemDesenvolvedor));
		
		return ResponseEntity.badRequest().body(erros);
	}
	
	
	
	
	
	
	
	
}
