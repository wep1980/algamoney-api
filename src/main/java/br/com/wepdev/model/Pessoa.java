package br.com.wepdev.model;

import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Data;

@Data
@Entity
@Table(name = "pessoa")
public class Pessoa {

	
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long codigo;

    @NotNull
    private String nome;

    @Embedded
    private Endereco endereco;

    @NotNull
    private Boolean ativo;

    
    /*
     * Metodo que verica se pessoa esta inativa
     */
    @JsonIgnore // Como o Jackson ou Hibernate entendem esse metodo como propriedade, eles vão tentar serializar e buscar , por isso foi colocada essa anotação.
    @Transient // Como o Hibernate ou Jackson entendem esse metodo como propriedade, eles vão tentar buscar e serializar, por isso foi colocada essa anotação.
	public boolean isInativo() {
		
		return !this.ativo;
	}
    
}
