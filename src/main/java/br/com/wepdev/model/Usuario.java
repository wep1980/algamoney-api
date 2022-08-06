package br.com.wepdev.model;

import java.util.List;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Getter
@Setter
@Entity
@Table(name = "usuario")
public class Usuario {

	@EqualsAndHashCode.Include
	@Id
	private Long codigo;
	
	private String nome;
	private String email;
	private String senha;
	
	@ManyToMany(fetch = FetchType.EAGER) // Toda vez que buscar os usuarios ja traz junto as permissoes
	@JoinTable(name = "usuario_permissao", joinColumns = @JoinColumn(name = "codigo_usuario")
	        , inverseJoinColumns = @JoinColumn(name = "codigo_permissao"))
	private List<Permissao> permissoes;
}
