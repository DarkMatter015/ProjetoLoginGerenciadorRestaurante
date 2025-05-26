package br.app.appLogin.controllers;

import br.app.appLogin.dtos.ProdutoDTO;
import br.app.appLogin.exceptions.CategoriaException;
import br.app.appLogin.exceptions.ProdutoException;
import br.app.appLogin.models.ProdutoModel;
import br.app.appLogin.services.ProdutoService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/produtos")
public class ProdutoController {

    private static final Logger logger = LoggerFactory.getLogger(ProdutoController.class);
    private final ProdutoService produtoService;

    public ProdutoController(ProdutoService produtoService) {
        this.produtoService = produtoService;
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN')")
    public String listarProdutos(Model model) {
        model.addAttribute("produtos", produtoService.listarProdutos());
        model.addAttribute("categorias", produtoService.listarCategorias());
        logger.debug("Produtos loaded: {}", produtoService.listarProdutos());
        return "produto/produtos";
    }

    @GetMapping("/cadastrar")
    @PreAuthorize("hasRole('ADMIN')")
    public String cadastrarProduto(Model model) {
        model.addAttribute("novoProduto", new ProdutoDTO(null, null, null, null, null));
        model.addAttribute("categorias", produtoService.listarCategorias());
        return "produto/cadastroProduto";
    }

    @PostMapping("/cadastrar")
    @PreAuthorize("hasAnyRole('ADMIN')")
    public String cadastrarProduto(@Valid ProdutoDTO produtoDTO, BindingResult erros, Model model, RedirectAttributes attributes) {
        if (erros.hasErrors()) {
            logger.debug("Validation errors: {}", erros.getAllErrors());
            model.addAttribute("produto", produtoDTO);
            model.addAttribute("categorias", produtoService.listarCategorias());
            return "produto/cadastroProduto";
        }
        try {
            produtoService.cadastrarProduto(produtoDTO);
            attributes.addFlashAttribute("msg", "Produto cadastrado com sucesso!");
        } catch (ProdutoException e) {
            logger.error("Error creating produto: {}", e.getMessage());
            erros.rejectValue("nome", "duplicate", e.getMessage());
            model.addAttribute("produto", produtoDTO);
            model.addAttribute("categorias", produtoService.listarCategorias());
            return "produto/cadastroProduto";
        }
        return "redirect:/produtos";
    }

    @GetMapping("/editar/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public String editarProduto(@PathVariable("id") Long id, Model model, RedirectAttributes attributes) {
        if (id <= 0) {
            attributes.addFlashAttribute("error", "ID inválido");
            return "redirect:/produtos";
        }
        try {
            ProdutoModel produto = produtoService.buscarProdutoPorId(id);
            ProdutoDTO produtoDTO = new ProdutoDTO(
                    produto.getId(),
                    produto.getNome(),
                    produto.getPreco(),
                    produto.getDisponivel(),
                    produto.getCategoria().getId()
            );
            model.addAttribute("produto", produtoDTO);
            model.addAttribute("categorias", produtoService.listarCategorias());
            return "produto/editaProduto";
        } catch (ProdutoException e) {
            attributes.addFlashAttribute("error", e.getMessage());
            return "redirect:/produtos";
        }
    }

    @PostMapping("/editar/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public String editarProduto(@PathVariable("id") Long id,
                                  @ModelAttribute("produto") @Valid ProdutoDTO produtoDTO,
                                  BindingResult erros,
                                  Model model,
                                  RedirectAttributes attributes) {

        if (erros.hasErrors()) {
            model.addAttribute("error", erros.getAllErrors());
            return "produto/editaProduto";
        }
        try {
            produtoService.atualizarProduto(id, produtoDTO);
            attributes.addFlashAttribute("msg", "Produto atualizado com sucesso!");
        } catch (ProdutoException e) {
            attributes.addFlashAttribute("error", e.getMessage());
            return "redirect:/produtos";
        } catch (IllegalArgumentException e) {
            model.addAttribute("error", e.getMessage());
            return "produto/editaProduto";
        }
        return "redirect:/produtos";
    }

    @GetMapping("/excluir/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public String excluirProduto(@PathVariable("id") Long id, RedirectAttributes attributes) {
        try {
            produtoService.excluirProdutoPorId(id);
            attributes.addFlashAttribute("msg", "Produto excluída com sucesso!");
        } catch (CategoriaException e) {
            attributes.addFlashAttribute("error", e.getMessage());
        }
        return "redirect:/produtos";
    }
}