package br.com.gabrielfernandes.bdv.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.gabrielfernandes.bdv.model.Mesa;
import br.com.gabrielfernandes.bdv.repository.MesaRepository;

@Service
public class MesaService {

    private final MesaRepository mesaRepository;

    
    public MesaService(MesaRepository mesaRepository) {
        this.mesaRepository = mesaRepository;
    }

    @Transactional
    public Mesa save(Mesa mesa) {
        System.out.println("Persistindo mesa no banco de dados: " + mesa);
        return mesaRepository.save(mesa);
    }

    public List<Mesa> findAll() {
        return mesaRepository.findAll();
    }
}
