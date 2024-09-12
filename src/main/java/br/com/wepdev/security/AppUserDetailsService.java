package br.com.wepdev.security;

import java.util.Collection;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import br.com.wepdev.model.Usuario;
import br.com.wepdev.repository.UsuarioRepository;

/**
 * Classe que contem a implementação da interface UserDetailsService
 * Classe responsavel por buscar usuario no banco de dados
 */
@Service
public class AppUserDetailsService implements UserDetailsService{

	
	@Autowired
	private UsuarioRepository usuarioRepository;
	
	
	@Override
	public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
		
		Optional<Usuario> usuarioOptional = usuarioRepository.findByEmail(email);
		Usuario usuario = usuarioOptional.orElseThrow(() -> new UsernameNotFoundException("Usuário e/ou senha incorretos"));
		
		return new UsuarioSistema(usuario, getPermissoes(usuario));
	}

	/*
	 * Metodo que pega as permissoes do usuario
	 */
	private Collection<? extends GrantedAuthority> getPermissoes(Usuario usuario) {
		
		Set<SimpleGrantedAuthority> autorizacoes = new HashSet<>();

		// para cada permissao e adicionada dentro da lista de autorizacoes
		usuario.getPermissoes().forEach(p -> autorizacoes.add(new SimpleGrantedAuthority(p.getDescricao().toUpperCase())));
		
		return autorizacoes;
	}

}
