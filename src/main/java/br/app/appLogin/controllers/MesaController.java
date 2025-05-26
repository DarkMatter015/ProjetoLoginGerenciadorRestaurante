package br.app.appLogin.controllers;

import br.app.appLogin.dtos.MesaDTO;
import br.app.appLogin.exceptions.MesaException;
import br.app.appLogin.services.MesaService;
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
@RequestMapping("/mesas")
public class MesaController {

    private static final Logger logger = LoggerFactory.getLogger(MesaController.class);

    @Autowired
    private MesaService mesaService;

    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'WAITER')")
    public String showMesaForm(Model model) {
        if (!model.containsAttribute("mesa")) {
            model.addAttribute("mesa", new MesaDTO(null, null, null, null));
        }
        model.addAttribute("mesas", mesaService.listarMesas());
        logger.debug("Mesas loaded: {}", mesaService.listarMesas());
        return "index";
    }

    @PostMapping("/cadastrar")
    @PreAuthorize("hasAnyRole('ADMIN', 'WAITER')")
    public String cadastrarMesa(@Valid MesaDTO mesaDTO, BindingResult result, Model model, RedirectAttributes attributes) {
        if (result.hasErrors()) {
            logger.debug("Validation errors: {}", result.getAllErrors());
            model.addAttribute("mesa", mesaDTO);
            model.addAttribute("mesas", mesaService.listarMesas());
            return "index";
        }
        try {
            mesaService.criarMesa(mesaDTO);
            attributes.addFlashAttribute("msg", "Mesa cadastrada com sucesso!");
        } catch (MesaException e) {
            logger.error("Error creating mesa: {}", e.getMessage());
            result.rejectValue("numero", "duplicate", e.getMessage());

            model.addAttribute("error", e.getMessage());
            model.addAttribute("mesa", mesaDTO);
            model.addAttribute("mesas", mesaService.listarMesas());
            return "index";
        }
        return "redirect:/mesas";
    }
}