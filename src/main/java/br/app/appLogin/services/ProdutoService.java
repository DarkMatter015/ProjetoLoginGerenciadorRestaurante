package br.app.appLogin.services;

import br.app.appLogin.dtos.ProdutoDTO;
import br.app.appLogin.exceptions.CategoriaException;
import br.app.appLogin.exceptions.ProdutoException;
import br.app.appLogin.models.CategoriaModel;
import br.app.appLogin.models.ProdutoModel;
import br.app.appLogin.repositories.CategoriaRepository;
import br.app.appLogin.repositories.ProdutoRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProdutoService {

    private static final Logger logger = LoggerFactory.getLogger(CategoriaService.class);
    private ProdutoRepository produtoRepository;
    private CategoriaRepository categoriaRepository;

    public ProdutoService(ProdutoRepository produtoRepository, CategoriaRepository categoriaRepository) {
        this.produtoRepository = produtoRepository;
        this.categoriaRepository = categoriaRepository;
    }

    public ProdutoModel buscarProdutoPorId(Long id) throws CategoriaException {
        logger.info("Buscando produto com ID: {}", id);
        return produtoRepository.findById(id)
                .orElseThrow(() -> new ProdutoException("Produto com ID: " + id + " não encontrado!"));
    }

    @Transactional
    public void cadastrarProduto(ProdutoDTO produtoDTO) {
        if (produtoDTO == null || produtoDTO.nome() == null || produtoDTO.preco() == null ||
                produtoDTO.disponivel() == null || produtoDTO.categoriaId() == null) {
            throw new ProdutoException("Dados do produto são obrigatórios");
        }
        if (produtoRepository.existsByNome(produtoDTO.nome())) {
            throw new ProdutoException("Nome do produto já existe");
        }
        CategoriaModel categoria = categoriaRepository.findById(produtoDTO.categoriaId())
                .orElseThrow(() -> new ProdutoException("Categoria não encontrada com ID: " + produtoDTO.categoriaId()));
        ProdutoModel produto = new ProdutoModel();
        produto.setNome(produtoDTO.nome());
        produto.setPreco(produtoDTO.preco());
        produto.setDisponivel(produtoDTO.disponivel());
        produto.setCategoria(categoria);
        produtoRepository.save(produto);
    }

    @Transactional(readOnly = true)
    public List<ProdutoDTO> listarProdutos() {
        return produtoRepository.findAll().stream()
                .map(p -> new ProdutoDTO(
                        p.getId(),
                        p.getNome(),
                        p.getPreco(),
                        p.getDisponivel(),
                        p.getCategoria().getId()
                ))
                .collect(Collectors.toList());
    }

    @Transactional
    public void atualizarProduto(Long id, ProdutoDTO produtoDTO) {
        ProdutoModel produto = produtoRepository.findById(id)
                .orElseThrow(() -> new ProdutoException("Produto não encontrado com ID: " + id));
        if (!produto.getNome().equals(produtoDTO.nome()) && produtoRepository.existsByNome(produtoDTO.nome())) {
            throw new ProdutoException("Nome do produto já existe");
        }
        CategoriaModel categoria = categoriaRepository.findById(produtoDTO.categoriaId())
                .orElseThrow(() -> new ProdutoException("Categoria não encontrada com ID: " + produtoDTO.categoriaId()));
        produto.setNome(produtoDTO.nome());
        produto.setPreco(produtoDTO.preco());
        produto.setDisponivel(produtoDTO.disponivel());
        produto.setCategoria(categoria);
        produtoRepository.save(produto);
    }

    @Transactional
    public void excluirProdutoPorId(Long id) {
        if (!produtoRepository.existsById(id)) {
            throw new ProdutoException("Produto não encontrado com ID: " + id);
        }
        produtoRepository.deleteById(id);
    }

    @Transactional(readOnly = true)
    public List<CategoriaModel> listarCategorias() {
        return categoriaRepository.findAll();
    }

    @Transactional(readOnly = true)
    public long countByCategoriaId(Long categoriaId) {
        return produtoRepository.countByCategoriaId(categoriaId);
    }
}