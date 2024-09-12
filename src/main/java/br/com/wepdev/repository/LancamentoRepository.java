package br.com.wepdev.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import br.com.wepdev.model.Lancamento;
import br.com.wepdev.repository.lancamento.LancamentoRepositoryQuery;

/**
 * LancamentoRepositoryQuery extend essa classe para os metodos criados nele,
 * poderem ser chamados de LancamentoRepository
 */
public interface LancamentoRepository extends JpaRepository<Lancamento, Long>, LancamentoRepositoryQuery{


}
