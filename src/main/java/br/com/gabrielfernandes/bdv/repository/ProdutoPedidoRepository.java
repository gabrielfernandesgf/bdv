package br.com.gabrielfernandes.bdv.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import br.com.gabrielfernandes.bdv.model.ItemPedido;

public interface ProdutoPedidoRepository extends JpaRepository<ItemPedido, Long> {
}
