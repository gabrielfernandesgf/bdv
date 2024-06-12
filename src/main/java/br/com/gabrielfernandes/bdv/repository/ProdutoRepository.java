package br.com.gabrielfernandes.bdv.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import br.com.gabrielfernandes.bdv.model.Produto;


@Repository
public interface ProdutoRepository extends JpaRepository <Produto, Long> {
}
