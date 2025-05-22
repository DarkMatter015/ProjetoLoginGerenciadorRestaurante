package br.app.appLogin.controllers;

import br.app.appLogin.dtos.LoginDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class LoginController {

    private static final Logger logger = LoggerFactory.getLogger(LoginController.class);

    @GetMapping("/")
    public String home(Model model, Authentication authentication) {
        if (authentication != null && authentication.isAuthenticated()) {
            model.addAttribute("nome", authentication.getName());
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
        return "index";
    }
}