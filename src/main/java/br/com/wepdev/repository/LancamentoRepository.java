package br.com.wepdev.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import br.com.wepdev.model.Lancamento;

public interface LancamentoRepository extends JpaRepository<Lancamento, Long>{


}
