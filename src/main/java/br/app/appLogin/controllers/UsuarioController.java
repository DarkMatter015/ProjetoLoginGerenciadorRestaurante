package br.app.appLogin.controllers;

import br.app.appLogin.dtos.UsuarioDTO;
import br.app.appLogin.exceptions.UsuarioException;
import br.app.appLogin.models.UsuarioModel;
import br.app.appLogin.services.RoleService;
import br.app.appLogin.services.UsuarioService;
import jakarta.validation.Valid;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/usuarios")
public class UsuarioController {

    private final UsuarioService usuarioService;
    private final RoleService roleService;

    public UsuarioController(UsuarioService usuarioService, RoleService roleService) {
        this.usuarioService = usuarioService;
        this.roleService = roleService;
    }

    @GetMapping("/cadastrar")
    public String cadastroUsuario(Model model) {
        model.addAttribute("novoUsuario", new UsuarioDTO());
        model.addAttribute("roles", roleService.listarRoles());
        return "usuario/cadastroUsuario";
    }

    @PostMapping("/cadastrar")
    public String cadastroUsuario(
            @ModelAttribute("novoUsuario") @Valid UsuarioDTO usuarioDTO,
            BindingResult erros,
            RedirectAttributes attributes,
            Model model) {
        if (erros.hasErrors() || !usuarioDTO.isSenhasIguais()) {
            if (!usuarioDTO.isSenhasIguais()) {
                model.addAttribute("error", "As senhas precisam ser iguais");
            }
            model.addAttribute("roles", roleService.listarRoles());
            return "usuario/cadastroUsuario";
        }
        try {
            usuarioService.cadastrarUsuario(usuarioDTO.toUsuarioModel());
            attributes.addFlashAttribute("msg", "Usuário cadastrado com sucesso!");
        } catch (RuntimeException e) {
            model.addAttribute("error", e.getMessage());
            model.addAttribute("roles", roleService.listarRoles());
            return "usuario/cadastroUsuario";
        }
        return "redirect:/usuarios";
    }

    @GetMapping
    public String listaUsuarios(Model model, Authentication authentication) {
        model.addAttribute("usuarios", usuarioService.listarUsuarios());
        model.addAttribute("nome", authentication != null ? authentication.getName() : null);
        return "usuario/usuarios";
    }

    @GetMapping("/editar/{id}")
    public String editarUsuario(@PathVariable("id") Long id, Model model, RedirectAttributes attributes) {
        if (id <= 0) {
            attributes.addFlashAttribute("error", "ID inválido");
            return "redirect:/usuarios";
        }
        try {
            UsuarioModel usuario = usuarioService.buscarUsuarioPorId(id);
            UsuarioDTO usuarioDTO = new UsuarioDTO(
                    usuario.getId(),
                    usuario.getNome(),
                    usuario.getEmail(),
                    usuario.getRole() != null ? usuario.getRole().getName() : null
            );
            model.addAttribute("usuario", usuarioDTO);
            model.addAttribute("roles", roleService.listarRoles());
            return "usuario/editaUsuario";
        } catch (UsuarioException e) {
            attributes.addFlashAttribute("error", e.getMessage());
            return "redirect:/usuarios";
        }
    }

    @PostMapping("/editar/{id}")
    public String editarUsuario(
            @PathVariable("id") Long id,
            @ModelAttribute("usuario") @Valid UsuarioDTO usuarioDTO,
            BindingResult erros,
            Model model,
            RedirectAttributes attributes) {

        if (erros.hasErrors() || !usuarioDTO.isSenhasIguais()) {
            if (!usuarioDTO.isSenhasIguais()) {
                model.addAttribute("error", "As senhas precisam ser iguais");
            }

            model.addAttribute("error", erros.getAllErrors());

            model.addAttribute("roles", roleService.listarRoles());
            return "usuario/editaUsuario";
        }
        try {
            usuarioService.atualizarUsuario(id, usuarioDTO);
            attributes.addFlashAttribute("msg", "Usuário atualizado com sucesso!");
        } catch (UsuarioException e) {
            attributes.addFlashAttribute("error", e.getMessage());
            return "redirect:/usuarios";
        } catch (IllegalArgumentException e) {
            model.addAttribute("error", e.getMessage());
            model.addAttribute("roles", roleService.listarRoles());
            return "usuario/editaUsuario";
        }
        return "redirect:/usuarios";
    }

    @GetMapping("/excluir/{id}")
    public String excluirUsuario(@PathVariable("id") Long id, RedirectAttributes attributes) {
        try {
            usuarioService.excluirUsuarioPorId(id);
            attributes.addFlashAttribute("msg", "Usuário excluído com sucesso!");
        } catch (UsuarioException e) {
            attributes.addFlashAttribute("erro", e.getMessage());
        }
        return "redirect:/usuarios";
    }
}