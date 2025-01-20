package br.app.appLogin.services;

import br.app.appLogin.models.UsuarioModel;
import br.app.appLogin.repositories.UsuarioRepository;
import jakarta.validation.Valid;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;

@Service
public class UsuarioService {
    private static UsuarioRepository usuarioRepository;

    public UsuarioService(UsuarioRepository usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
    }

    public static UsuarioModel buscarUsuario(String email, String senha) {
        return usuarioRepository.buscaUsuarioPorEmailSenha(email, senha);
    }

    public static UsuarioModel cadastrarUsuario(@Valid String nome,
                                                @Valid String email,
                                                @Valid String senha) {
        if(nome.trim().isEmpty() || email.trim().isEmpty() || senha.trim().isEmpty()) {
            return null;
        }else{
            UsuarioModel usuario = new UsuarioModel();
            usuario.setNome(nome);
            usuario.setEmail(email);
            usuario.setSenha(senha);
            usuarioRepository.save(usuario);

            return usuario;
        }
    }
}
