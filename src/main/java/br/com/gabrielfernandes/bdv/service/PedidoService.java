package br.com.gabrielfernandes.bdv.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.gabrielfernandes.bdv.model.Mesa;
import br.com.gabrielfernandes.bdv.model.Pedido;
import br.com.gabrielfernandes.bdv.model.Produto;
import br.com.gabrielfernandes.bdv.repository.PedidoRepository;
import br.com.gabrielfernandes.bdv.repository.ProdutoRepository;

@Service
public class PedidoService {

    @Autowired
    private PedidoRepository pedidoRepository;

    @Autowired
    private ProdutoRepository produtoRepository; 

    @Transactional
    public void save(Pedido pedido) {
        pedidoRepository.save(pedido);
    }

    public List<Pedido> findAll() {
        return pedidoRepository.findAll();
    }

    public Pedido findById(Long id) {
        return pedidoRepository.findById(id).orElse(null);
    }

    public List<Pedido> findByMesaAndStatus(Mesa mesa, Pedido.Status status) {
        return pedidoRepository.findByMesaAndStatus(mesa, status);
    }

    public Produto findProdutoById(Long id) {
        return produtoRepository.findById(id).orElse(null);
    }
}
