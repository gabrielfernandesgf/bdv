package br.com.gabrielfernandes.bdv.controller;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.faces.bean.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import br.com.gabrielfernandes.bdv.model.Categoria;
import br.com.gabrielfernandes.bdv.model.Produto;
import br.com.gabrielfernandes.bdv.service.ProdutoService;

@Named
@ViewScoped
public class ComandaController implements Serializable {

    @Inject
    private ProdutoService produtoService;

    private List<Categoria> categorias;
    private Map<Categoria, List<Produto>> produtosPorCategoria;

    @PostConstruct
    public void init() {
        categorias = produtoService.findAllCategorias();
        produtosPorCategoria = new HashMap<>();
        for (Categoria categoria : categorias) {
            produtosPorCategoria.put(categoria, produtoService.findProdutosByCategoria(categoria));
        }
    }

    public List<Categoria> getCategorias() {
        return categorias;
    }

    public Map<Categoria, List<Produto>> getProdutosPorCategoria() {
        return produtosPorCategoria;
    }

    public void adicionarItem(Produto produto) {
        // lógica para adicionar item ao pedido
    }
}

