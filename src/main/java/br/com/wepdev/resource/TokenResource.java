package br.com.wepdev.resource;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.wepdev.config.property.AlgamoneyApiProperty;

/**
 * Resource feito para implementacao da pagina de logout
 */
@RestController
@RequestMapping("/tokens")
public class TokenResource {
	
	
	@Autowired
	private AlgamoneyApiProperty algamoneyApiProperty; // pegando a configuracao setada no properties

	// Deleta o valor do refreshToken ao fazer logout
	@DeleteMapping("/revoke")
	public void revoke(HttpServletRequest req, HttpServletResponse resp) {
		
		Cookie cookie = new Cookie("refreshToken", null);
		cookie.setHttpOnly(true);
		// configuracao criada no properties para habilitar o https quando estiver em producão
		cookie.setSecure(algamoneyApiProperty.getSeguranca().isEnableHttps());
		cookie.setPath(req.getContextPath() + "/oauth/token");
		cookie.setMaxAge(0); // expira agora
		
		resp.addCookie(cookie);
		resp.setStatus(HttpStatus.NO_CONTENT.value());
	}
	
}
