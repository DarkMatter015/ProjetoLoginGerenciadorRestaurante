package br.app.appLogin.controllers;

import br.app.appLogin.models.UsuarioModel;
import br.app.appLogin.services.CookieService;
import br.app.appLogin.services.UsuarioService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.UnsupportedEncodingException;
import java.util.Objects;

@Controller
public class UsuarioController {

    @GetMapping("/cadastroUsuario")
    public String getCadastroUsuario(Model model) {
        UsuarioModel usuario = new UsuarioModel();
        model.addAttribute("novoUsuario", usuario);
        return "cadastroUsuario";
    }

    @PostMapping("/cadastroUsuario")
    public String postCadastroUsuario(@ModelAttribute("novoUsuario") @Valid UsuarioModel usuario,
                                      BindingResult erros,
                                      RedirectAttributes attributes,
                                      @RequestParam("senha") String senha,
                                      @RequestParam("confirmarSenha") String confirmarSenha,
                                      Model model) {

        if(erros.hasErrors() || !senha.equals(confirmarSenha)){
            if( !senha.equals(confirmarSenha)){model.addAttribute("erroSenha", "As senhas precisam ser iguais");}
            return "/cadastroUsuario";
        }
        UsuarioService.cadastrarUsuario(usuario);
        attributes.addFlashAttribute("msg", "Usuário cadastrado com sucesso!");

        return "redirect:/cadastroUsuario";
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
