package br.com.wepdev.config;

import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;

@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter{
	
	
	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.inMemoryAuthentication() // Autenticação basica feita em memoria
        .withUser("admin").password("{noop}admin").roles("ROLE"); // autenticação e autorização, por enquanto semente a autorização sera implementada. Declarando o encoder direto na senha password("{noop}admin")
		
	}
	
	/**
	 * Conbfiguração de autorização das requisições
	 */
	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http.authorizeRequests()
		.antMatchers("/categorias").permitAll() // Ta liberado o acesso para URLs de categoria sem precisar estar autenticado
		.anyRequest().authenticated()
		.and()
		.httpBasic()
		.and()
		.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS) // Nessa APi não tera sessão de nada e não mantem estado de nada(sem estado)
		.and()
		.csrf().disable(); // Não permite o JavaScript injection dentro da APi, pois ainda não temos a parte WEB na aplicação
	}

}
