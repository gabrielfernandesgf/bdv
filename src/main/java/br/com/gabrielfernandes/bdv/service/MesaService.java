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

    public void save(Mesa mesa) {
        mesaRepository.save(mesa);
    }

    public List<Mesa> findAll() {
        return mesaRepository.findAll();
    }
}
