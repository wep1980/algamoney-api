package br.com.wepdev.service;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;

import br.com.wepdev.model.Pessoa;
import br.com.wepdev.repository.PessoaRepository;

@Service
public class PessoaService {

	@Autowired
	private PessoaRepository pessoaRepository;
	
	
	public Pessoa atualizar(Long codigo, Pessoa pessoa) {
		
		  Pessoa pessoaSalva = buscarPessoaPeloCodigo(codigo);
		  
		// Copia os dados vindos da requisição "pessoa" e coloca no objeto pessoaSalva, iguinorando na copia o codigo(ID)
		  BeanUtils.copyProperties(pessoa, pessoaSalva, "codigo");

		  return pessoaRepository.save(pessoaSalva);
		}
	
	
	public void atualizarPropriedadeAtivo(Long codigo, Boolean ativo) {
		
		Pessoa pessoaSalva = pessoaRepository.findById(codigo)
				.orElseThrow(() -> new EmptyResultDataAccessException(1));
		
		pessoaSalva.setAtivo(ativo);
		pessoaRepository.save(pessoaSalva);
	}
	
	
	public Pessoa buscarPessoaPeloCodigo(Long codigo) {
		
		Pessoa pessoaSalva = pessoaRepository.findById(codigo)
				.orElseThrow(() -> new EmptyResultDataAccessException(1));

		return pessoaSalva;
	}




	
}
