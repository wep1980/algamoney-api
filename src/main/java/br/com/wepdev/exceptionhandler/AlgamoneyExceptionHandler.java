package br.com.wepdev.exceptionhandler;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import lombok.Getter;

// Classe que captura excessoes de respostas de entidades
@ControllerAdvice // Observa toda a aplicação
public class AlgamoneyExceptionHandler extends ResponseEntityExceptionHandler{
	
	
	@Autowired
	private MessageSource messageSource; // Pegando a mensagem do arquivo messages.properties, objeto do spring
	
	/**
	 * Metodo que captura mensagens de erros que não é possivel ser lida
	 */
	@Override
	protected ResponseEntity<Object> handleHttpMessageNotReadable(HttpMessageNotReadableException ex,
			HttpHeaders headers, HttpStatus status, WebRequest request) {
		
		String mensagemUsuario = messageSource.getMessage("mensagem.invalida", null, LocaleContextHolder.getLocale());
		String mensagemDesenvolvedor = ex.getCause().toString();
				
		return handleExceptionInternal(ex, new Erro(mensagemUsuario, mensagemDesenvolvedor), headers, HttpStatus.BAD_REQUEST, request);
	}
	
	
	@Getter
	public static class Erro {
		
		private String mensagemUsuario;
		private String mensagemDesenvolvedor;
		
		public Erro(String mensagemUsuario, String mensagemDesenvolvedor) {
			super();
			this.mensagemUsuario = mensagemUsuario;
			this.mensagemDesenvolvedor = mensagemDesenvolvedor;
		}
		
		
		
	}

}
