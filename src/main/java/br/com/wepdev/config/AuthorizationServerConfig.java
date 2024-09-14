//package br.com.wepdev.config;
//
//import java.util.Arrays;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.context.annotation.Profile;
//import org.springframework.security.authentication.AuthenticationManager;
//import org.springframework.security.core.userdetails.UserDetailsService;
//import org.springframework.security.crypto.password.PasswordEncoder;
//import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
//import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
//import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
//import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
//import org.springframework.security.oauth2.provider.token.TokenEnhancer;
//import org.springframework.security.oauth2.provider.token.TokenEnhancerChain;
//import org.springframework.security.oauth2.provider.token.TokenStore;
//import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;
//import org.springframework.security.oauth2.provider.token.store.JwtTokenStore;
//
//import br.com.wepdev.config.token.CustomTokenEnhancer;
//
//@Profile("oauth-security")
//@SuppressWarnings("deprecation")
//@Configuration
//@EnableAuthorizationServer
//public class AuthorizationServerConfig extends AuthorizationServerConfigurerAdapter {
//
//
//    @Autowired
//	private AuthenticationManager authenticationManager; // gerencia a autenticacao, pega o usuario e senha
//
//	@Autowired
//	private PasswordEncoder passwordEncoder;
//
//	@Autowired
//	private UserDetailsService userDetailsService;
//
//    /*
//     * Metodo que autoriza o cliente a acessar a api, no caso cliente sera o Angular.
//     * Pode ser liberado para varios clientes
//     */
//	@Override
//	public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
//
//		// Ta configurado em memoria pq somente o angular q vai utilizar, mas poderia ser configurado em banco de dados para poder adicionar N clientes
//		clients.inMemory()
//					.withClient("angular")// nome do cliente
//					.secret("$2a$10$UAc049fUm6Bxy8X/.mpn8.PfD2ncb4ZgvmEa5Hb.JOGVJNX1ampgG") // @ngul@r0 senha para acessar
//					.scopes("read", "write") // O que e permitido para o cliente angular, ler e escrever
//					.authorizedGrantTypes("password", "refresh_token") // fluxo onde a aplicacao recebe usuario e senha para pegar o token
//					.accessTokenValiditySeconds(1800) // Tempo do token esta valido por 10 segundos, 30 min seria (1800)
//					.refreshTokenValiditySeconds(3600 * 24) // tempo de vida do refresh token, 1 dia
//				.and()
//				     //Adicionando um novo cliente
//					.withClient("mobile")
//					.secret(passwordEncoder.encode("m0b1le")) // Forma insegura
//					.scopes("read")
//					.authorizedGrantTypes("password", "refresh_token")
//					.accessTokenValiditySeconds(1800)
//					.refreshTokenValiditySeconds(3600 * 24);
//	}
//
//
//	/*
//	 * Metodo onde define onde o token e armazenado, o Angular acessa esse token
//	 * Nesse metodo o token pode ser trabalhado com mais detalhes
//	 */
//	@Override
//	public void configure(AuthorizationServerEndpointsConfigurer endpoints) throws Exception {
//
//		TokenEnhancerChain tokenEnhancerChain = new TokenEnhancerChain();
//		tokenEnhancerChain.setTokenEnhancers(Arrays.asList(tokenEnhancer(), accessTokenConverter()));
//
//		endpoints
//			.authenticationManager(authenticationManager) // valida usuario e senha
//			.userDetailsService(userDetailsService) // configura a parte de refresh do token
//			.tokenEnhancer(tokenEnhancerChain) // esse token contentando as informações do usuario que esta logado tb e passado
//			.accessTokenConverter(accessTokenConverter())  // converte o token
//			.tokenStore(tokenStore())
//			.reuseRefreshTokens(false); // sempre que for feito o pedido do um novo access token , um novo refresh token sera enviado
//	}
//
//	/*
//	 * Metodo que contem a chave(senha) que valida o token
//	 */
//	@Bean
//	public JwtAccessTokenConverter accessTokenConverter() {
//		JwtAccessTokenConverter accessTokenConverter = new JwtAccessTokenConverter();
//
//		// senha que valida o token = 3032885ba9cd6621bcc4e7d6b6c35c2b
//		accessTokenConverter.setSigningKey("3032885ba9cd6621bcc4e7d6b6c35c2b");
//
//		return accessTokenConverter;
//	}
//
//	@Bean
//	public TokenStore tokenStore() {
//
//		return new JwtTokenStore(accessTokenConverter());
//	}
//
//	@Bean
//	public TokenEnhancer tokenEnhancer() {
//
//		return new CustomTokenEnhancer();
//	}
//
//}