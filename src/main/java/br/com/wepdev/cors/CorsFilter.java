package br.com.wepdev.cors;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import br.com.wepdev.config.property.AlgamoneyApiProperty;

/**
 * Filtro criado para a liberação dos CORS, pois no OAUth 2 essa e melhor forma de funcionar a liberção
 * @author wepbi
 *
 */
@Component
@Order(Ordered.HIGHEST_PRECEDENCE) // filtro com alta prioridade, para ser executado logo no inicio
public class CorsFilter implements Filter{

	@Autowired
	private AlgamoneyApiProperty algamoneyApiProperty;


	@Override
	public void doFilter(ServletRequest req, ServletResponse resp, FilterChain chain)
			throws IOException, ServletException {

		HttpServletRequest request = (HttpServletRequest) req; // Fazendo cast
		HttpServletResponse response = (HttpServletResponse) resp; // Fazendo cast

		// Essas 2 configuração estão de fora pq precisam ser sempre enviadas em todas as requisições
		response.setHeader("Access-Control-Allow-Origin", algamoneyApiProperty.getOriginPermitida());
        response.setHeader("Access-Control-Allow-Credentials", "true"); // Configuração para o cookie que contem o refreshToken ser permitido

        if ("OPTIONS".equals(request.getMethod()) && algamoneyApiProperty.getOriginPermitida().equals(request.getHeader("Origin"))) {
			response.setHeader("Access-Control-Allow-Methods", "POST, GET, DELETE, PUT, OPTIONS");
        	response.setHeader("Access-Control-Allow-Headers", "Authorization, Content-Type, Accept");

			// So depois de 1 hora que o browse vai fazer a proxima requisição, pq antes disso essa config fica armazenada em cache
        	response.setHeader("Access-Control-Max-Age", "3600");

			response.setStatus(HttpServletResponse.SC_OK);
		} else {
			chain.doFilter(req, resp);
		}
	}

}
