package br.app.appLogin.controllers;

import br.app.appLogin.dtos.ProdutoDTO;
import br.app.appLogin.exceptions.ProdutoException;
import br.app.appLogin.services.ProdutoService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/produtos")
public class ProdutoController {

    private static final Logger logger = LoggerFactory.getLogger(ProdutoController.class);

    @Autowired
    private ProdutoService produtoService;

    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'WAITER')")
    public String showProdutoForm(Model model) {
        if (!model.containsAttribute("produto")) {
            model.addAttribute("produto", new ProdutoDTO(null, null, null, null, null));
        }
        model.addAttribute("produtos", produtoService.listarProdutos());
        model.addAttribute("categorias", produtoService.listarCategorias());
        logger.debug("Produtos loaded: {}", produtoService.listarProdutos());
        return "produtos";
    }

    @PostMapping("/cadastrar")
    @PreAuthorize("hasAnyRole('ADMIN', 'WAITER')")
    public String cadastrarProduto(@Valid ProdutoDTO produtoDTO, BindingResult result, Model model, RedirectAttributes attributes) {
        if (result.hasErrors()) {
            logger.debug("Validation errors: {}", result.getAllErrors());
            model.addAttribute("produto", produtoDTO);
            model.addAttribute("produtos", produtoService.listarProdutos());
            model.addAttribute("categorias", produtoService.listarCategorias());
            return "produtos";
        }
        try {
            produtoService.criarProduto(produtoDTO);
            attributes.addFlashAttribute("msg", "Produto cadastrado com sucesso!");
        } catch (ProdutoException e) {
            logger.error("Error creating produto: {}", e.getMessage());
            result.rejectValue("nome", "duplicate", e.getMessage());
            model.addAttribute("produto", produtoDTO);
            model.addAttribute("produtos", produtoService.listarProdutos());
            model.addAttribute("categorias", produtoService.listarCategorias());
            return "produtos";
        }
        return "redirect:/produtos";
    }
}