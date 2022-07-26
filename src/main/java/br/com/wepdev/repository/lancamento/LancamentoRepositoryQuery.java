package br.com.wepdev.repository.lancamento;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import br.com.wepdev.model.Lancamento;
import br.com.wepdev.repository.filter.LancamentoFilter;

public interface LancamentoRepositoryQuery {
	
	
	public Page<Lancamento> filtrar(LancamentoFilter lancamentoFilter, Pageable pageable);

}
