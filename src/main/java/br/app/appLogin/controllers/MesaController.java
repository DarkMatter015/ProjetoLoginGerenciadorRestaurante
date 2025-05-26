package br.app.appLogin.controllers;

import br.app.appLogin.dtos.MesaDTO;
import br.app.appLogin.exceptions.MesaException;
import br.app.appLogin.services.MesaService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/mesas")
public class MesaController {

    private static final Logger logger = LoggerFactory.getLogger(MesaController.class);
    private final MesaService mesaService;

    public MesaController(MesaService mesaService) {
        this.mesaService = mesaService;
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'WAITER')")
    public String listarMesas(Model model) {
        if (!model.containsAttribute("mesa")) {
            model.addAttribute("mesa", new MesaDTO(null, null, null, null));
        }
        model.addAttribute("mesas", mesaService.listarMesas());
        logger.debug("Mesas loaded: {}", mesaService.listarMesas());
        return "index";
    }

    @PostMapping("/cadastrar")
    @PreAuthorize("hasRole('ADMIN')")
    public String cadastrarMesa(@Valid MesaDTO mesaDTO,
                                BindingResult erros,
                                Model model,
                                RedirectAttributes attributes) {

        if (erros.hasErrors()) {
            logger.debug("Validation errors: {}", erros.getAllErrors());
            model.addAttribute("error", erros.getAllErrors());
            model.addAttribute("mesa", mesaDTO);
            model.addAttribute("mesas", mesaService.listarMesas());
            return "index";
        }
        try {
            mesaService.cadastrarMesa(mesaDTO);
            attributes.addFlashAttribute("msg", "Mesa cadastrada com sucesso!");
        } catch (MesaException e) {
            logger.error("Error creating mesa: {}", e.getMessage());
            erros.rejectValue("numero", "duplicate", e.getMessage());

            model.addAttribute("error", e.getMessage());
            model.addAttribute("mesa", mesaDTO);
            model.addAttribute("mesas", mesaService.listarMesas());
            return "index";
        }
        return "redirect:/mesas";
    }

//    @GetMapping("/editar/{id}")
//    @PreAuthorize("hasRole('ADMIN')")
//    public String editarCategoria(@PathVariable("id") Long id, Model model, RedirectAttributes attributes) {
//        if (id <= 0) {
//            attributes.addFlashAttribute("error", "ID inválido");
//            return "redirect:/";
//        }
//        try {
//            CategoriaModel categoria = categoriaService.buscarCategoriaPorId(id);
//            CategoriaDTO categoriaDTO = new CategoriaDTO(
//                    categoria.getId(),
//                    categoria.getNome(),
//                    null
//            );
//            model.addAttribute("categoria", categoriaDTO);
//            model.addAttribute("categorias", categoriaService.listarCategorias());
//            return "categoria/editaCategoria";
//        } catch (CategoriaException e) {
//            attributes.addFlashAttribute("error", e.getMessage());
//            return "redirect:/categorias";
//        }
//    }
}