package br.app.appLogin.controllers;

import br.app.appLogin.dtos.CategoriaDTO;
import br.app.appLogin.exceptions.CategoriaException;
import br.app.appLogin.models.CategoriaModel;
import br.app.appLogin.services.CategoriaService;
import jakarta.validation.Valid;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/categorias")
public class CategoriaController {

    private final CategoriaService categoriaService;

    public CategoriaController(CategoriaService categoriaService) {
        this.categoriaService = categoriaService;
    }

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public String listarCategorias(Model model) {
        model.addAttribute("categorias", categoriaService.listarCategorias());
        return "categoria/categorias";
    }

    @GetMapping("/cadastrar")
    @PreAuthorize("hasRole('ADMIN')")
    public String cadastrarCategoria(Model model) {
        model.addAttribute("novaCategoria", new CategoriaDTO(null, null, null));
        return "categoria/cadastroCategoria";
    }

    @PostMapping("/cadastrar")
    @PreAuthorize("hasRole('ADMIN')")
    public String cadastrarCategoria(@ModelAttribute("novaCategoria") @Valid CategoriaDTO categoriaDTO, 
                                     BindingResult erros,
                                     Model model,
                                     RedirectAttributes attributes) {

        if (erros.hasErrors()) {
            model.addAttribute("error", erros.getAllErrors());
            return "categoria/cadastroCategoria";
        }
        try {
            categoriaService.cadastrarCategoria(categoriaDTO);
            attributes.addFlashAttribute("msg", "Categoria cadastrada com sucesso!");
        } catch (RuntimeException e) {
            model.addAttribute("error", e.getMessage());
            return "categoria/cadastroCategoria";
        }
        return "redirect:/categorias";
    }

    @GetMapping("/editar/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public String editarCategoria(@PathVariable("id") Long id, Model model, RedirectAttributes attributes) {
        if (id <= 0) {
            attributes.addFlashAttribute("error", "ID inválido");
            return "redirect:/categorias";
        }
        try {
            CategoriaModel categoria = categoriaService.buscarCategoriaPorId(id);
            CategoriaDTO categoriaDTO = new CategoriaDTO(
                    categoria.getId(),
                    categoria.getNome(),
                    null
            );
            model.addAttribute("categoria", categoriaDTO);
            model.addAttribute("categorias", categoriaService.listarCategorias());
            return "categoria/editaCategoria";
        } catch (CategoriaException e) {
            attributes.addFlashAttribute("error", e.getMessage());
            return "redirect:/categorias";
        }
    }

    @PostMapping("/editar/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public String editarCategoria(@PathVariable("id") Long id,
                                  @ModelAttribute("categoria") @Valid CategoriaDTO categoriaDTO,
                                  BindingResult erros,
                                  Model model,
                                  RedirectAttributes attributes) {

        if (erros.hasErrors()) {
            model.addAttribute("error", erros.getAllErrors());
            return "categoria/editaCategoria";
        }
        try {
            categoriaService.atualizarCategoria(id, categoriaDTO);
            attributes.addFlashAttribute("msg", "Categoria atualizada com sucesso!");
        } catch (CategoriaException e) {
            attributes.addFlashAttribute("error", e.getMessage());
            return "redirect:/categorias";
        } catch (IllegalArgumentException e) {
            model.addAttribute("error", e.getMessage());
            return "categoria/editaCategoria";
        }
        return "redirect:/categorias";
    }

    @GetMapping("/excluir/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public String excluirCategoria(@PathVariable("id") Long id, RedirectAttributes attributes) {
        try {
            categoriaService.excluirCategoriaPorId(id);
            attributes.addFlashAttribute("msg", "Categoria excluída com sucesso!");
        } catch (CategoriaException e) {
            attributes.addFlashAttribute("error", e.getMessage());
        }
        return "redirect:/categorias";
    }
}