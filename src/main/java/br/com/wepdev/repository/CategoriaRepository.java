package br.com.wepdev.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import br.com.wepdev.model.Categoria;

public interface CategoriaRepository extends JpaRepository<Categoria, Long>{

}
