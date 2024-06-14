package br.com.gabrielfernandes.bdv.service;

import java.util.List;
import java.util.Optional;

import javax.inject.Inject;

import org.springframework.stereotype.Service;

import br.com.gabrielfernandes.bdv.model.Pedido;
import br.com.gabrielfernandes.bdv.model.ProdutoPedido;
import br.com.gabrielfernandes.bdv.repository.PedidoRepository;

@Service
public class PedidoService {

    @Inject
    private PedidoRepository pedidoRepository;

    public void save(Pedido pedido) {
        pedido.calcularTotal();
        pedidoRepository.save(pedido);
    }

    public List<Pedido> findAll() {
        return pedidoRepository.findAll();
    }

    public Pedido findById(Long id) {
        return pedidoRepository.findById(id).orElse(null);
    }

    public void addProduto(Long pedidoId, ProdutoPedido produtoPedido) {
        Pedido pedido = findById(pedidoId);
        if (pedido != null) {
            pedido.getProdutos().add(produtoPedido);
            save(pedido);
        }
    }

    public void fecharPedido(Long pedidoId) {
        Pedido pedido = findById(pedidoId);
        if (pedido != null) {
            pedido.setStatus(Pedido.Status.FINALIZADA);
            save(pedido);
        }
    }

    public void cancelarPedido(Long pedidoId) {
        Pedido pedido = findById(pedidoId);
        if (pedido != null) {
            pedido.setStatus(Pedido.Status.CANCELADO);
            save(pedido);
        }
    }

    public Optional<Pedido> findByMesaId(Long mesaId) {
        return pedidoRepository.findById(mesaId);
    }
}
