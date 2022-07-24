package br.com.wepdev.repository.lancamento;

import java.util.List;

import br.com.wepdev.model.Lancamento;
import br.com.wepdev.repository.filter.LancamentoFilter;

public interface LancamentoRepositoryQuery {
	
	
	public List<Lancamento> filtrar(LancamentoFilter lancamentoFilter);

}
