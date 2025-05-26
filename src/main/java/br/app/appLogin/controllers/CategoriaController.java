package br.app.appLogin.controllers;

import br.app.appLogin.dtos.CategoriaDTO;
import br.app.appLogin.exceptions.CategoriaException;
import br.app.appLogin.services.CategoriaService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/categorias")
public class CategoriaController {

    private static final Logger logger = LoggerFactory.getLogger(CategoriaController.class);

    @Autowired
    private CategoriaService categoriaService;

    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'WAITER')")
    public String showCategoriaForm(Model model) {
        if (!model.containsAttribute("categoria")) {
            model.addAttribute("categoria", new CategoriaDTO(null, null));
        }
        model.addAttribute("categoria", new CategoriaDTO(null, null));
        model.addAttribute("categorias", categoriaService.listarCategorias());
        logger.debug("Categorias loaded: {}", categoriaService.listarCategorias());
        return "categorias";
    }

    @PostMapping("/cadastrar")
    @PreAuthorize("hasAnyRole('ADMIN', 'WAITER')")
    public String cadastrarCategoria(@Valid CategoriaDTO categoriaDTO, BindingResult result, Model model, RedirectAttributes attributes) {
        if (result.hasErrors()) {
            logger.debug("Validation errors: {}", result.getAllErrors());
            model.addAttribute("categoria", categoriaDTO);
            model.addAttribute("categorias", categoriaService.listarCategorias());
            return "categorias";
        }
        try {
            categoriaService.criarCategoria(categoriaDTO);
            attributes.addFlashAttribute("msg", "Categoria cadastrada com sucesso!");
        } catch (CategoriaException e) {
            logger.error("Error creating categoria: {}", e.getMessage());
            result.rejectValue("nome", "duplicate", e.getMessage());
            model.addAttribute("categoria", categoriaDTO);
            model.addAttribute("categorias", categoriaService.listarCategorias());
            return "categorias";
        }
        return "redirect:/categorias";
    }

    @GetMapping("/editar/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'WAITER')")
    public String editarCategoria(@PathVariable Long id, Model model, RedirectAttributes attributes) {
        try {
            CategoriaDTO categoria = categoriaService.listarCategorias().stream()
                    .filter(c -> c.id().equals(id))
                    .findFirst()
                    .orElseThrow(() -> new CategoriaException("Categoria não encontrada com ID: " + id));
            model.addAttribute("categoria", categoria);
            model.addAttribute("categorias", categoriaService.listarCategorias());
            return "categoria/cadastroCategoria";
        } catch (CategoriaException e) {
            logger.error("Error loading categoria: {}", e.getMessage());
            attributes.addFlashAttribute("error", e.getMessage());
            return "redirect:/categorias";
        }
    }

    @PostMapping("/editar/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'WAITER')")
    public String salvarEdicaoCategoria(@PathVariable Long id, @Valid CategoriaDTO categoriaDTO, BindingResult result, Model model, RedirectAttributes attributes) {
        if (result.hasErrors()) {
            logger.debug("Validation errors: {}", result.getAllErrors());
            model.addAttribute("categoria", categoriaDTO);
            model.addAttribute("categorias", categoriaService.listarCategorias());
            return "categorias";
        }
        try {
            categoriaService.atualizarCategoria(id, categoriaDTO);
            attributes.addFlashAttribute("msg", "Categoria atualizada com sucesso!");
        } catch (CategoriaException e) {
            logger.error("Error updating categoria: {}", e.getMessage());
            result.rejectValue("nome", "duplicate", e.getMessage());
            model.addAttribute("categoria", categoriaDTO);
            model.addAttribute("categorias", categoriaService.listarCategorias());
            return "categorias";
        }
        return "redirect:/categorias";
    }

    @PostMapping("/excluir/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'WAITER')")
    public String excluirCategoria(@PathVariable Long id, RedirectAttributes attributes) {
        try {
            categoriaService.excluirCategoria(id);
            attributes.addFlashAttribute("msg", "Categoria excluída com sucesso!");
        } catch (CategoriaException e) {
            logger.error("Error deleting categoria: {}", e.getMessage());
            attributes.addFlashAttribute("error", e.getMessage());
        }
        return "redirect:/categorias";
    }
}