//package br.com.wepdev.token;
//
//
//import javax.servlet.http.Cookie;
//import javax.servlet.http.HttpServletRequest;
//import javax.servlet.http.HttpServletResponse;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.core.MethodParameter;
//import org.springframework.http.MediaType;
//import org.springframework.http.converter.HttpMessageConverter;
//import org.springframework.http.server.ServerHttpRequest;
//import org.springframework.http.server.ServerHttpResponse;
//import org.springframework.http.server.ServletServerHttpRequest;
//import org.springframework.http.server.ServletServerHttpResponse;
//import org.springframework.security.oauth2.common.DefaultOAuth2AccessToken;
//import org.springframework.security.oauth2.common.OAuth2AccessToken;
//import org.springframework.web.bind.annotation.ControllerAdvice;
//import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;
//
//import br.com.wepdev.config.property.AlgamoneyApiProperty;
//
///**
// * Classe que trabalha o refresh token para que ele fique armazenado em um cookie http ( BOAS PRATICAS )
// * essa classe intercepta a requisão antes da resposta para quem fez a chamada
// *
// * @author wepbi
// *
// */
//@SuppressWarnings("deprecation")
//@ControllerAdvice
//public class RefreshTokenPostProcessor implements ResponseBodyAdvice<OAuth2AccessToken>{
//
//
//	@Autowired
//	private AlgamoneyApiProperty algamoneyApiProperty;
//
//
//	@Override
//	public boolean supports(MethodParameter returnType, Class<? extends HttpMessageConverter<?>> converterType) {
//		return returnType.getMethod().getName().equals("postAccessToken");
//	}
//
//	@Override
//	public OAuth2AccessToken beforeBodyWrite(OAuth2AccessToken body, MethodParameter returnType,
//			MediaType selectedContentType, Class<? extends HttpMessageConverter<?>> selectedConverterType,
//			ServerHttpRequest request, ServerHttpResponse response) {
//
//		HttpServletRequest req = ((ServletServerHttpRequest) request).getServletRequest(); // Transformando de ServerHttpRequest para HttpServletRequest
//		HttpServletResponse resp = ((ServletServerHttpResponse) response).getServletResponse(); // Transformando de ServerHttpResponse para HttpServletResponse
//
//		DefaultOAuth2AccessToken token = (DefaultOAuth2AccessToken) body; // Fazendo cast para DefaultOAuth2AccessToken
//
//		String refreshToken = body.getRefreshToken().getValue(); // Pegando o refresh token
//
//		adicionarRefreshTokenNoCookie(refreshToken, req, resp);
//		removerRefreshTokenDoBody(token);
//
//		return body;
//	}
//
//
//	private void removerRefreshTokenDoBody(DefaultOAuth2AccessToken token) {
//		token.setRefreshToken(null);
//
//	}
//
//	private void adicionarRefreshTokenNoCookie(String refreshToken, HttpServletRequest req, HttpServletResponse resp) {
//
//		Cookie refreshTokenCookie = new Cookie("refreshToken", refreshToken); // Criando um cookie
//		refreshTokenCookie.setHttpOnly(true); // acesso somente via HTTP
//		refreshTokenCookie.setSecure(algamoneyApiProperty.getSeguranca().isEnableHttps()); // Token que de deve funcionar apenas em HTTPS, em produção deve ser usado em TRUE
//		refreshTokenCookie.setPath(req.getContextPath() + "/oauth/token"); // Para qual canminho esse cookie deve ser enviado para o browse
//		refreshTokenCookie.setMaxAge(2592000); // em quanto tempo esse cookie vai expirar (2592000) dias
//		resp.addCookie(refreshTokenCookie); // Adicionando o cookie na resposta
//	}
//
//}
