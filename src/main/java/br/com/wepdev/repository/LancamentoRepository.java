package br.com.wepdev.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import br.com.wepdev.model.Lancamento;
import br.com.wepdev.repository.lancamento.LancamentoRepositoryQuery;

public interface LancamentoRepository extends JpaRepository<Lancamento, Long>, LancamentoRepositoryQuery{


}
