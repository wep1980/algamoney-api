//package br.com.wepdev.config;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Profile;
//import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
//import org.springframework.security.config.annotation.web.builders.HttpSecurity;
//import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
//import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
//import org.springframework.security.config.http.SessionCreationPolicy;
//import org.springframework.security.core.userdetails.UserDetailsService;
//import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
//import org.springframework.security.crypto.password.PasswordEncoder;
//
///*
// * Adicionando a segurança do tipo basic tb para facilitar no desenvolvimento do front
// */
//@SuppressWarnings("deprecation")
//@Profile("basic-security")
//@EnableWebSecurity
//public class BasicSecurityConfig extends WebSecurityConfigurerAdapter {
//
//	@Autowired
//	private UserDetailsService userDetailsService;
//
//
//	/**
//	 * Primeiro metodo implementado para segurança Basic.
//	 * PASSO 1
//	 *
//	 * No postman , colocar em Authorization, type Basic Auth,
//	 * Username = confomre passado abaixo -> withUser()
//	 * Password = confomre passado abaixo -> password()
//	 *
//	 * @param auth
//	 * @throws Exception
//	 */
////	@Override
////	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
////		auth.inMemoryAuthentication()// AUTENTICAÇÃO EM memoria
////				.withUser("admin")
////				.password("admin")
////				.roles("ROLE");
////	}
////
////	/**
////	 * Segundo metodo implementado para segurança Basic.
////	 * PASSO 2
////	 * @param http
////	 * @throws Exception
////	 */
////	@Override
////	protected void configure(HttpSecurity http) throws Exception {
////		http.authorizeRequests()
////				.antMatchers("/categorias").permitAll() // Para categorias nao e necessario autenticacao
////				.anyRequest().authenticated() // PARA QUALQUER REQUISICAO E NECESSARIO ESTAR AUTENTICADO
////				.and()
////				.httpBasic()// Tipo de autenticacao basic
////				.and()
////				.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)//Desabilita o uso de sessao(Nao mantem estado)
////				.and()
////				.csrf().disable();//desabilitando o javaScript Ejection
////	}
//
//	/*
//	 * Esta usando a mesma classe de segurança do banco, então aqui na implementação
//	 * basic da segurança, para acessar a api, são os mesmos usuarios cadastrados no banco
//	 */
//    @Override
//    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
//        auth.userDetailsService(userDetailsService).passwordEncoder(passwordEncoder());
//    }
//
//
//    @Override
//    protected void configure(HttpSecurity http) throws Exception {
//		http.authorizeRequests()
//			.anyRequest().authenticated()
//			.and()
//			.httpBasic()
//			.and()
//			.sessionManagement()
//				.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
//			.and()
//			.csrf().disable();
//    }
//
//
//    /*
//     * Criptografia com BCrypt
//     */
//    @Bean
//    public PasswordEncoder passwordEncoder() {
//        return new BCryptPasswordEncoder();
//    }
//
//}
