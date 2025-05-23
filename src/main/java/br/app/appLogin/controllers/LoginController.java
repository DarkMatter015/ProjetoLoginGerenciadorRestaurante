package br.app.appLogin.controllers;

import br.app.appLogin.dtos.LoginDTO;
import br.app.appLogin.dtos.MesaDTO;
import br.app.appLogin.services.MesaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class LoginController {


    @Autowired
    private MesaService mesaService;

    @GetMapping("/")
    public String home(Model model, Authentication authentication) {
        if (authentication != null && authentication.isAuthenticated()) {
            model.addAttribute("nome", authentication.getName());
            // Check for ADMIN or WAITER roles
            boolean hasRole = authentication.getAuthorities().stream()
                    .anyMatch(auth -> auth.getAuthority().equals("ROLE_ADMIN") || auth.getAuthority().equals("ROLE_WAITER"));
            if (hasRole) {
                model.addAttribute("mesas", mesaService.listarMesas());
            }
        }
        if (!model.containsAttribute("mesa")) {
            model.addAttribute("mesa", new MesaDTO(null, null, null, null));
        }
        return "index";
    }

    @GetMapping("/login")
    public String getLogin(Model model) {
        model.addAttribute("login", new LoginDTO());
        return "login";
    }

    @GetMapping("/acesso-negado")
    public String acessoNegado(Model model, Authentication authentication) {
        model.addAttribute("nome", authentication != null ? authentication.getName() : null);
        model.addAttribute("erro", "Acesso negado: você não tem permissão para acessar esta página.");
        if (!model.containsAttribute("mesa")) {
            model.addAttribute("mesa", new MesaDTO(null, null, null, null));
        }
        return "index";
    }
}