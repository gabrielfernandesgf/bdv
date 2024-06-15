package br.com.gabrielfernandes.bdv.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import br.com.gabrielfernandes.bdv.model.Pedido;

public interface PedidoRepository extends JpaRepository<Pedido, Long> {
}
