package br.app.appLogin.services;

import br.app.appLogin.dtos.CategoriaDTO;
import br.app.appLogin.exceptions.CategoriaException;
import br.app.appLogin.models.CategoriaModel;
import br.app.appLogin.repositories.CategoriaRepository;
import br.app.appLogin.repositories.ProdutoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class CategoriaService {

    @Autowired
    private CategoriaRepository categoriaRepository;

    @Autowired
    private ProdutoRepository produtoRepository;

    @Transactional
    public void criarCategoria(CategoriaDTO categoriaDTO) {
        if (categoriaDTO == null || categoriaDTO.nome() == null) {
            throw new CategoriaException("O nome da categoria é obrigatório");
        }
        if (categoriaRepository.existsByNome(categoriaDTO.nome())) {
            throw new CategoriaException("Nome da categoria já existe");
        }
        CategoriaModel categoria = new CategoriaModel();
        categoria.setNome(categoriaDTO.nome());
        categoriaRepository.save(categoria);
    }

    @Transactional(readOnly = true)
    public List<CategoriaDTO> listarCategorias() {
        List<CategoriaModel> categorias = categoriaRepository.findAll();
        // Fetch product counts for all categories in one query
        List<Map<String, Object>> rawCounts = produtoRepository.countCategoriasByProduct();
        // Convert List<Map<String, Object>> to Map<Long, Long>
        Map<Long, Long> productCounts = rawCounts.stream()
                .collect(Collectors.toMap(
                        map -> ((Number) map.get("categoriaId")).longValue(),
                        map -> ((Number) map.get("count")).longValue(),
                        (v1, v2) -> v1 // Merge function (use first value if duplicate)
                ));
        return categorias.stream()
                .map(c -> new CategoriaDTO(
                        c.getId(),
                        c.getNome()
                ))
                .collect(Collectors.toList());
    }

    @Transactional
    public void atualizarCategoria(Long id, CategoriaDTO categoriaDTO) {
        CategoriaModel categoria = categoriaRepository.findById(id)
                .orElseThrow(() -> new CategoriaException("Categoria não encontrada com ID: " + id));
        if (!categoria.getNome().equals(categoriaDTO.nome()) && categoriaRepository.existsByNome(categoriaDTO.nome())) {
            throw new CategoriaException("Nome da categoria já existe");
        }
        categoria.setNome(categoriaDTO.nome());
        categoriaRepository.save(categoria);
    }

    @Transactional
    public void excluirCategoria(Long id) {
        CategoriaModel categoria = categoriaRepository.findById(id)
                .orElseThrow(() -> new CategoriaException("Categoria não encontrada com ID: " + id));
        long productCount = produtoRepository.countByCategoriaId(id);
        if (productCount > 0) {
            throw new CategoriaException("Não é possível excluir a categoria pois ela está vinculada a " + productCount + " produto(s)");
        }
        categoriaRepository.deleteById(id);
    }
}