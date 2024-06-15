package br.com.gabrielfernandes.bdv.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.gabrielfernandes.bdv.model.ItemPedido;
import br.com.gabrielfernandes.bdv.repository.ProdutoPedidoRepository;

@Service
public class ProdutoPedidoService {

    @Autowired
    private ProdutoPedidoRepository produtoPedidoRepository;

    public void save(ItemPedido produtoPedido) {
        produtoPedidoRepository.save(produtoPedido);
    }
}
