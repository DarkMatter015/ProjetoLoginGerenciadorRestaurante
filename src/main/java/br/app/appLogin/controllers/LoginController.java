package br.app.appLogin.controllers;

import br.app.appLogin.dtos.LoginDTO;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class LoginController {


    @GetMapping("/")
    public String home(Model model, Authentication authentication) {
        if (authentication != null && authentication.isAuthenticated()) {
            model.addAttribute("nome", authentication.getName());
            // Se precisar do ID, pode buscar via UsuarioService (injetar se necessário)
        }
        return "index";
    }

    @GetMapping("/login")
    public String getLogin(Model model) {
        model.addAttribute("login", new LoginDTO());
        return "login";
    }
}