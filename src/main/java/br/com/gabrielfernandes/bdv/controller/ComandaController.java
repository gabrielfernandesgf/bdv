package br.com.gabrielfernandes.bdv.controller;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import br.com.gabrielfernandes.bdv.model.Categoria;
import br.com.gabrielfernandes.bdv.model.Produto;
import br.com.gabrielfernandes.bdv.service.ProdutoService;

@Named
@ViewScoped
public class ComandaController implements Serializable {

    private static final long serialVersionUID = 1L;

    @Inject
    private ProdutoService produtoService;

    private List<Categoria> categorias;
    private Long categoriaSelecionadaId;
    private List<Produto> produtosFiltrados;
    private Map<String, List<Produto>> produtosPorCategoria;

    @PostConstruct
    public void init() {
        categorias = produtoService.findAllCategorias();
        produtosPorCategoria = new HashMap<>();
        for (Categoria categoria : categorias) {
            produtosPorCategoria.put(categoria.getNome(), produtoService.findProdutosByCategoria(categoria.getId()));
        }
    }

    public void onCategoriaChange(Long categoriaId) {
        if (categoriaId != null) {
            produtosFiltrados = produtoService.findProdutosByCategoria(categoriaId);
        } else {
            produtosFiltrados = null;
        }
    }

    public List<Categoria> getCategorias() {
        return categorias;
    }

    public Long getCategoriaSelecionadaId() {
        return categoriaSelecionadaId;
    }

    public void setCategoriaSelecionadaId(Long categoriaSelecionadaId) {
        this.categoriaSelecionadaId = categoriaSelecionadaId;
    }

    public List<Produto> getProdutosFiltrados() {
        return produtosFiltrados;
    }

    public Map<String, List<Produto>> getProdutosPorCategoria() {
        return produtosPorCategoria;
    }
}
