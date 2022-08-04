package br.com.wepdev.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.store.JwtTokenStore;

@SuppressWarnings("deprecation")
@Configuration
@EnableAuthorizationServer
public class AuthorizationServerConfig extends AuthorizationServerConfigurerAdapter {
	
	
    @Autowired
	private AuthenticationManager authenticationManager;

	@Autowired
	private PasswordEncoder passwordEncoder;
	
	@Autowired
	private UserDetailsService userDetailsService;
	
    /*
     * Metodo que autoriza o cliente a acessar a api, no caso cliente sera o Angular.
     * Pode ser liberado para varios clientes	
     */
	@Override
	public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
		clients.inMemory()
				.withClient("angular")
				.secret(passwordEncoder.encode("@ngul@r0")) // @ngul@r0 senha para acessar
				.scopes("read", "write") // O que e permitido para o cliente angular, ler e escrever
				.authorizedGrantTypes("password", "refresh_token") // fluxo onde a aplicacao recebe usuario e senha para pegar o token
				.accessTokenValiditySeconds(10) // Tempo do token esta valido por 10 segundos, 30 min seria (1800)
				.refreshTokenValiditySeconds(3600 * 24); // tempo de vida do refresh token, 1 dia
	}
	

	/*
	 * Metodo onde define onde o token e armazenado, o Angular acessa esse token
	 */
	@Override
	public void configure(AuthorizationServerEndpointsConfigurer endpoints) throws Exception {
		endpoints
			.authenticationManager(authenticationManager) // verifica usuario e senha
			.accessTokenConverter(accessTokenConverter())
			.tokenStore(tokenStore())
			.userDetailsService(userDetailsService) // configura a parte de refresh do token
			.reuseRefreshTokens(false); // sempre que for feito o pedido do um novo access token , um novo refresh token sera enviado
	}
	
	/*
	 * Metodo que contem a chave que valida o token
	 */
	@Bean
	public JwtAccessTokenConverter accessTokenConverter() {
		JwtAccessTokenConverter accessTokenConverter = new JwtAccessTokenConverter();

		accessTokenConverter.setSigningKey("3032885ba9cd6621bcc4e7d6b6c35c2b");

		return accessTokenConverter;
	}


	@Bean
	public TokenStore tokenStore() {
		return new JwtTokenStore(accessTokenConverter());
	}


}