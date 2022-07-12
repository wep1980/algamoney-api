package br.com.wepdev.model;

import java.math.BigDecimal;
import java.time.LocalDate;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.Data;

@Data
@Entity
@Table(name = "Lancamento")
public class Lancamento {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long codigo;
	private String descricao;
	
	@Column(name = "data_vencimento")
	@JsonFormat(pattern =  "dd/MM/yyyy")
	private LocalDate dataVencimento;
	
	@Column(name = "data_pagamento")
	@JsonFormat(pattern =  "dd/MM/yyyy")
	private LocalDate dataPagamento;
	
	private BigDecimal valor;
	
	private String observacao;
	
	@Enumerated(EnumType.STRING)
	private TipoLancamento tipo;
	
	@ManyToOne // Muitos lançamentos tem uma categoria
	@JoinColumn(name = "codigo_categoria")
	private Categoria categoria;
	
	@ManyToOne // Muitos lançamentos tem uma pessoa
	@JoinColumn(name = "codigo_pessoa")
	private Pessoa pessoa;
	
}
