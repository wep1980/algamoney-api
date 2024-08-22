package br.com.wepdev.event;

import javax.servlet.http.HttpServletResponse;

import org.springframework.context.ApplicationEvent;

import lombok.Getter;

/**
 * Classe de Evento criada para disparar o header Location e tirar o codigo repetitivo dos Resources e adcionar
 * o Header location
 */
@Getter
public class RecursoCriadoEvent extends ApplicationEvent{
	private static final long serialVersionUID = 1L;
	
	
	private HttpServletResponse response;
	private Long codigo;
	
	public RecursoCriadoEvent(Object source, HttpServletResponse response, Long codigo) {
		super(source);
		this.response = response;
		this.codigo = codigo;
		
	}

}
