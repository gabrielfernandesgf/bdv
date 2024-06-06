package com.gabrielfernandes.bdv.controllers;


import java.io.Serializable;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import com.gabrielfernandes.bdv.model.Mesa;
import com.gabrielfernandes.bdv.service.MesaService;

@Controller
@ViewScoped
public class MesaController implements Serializable {

    private static final long serialVersionUID = 1L;

    @Autowired
    private MesaService mesaService;

    private List<Mesa> mesas;
    private Mesa mesa;

    @PostConstruct
    public void init() {
        mesas = mesaService.listarTodas();
        mesa = new Mesa();

    }

    public void salvar() {
        mesaService.salvar(mesa);
        mesas = mesaService.listarTodas();

    }

    public void deletar(Long id) {
        mesaService.deletar(id);
        mesas = mesaService.listarTodas();
    }

    public List<Mesa> getMesas() {
        return mesas;
    }

    public Mesa getMesa() {
        return mesa;
    }

    public void setMesa(Mesa mesa) {
        this.mesa = mesa;
    }

}
