package br.com.wepdev.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import br.com.wepdev.model.Pessoa;

public interface PessoaRepository extends JpaRepository<Pessoa, Long>{

}
