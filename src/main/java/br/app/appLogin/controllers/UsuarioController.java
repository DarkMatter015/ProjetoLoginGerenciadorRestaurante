package br.app.appLogin.controllers;

import br.app.appLogin.models.UsuarioModel;
import br.app.appLogin.services.CookieService;
import br.app.appLogin.services.UsuarioService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.UnsupportedEncodingException;

@Controller
public class UsuarioController {

    @GetMapping("/cadastroUsuario")
    public String getCadastroUsuario() {
        return "cadastroUsuario";
    }

    @PostMapping("/cadastroUsuario")
    public String postCadastroUsuario(@RequestParam("nome") String nome,
                                      @RequestParam("email") String email,
                                      @RequestParam("senha") String senha,
                                      Model model
    ) {

        if(UsuarioService.cadastrarUsuario(nome, email, senha) != null){
            model.addAttribute("msg", "cadastrado com sucesso!");
            return "/index";
        }else{
            model.addAttribute("msg", "nao deu boa!");
            return "/cadastroUsuario";
        }
    }

    @GetMapping("/listarUsuarios")
    public String listaUsuarios(Model model, HttpServletRequest request) throws UnsupportedEncodingException {
        model.addAttribute("usuarios", UsuarioService.listarUsuarios());
        model.addAttribute("nome", CookieService.getCookie(request, "nome"));
        model.addAttribute("id", CookieService.getCookie(request, "id"));
        return "listaUsuarios";
    }

    @GetMapping("/editar/{id}")
    public String editarUsuario(Model model,
                                @PathVariable("id") Long id,
                                RedirectAttributes attributes) {

        try {
            UsuarioModel usuario = UsuarioService.buscarUsuarioPorId(id);
            model.addAttribute("usuario", usuario);
            return "/editaUsuario";
        } catch (Exception e) {
            attributes.addFlashAttribute("mensagemErro", e.getMessage());
        }

        return "redirect:/listarUsuarios";
    }

    @GetMapping("/excluir/{id}")
    public String excluirUsuario(@PathVariable("id") Long id){
        UsuarioService.excluirUsuarioPorId(id);

        return "redirect:/listarUsuarios";
    }

    @PostMapping("/editar/{id}")
    public String editarUsuario(Model model,
                                @PathVariable("id") Long id,
                                @RequestParam("nome") String nome,
                                @RequestParam("email") String email,
                                @RequestParam("senha") String senha){

        UsuarioService.atualizarUsuario(id, nome, email, senha);

        return "redirect:/listarUsuarios";
    }
}
