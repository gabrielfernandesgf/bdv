package com.gabrielfernandes.bdv.service;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.mockito.MockitoAnnotations;

import br.com.gabrielfernandes.bdv.model.Produto;
import br.com.gabrielfernandes.bdv.repository.ProdutoRepository;
import br.com.gabrielfernandes.bdv.service.ProdutoService;

public class ProdutoServiceTest {

    @InjectMocks
    private ProdutoService produtoService;

    @Mock
    private ProdutoRepository produtoRepository;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testFindAll() {
        Produto produto = new Produto();
        produto.setNome("Produto Teste");
        when(produtoRepository.findAll()).thenReturn(Arrays.asList(produto));

        List<Produto> produtos = produtoService.findAll();
        assertNotNull(produtos);
        assertFalse(produtos.isEmpty());
        assertEquals("Produto Teste", produtos.get(0).getNome());
    }

    @Test
    public void testFindById() {
        Produto produto = new Produto();
        produto.setId(1L);
        produto.setNome("Produto Teste");
        when(produtoRepository.findById(1L)).thenReturn(java.util.Optional.of(produto));

        Produto found = produtoService.findById(1L);
        assertNotNull(found);
        assertEquals(1L, found.getId());
        assertEquals("Produto Teste", found.getNome());
    }

    @Test
    public void testSave() {
        Produto produto = new Produto();
        produto.setNome("Produto Teste");
        produtoService.save(produto);

        verify(produtoRepository, times(1)).save(produto);
    }
}
