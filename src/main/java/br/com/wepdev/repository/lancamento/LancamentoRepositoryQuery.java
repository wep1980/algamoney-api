package br.com.wepdev.repository.lancamento;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import br.com.wepdev.model.Lancamento;
import br.com.wepdev.repository.filter.LancamentoFilter;
import br.com.wepdev.repository.projection.ResumoLancamento;

/**
 * Interface feita para o end point que busca lancamento por filtro dos campos descricao, dataVencimentoDe,
 * dataVencimentoAte.
 * A Implementação dessa classe foi feita em LancamentoRepositoryImpl
 */
public interface LancamentoRepositoryQuery {
	
	
	public Page<Lancamento> filtrar (LancamentoFilter lancamentoFilter, Pageable pageable);
	
	public Page<ResumoLancamento> resumir (LancamentoFilter lancamentoFilter, Pageable pageable);

}
