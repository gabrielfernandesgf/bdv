package br.com.gabrielfernandes.bdv.service;

import java.util.List;

import javax.inject.Named;

import org.springframework.beans.factory.annotation.Autowired;

import br.com.gabrielfernandes.bdv.model.Pedido;
import br.com.gabrielfernandes.bdv.repository.PedidoRepository;

@Named
public class PedidoService {

    @Autowired
    private PedidoRepository pedidoRepository;

    public Pedido save(Pedido pedido) {
        System.out.println("Persistindo pedido no banco de dados: " + pedido);
        return pedidoRepository.save(pedido);
    }

    public List<Pedido> findAll() {
        return pedidoRepository.findAll();
    }

    public Pedido findById(Long id) {
        return pedidoRepository.findById(id).orElse(null);
    }

    
    public void deleteById(Long id) {
        pedidoRepository.deleteById(id);
    }
}
