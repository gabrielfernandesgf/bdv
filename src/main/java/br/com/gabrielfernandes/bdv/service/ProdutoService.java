package br.com.gabrielfernandes.bdv.service;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.gabrielfernandes.bdv.model.Categoria;
import br.com.gabrielfernandes.bdv.model.Produto;
import br.com.gabrielfernandes.bdv.repository.ProdutoRepository;

@Service
public class ProdutoService {

    @PersistenceContext
    private EntityManager em;

    private final ProdutoRepository produtoRepository;

    public ProdutoService(ProdutoRepository produtoRepository) {
        this.produtoRepository = produtoRepository;
    }

    @Transactional
    public void save(Produto produto) {
        produtoRepository.save(produto);
    }

    public List<Produto> findAll() {
        return produtoRepository.findAll();
    }

    public Produto findById(Long id) {
        return produtoRepository.findById(id).orElse(null);
    }

    public List<Categoria> findAllCategorias() {
        return em.createQuery("SELECT c FROM Categoria c", Categoria.class).getResultList();
    }

    public List<Produto> findProdutosByCategoria(Long categoriaId) {
        return em.createQuery("SELECT p FROM Produto p WHERE p.categoria.id = :categoriaId", Produto.class)
                 .setParameter("categoriaId", categoriaId)
                 .getResultList();
    }
}
