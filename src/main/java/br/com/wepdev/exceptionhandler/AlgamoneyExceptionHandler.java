package br.com.wepdev.exceptionhandler;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import lombok.Getter;

// Classe que captura excessoes de respostas de entidades de toda API
@ControllerAdvice // Observa toda a aplicação, sem essa anotação nao e possivel capturar as excessoes
public class AlgamoneyExceptionHandler extends ResponseEntityExceptionHandler {

	@Autowired
	private MessageSource messageSource; // Pegando as mensagens do arquivo messages.properties, objeto do spring

	
	/**
	 * Metodo que captura mensagens de erros que não é possivel ser lida
	 */
	@Override
	protected ResponseEntity<Object> handleHttpMessageNotReadable(HttpMessageNotReadableException ex,
			HttpHeaders headers, HttpStatus status, WebRequest request) {

		// Pegando a mensagem do messages.properties e passando essa mensagem de erro para o usuario
		String mensagemUsuario = this.messageSource.getMessage("mensagem.invalida", null,
				LocaleContextHolder.getLocale());

		// Passando msg para o desenvolvedor. se nao existir a causa do erro, e mostrado direto o toString()
		String mensagemDesenvolvedor = Optional.ofNullable(ex.getCause()).orElse(ex).toString(); // Linha modificada

		List<Erro> erros = Arrays.asList(new Erro(mensagemUsuario, mensagemDesenvolvedor));

		return handleExceptionInternal(ex, erros, headers, HttpStatus.BAD_REQUEST, request);
	}


	/**
	 * Metodo que trata os argumentos de um metodo invalido, pega os erros do @Valid passado nos,
	 * Controllers ou Resources
	 */
	@Override
	protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex,
			HttpHeaders headers, HttpStatus status, WebRequest request) {

		// Retorna uma lista de erros, todos os campos que contem erro. Exp : nome, descricao, etc.....
		// getBindingResult() -> dentro dele contem todos os erros
		List<Erro> erros = criarListaDeErros(ex.getBindingResult());

		return handleExceptionInternal(ex, erros, headers, HttpStatus.BAD_REQUEST, request);
	}
	

	// Metodo customizado de tratamento da exception EmptyResultDataAccessException
	// @ResponseStatus(HttpStatus.NOT_FOUND) se não quiser enviar o corpo e so habilitar esse ResponseStatus
	@ExceptionHandler({ EmptyResultDataAccessException.class }) // Escificando a exception a ser tratada
	public ResponseEntity<Object> handleEmptyResultDataAccessException(EmptyResultDataAccessException ex,
			WebRequest request) {

		String mensagemUsuario = messageSource.getMessage("recurso.nao-encontrado", null,
				LocaleContextHolder.getLocale());
		String mensagemDesenvolvedor = ex.toString();

		List<Erro> erros = Arrays.asList(new Erro(mensagemUsuario, mensagemDesenvolvedor));

		return handleExceptionInternal(ex, erros, new HttpHeaders(), HttpStatus.NOT_FOUND, request);
	}
	

	@ExceptionHandler({ DataIntegrityViolationException.class })
	public ResponseEntity<Object> handleDataIntegrityViolationException(DataIntegrityViolationException ex,
			WebRequest request) {
		
		String mensagemUsuario = messageSource.getMessage("recurso.operacao-nao-permitida", null,
				LocaleContextHolder.getLocale());
		String mensagemDesenvolvedor = ExceptionUtils.getRootCauseMessage(ex);

		List<Erro> erros = Arrays.asList(new Erro(mensagemUsuario, mensagemDesenvolvedor));

		return handleExceptionInternal(ex, erros, new HttpHeaders(), HttpStatus.BAD_REQUEST, request);

	}


	/**
	 * Metodo responsavel por criar a lista de erros, os campos que contem erros ao serem passados na
	 * representacão(postman e etc...).
	 * BindingResult bindingResult -> dentro dele contem todos os erros
	 * @param bindingResult
	 * @return
	 */
	private List<Erro> criarListaDeErros(BindingResult bindingResult) {
		List<Erro> erros = new ArrayList<>();

		  // Pegando todos os campos das entidades que contem erros
		for (FieldError fieldError : bindingResult.getFieldErrors()) {

			// Passagem da msg de erro para o usuario que contem o nome do campo que esta errado
			String mensagemUsuario = messageSource.getMessage(fieldError, LocaleContextHolder.getLocale());

			String mensagemDesenvolvedor = fieldError.toString();

			erros.add(new Erro(mensagemUsuario, mensagemDesenvolvedor));
		}
		return erros;
	}

	/**
	 * Classe criada para passar mensagens de erro.
	 */
	@Getter
	public static class Erro {

		private String mensagemUsuario;
		private String mensagemDesenvolvedor;

		public Erro(String mensagemUsuario, String mensagemDesenvolvedor) {
			this.mensagemUsuario = mensagemUsuario;
			this.mensagemDesenvolvedor = mensagemDesenvolvedor;
		}

	}

}
