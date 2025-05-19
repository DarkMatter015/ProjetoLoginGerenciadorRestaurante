package br.app.appLogin.controllers;

import br.app.appLogin.dtos.UsuarioDTO;
import br.app.appLogin.exceptions.UsuarioAtualLogadoException;
import br.app.appLogin.exceptions.UsuarioNaoEncontradoException;
import br.app.appLogin.models.UsuarioModel;
import br.app.appLogin.services.UsuarioService;
import jakarta.validation.Valid;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class UsuarioController {

    private final UsuarioService usuarioService;

    public UsuarioController(UsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }

    @GetMapping("/cadastroUsuario")
    public String getCadastroUsuario(Model model) {
        model.addAttribute("novoUsuario", new UsuarioDTO());
        return "/cadastroUsuario";
    }

    @PostMapping("/cadastroUsuario")
    public String postCadastroUsuario(@ModelAttribute("novoUsuario") @Valid UsuarioDTO usuarioDTO,
                                      BindingResult erros,
                                      RedirectAttributes attributes,
                                      Model model) {
        if (erros.hasErrors() || !usuarioDTO.isSenhasIguais()) {
            if (!usuarioDTO.isSenhasIguais()) {
                model.addAttribute("erroSenha", "As senhas precisam ser iguais");
            }
            return "/cadastroUsuario";
        }
        try {
            usuarioService.cadastrarUsuario(usuarioDTO.toUsuarioModel());
            attributes.addFlashAttribute("msg", "Usuário cadastrado com sucesso!");
        } catch (IllegalArgumentException e) {
            model.addAttribute("erro", e.getMessage());
            return "/cadastroUsuario";
        }
        return "redirect:/cadastroUsuario";
    }

    @GetMapping("/listarUsuarios")
    public String listaUsuarios(Model model, Authentication authentication) {
        model.addAttribute("usuarios", usuarioService.listarUsuarios());
        model.addAttribute("nome", authentication != null ? authentication.getName() : null);
        // ID pode ser obtido via usuarioService.findByEmail(authentication.getName()).getId() se necessário
        return "/listaUsuarios";
    }

    @GetMapping("/editar/{id}")
    public String editarUsuario(@PathVariable("id") Long id, Model model, RedirectAttributes attributes) {
        if (id <= 0) {
            attributes.addFlashAttribute("mensagemErro", "ID inválido");
            return "redirect:/listarUsuarios";
        }
        try {
            UsuarioModel usuario = usuarioService.buscarUsuarioPorId(id);
            UsuarioDTO usuarioDTO = new UsuarioDTO();
            usuarioDTO.setNome(usuario.getNome());
            usuarioDTO.setEmail(usuario.getEmail());
            model.addAttribute("usuario", usuarioDTO);
//            model.addAttribute("id", id);
            return "/editaUsuario";
        } catch (UsuarioNaoEncontradoException e) {
            attributes.addFlashAttribute("mensagemErro", e.getMessage());
            return "redirect:/listarUsuarios";
        }
    }

    @PostMapping("/editar/{id}")
    public String editarUsuario(@PathVariable("id") Long id,
                                @ModelAttribute("usuario") @Valid UsuarioDTO usuarioDTO,
                                BindingResult erros,
                                RedirectAttributes attributes) {
        if (erros.hasErrors() || !usuarioDTO.isSenhasIguais()) {
            if (!usuarioDTO.isSenhasIguais()) {
                attributes.addFlashAttribute("mensagemErro", "As senhas precisam ser iguais");
            }
            return "/editaUsuario";
        }
        try {
            usuarioService.atualizarUsuario(id, usuarioDTO);
            attributes.addFlashAttribute("msg", "Usuário atualizado com sucesso!");
        } catch (UsuarioNaoEncontradoException e) {
            attributes.addFlashAttribute("mensagemErro", e.getMessage());
            return "redirect:/listarUsuarios";
        } catch (IllegalArgumentException e) {
            attributes.addFlashAttribute("mensagemErro", e.getMessage());
            return "/editaUsuario";
        }
        return "redirect:/listarUsuarios";
    }

    @GetMapping("/excluir/{id}")
    public String excluirUsuario(@PathVariable("id") Long id, RedirectAttributes attributes) {
        try {
            usuarioService.excluirUsuarioPorId(id);
            attributes.addFlashAttribute("msg", "Usuário excluído com sucesso!");
        } catch (UsuarioNaoEncontradoException | UsuarioAtualLogadoException e) {
            attributes.addFlashAttribute("erro", e.getMessage());
        }
        return "redirect:/listarUsuarios";
    }
}