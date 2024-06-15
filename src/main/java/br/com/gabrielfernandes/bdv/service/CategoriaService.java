package br.com.gabrielfernandes.bdv.service;

import org.springframework.stereotype.Service;
import br.com.gabrielfernandes.bdv.model.Categoria;
import br.com.gabrielfernandes.bdv.repository.CategoriaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import java.util.List;

@Service
public class CategoriaService {

    @Autowired
    private CategoriaRepository categoriaRepository;

    public List<Categoria> getAllCategorias() {
        return categoriaRepository.findAll();
    }

    public Categoria getCategoriaById(Long id) {
        return categoriaRepository.findById(id).orElse(null);
    }

    public Categoria saveCategoria(Categoria categoria) {
        return categoriaRepository.save(categoria);
    }

    public void deleteCategoria(Long id) {
        categoriaRepository.deleteById(id);
    }
}
