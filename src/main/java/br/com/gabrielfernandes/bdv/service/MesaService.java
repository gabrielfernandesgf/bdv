package br.com.gabrielfernandes.bdv.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.gabrielfernandes.bdv.model.Mesa;
import br.com.gabrielfernandes.bdv.repository.MesaRepository;

@Service
public class MesaService {

    @Autowired
    private MesaRepository mesaRepository;

    public Mesa save(Mesa mesa) {
        System.out.println("Persistindo mesa no banco de dados: " + mesa);
        return mesaRepository.save(mesa);
    }

    public List<Mesa> findAll() {
        return mesaRepository.findAll();
    }

    
}
