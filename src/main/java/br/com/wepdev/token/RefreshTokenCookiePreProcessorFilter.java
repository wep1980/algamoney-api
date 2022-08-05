package br.com.wepdev.token;

import java.io.IOException;
import java.util.Map;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;

import org.apache.catalina.util.ParameterMap;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

@Component
@Order(Ordered.HIGHEST_PRECEDENCE) // Prioridade alta na requisição para adicionar o Cookie na requisição caso seja necessario
public class RefreshTokenCookiePreProcessorFilter implements Filter{

	
	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {

		HttpServletRequest req = (HttpServletRequest) request;
		
		if("/oauth/token".equalsIgnoreCase(req.getRequestURI())
				&& "refresh_token".equals(req.getParameter("grant_type"))
				&& req.getCookies() != null) {
			for(Cookie cookie : req.getCookies()) {
				if(cookie.getName().equals("refreshToken")) {
					String refreshToken = cookie.getValue(); // Como não e possivel mexer na requisição depois de pronta para adicinar o refresh token foi necessario criar a classe MyServletRequestWrapper
					
					// SOLUÇÃO COM STEAM
//					String refreshToken = 
//						      Stream.of(req.getCookies())
//						          .filter(cookie -> "refreshToken".equals(cookie.getName()))
//						          .findFirst()
//						          .map(cookie -> cookie.getValue())
//						          .orElse(null);
					
					req = new MyServletRequestWrapper(req, refreshToken); // a requisição foi substituida pela a que contem o token
				}
			}
		}
		chain.doFilter(req, response);
	}
	
	// Classe criada para o cokkie ser adicionado na requisição
	static class MyServletRequestWrapper extends HttpServletRequestWrapper {

		private String refreshToken;
		
		public MyServletRequestWrapper(HttpServletRequest request, String refreshToken) {
			super(request);
			this.refreshToken = refreshToken;
		}

		
		@Override
		public Map<String, String[]> getParameterMap() {
			ParameterMap<String, String[]> map = new ParameterMap<>(getRequest().getParameterMap());
			map.put("refresh_token", new String[] { refreshToken }); // Pega a requisicao que ja existe e so adiciona o token no cookie dela
			map.setLocked(true); // trava o mapa da requisicão
			return map;
		}

		
	}

}
