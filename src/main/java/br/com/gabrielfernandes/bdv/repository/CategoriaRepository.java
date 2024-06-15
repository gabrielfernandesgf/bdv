package br.com.gabrielfernandes.bdv.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import br.com.gabrielfernandes.bdv.model.Categoria;

public interface CategoriaRepository extends JpaRepository<Categoria, Long> {
}
