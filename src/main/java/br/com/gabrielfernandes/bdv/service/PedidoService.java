package br.com.gabrielfernandes.bdv.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.gabrielfernandes.bdv.model.Pedido;
import br.com.gabrielfernandes.bdv.model.Produto;
import br.com.gabrielfernandes.bdv.repository.PedidoRepository;

@Service
public class PedidoService {

    @Autowired
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

    public void addProduto(Long pedidoId, Produto produto) {
        Pedido pedido = findById(pedidoId);
        if (pedido != null) {
            pedido.getProdutos().add(produto);
            save(pedido);
        }
    }

    public void fecharPedido(Long pedidoId) {
        Pedido pedido = findById(pedidoId);
        if (pedido != null) {
            pedido.setStatus("Fechado");
            save(pedido);
        }
    }

    public void cancelarPedido(Long pedidoId) {
        Pedido pedido = findById(pedidoId);
        if (pedido != null) {
            pedido.setStatus("Cancelado");
            save(pedido);
        }
    }
}
