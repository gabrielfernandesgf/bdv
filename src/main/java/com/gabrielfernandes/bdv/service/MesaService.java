package com.gabrielfernandes.bdv.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.gabrielfernandes.bdv.model.Mesa;
import com.gabrielfernandes.bdv.repository.MesaRepository;

@Service
public class MesaService {


    @Autowired
    private MesaRepository mesaRepository;

    public List<Mesa> listarTodas() {
        return mesaRepository.findAll();
    }

    public Mesa salvar(Mesa mesa) {
        return mesaRepository.save(mesa);
    }

    public void deletar(Long id) {
        mesaRepository.deleteById(id);
    }

    public Mesa buscarPorId(Long id) {
        return mesaRepository.findById(id).orElse(null);
    }

}
