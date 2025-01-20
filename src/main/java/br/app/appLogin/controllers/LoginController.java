package br.app.appLogin.controllers;

import br.app.appLogin.models.UsuarioModel;
import br.app.appLogin.services.CookieService;
import br.app.appLogin.services.UsuarioService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.io.UnsupportedEncodingException;

@Controller
public class LoginController {

    @GetMapping("/")
    public String home(Model model, HttpServletRequest request) throws UnsupportedEncodingException {
        model.addAttribute("nome", CookieService.getCookie(request, "nome"));
        model.addAttribute("id", CookieService.getCookie(request, "id"));
        return "index";
    }

    @GetMapping("/login")
    public String getLogin() {
        return "login";
    }

    @PostMapping("/login")
    public String postLogin(@RequestParam String email,
                            @RequestParam String senha,
                            Model model,
                            HttpServletResponse response) throws UnsupportedEncodingException {

        UsuarioModel usuarioLogado = UsuarioService.buscarUsuarioPorEmailSenha(email, senha);

        if(usuarioLogado == null){
            model.addAttribute("erro", "ERRO: Email ou senha incorreta!");
            return "login";
        }else{
            CookieService.setCookie(response, "id", String.valueOf(usuarioLogado.getId()), 10000);
            CookieService.setCookie(response, "nome", String.valueOf(usuarioLogado.getNome()), 10000);
            return "redirect:/";
        }
    }

    @GetMapping("/sair")
    public String sair(HttpServletResponse response) throws UnsupportedEncodingException {
        CookieService.setCookie(response, "id", "", 0);
        return "/login";
    }


}
