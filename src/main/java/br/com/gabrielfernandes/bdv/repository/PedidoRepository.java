package br.com.gabrielfernandes.bdv.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import br.com.gabrielfernandes.bdv.model.Mesa;
import br.com.gabrielfernandes.bdv.model.Pedido;

public interface PedidoRepository extends JpaRepository<Pedido, Long> {
    List<Pedido> findByMesaAndStatus(Mesa mesa, Pedido.Status status);
}
