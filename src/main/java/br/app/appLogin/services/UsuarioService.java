package br.app.appLogin.services;

import br.app.appLogin.models.UsuarioModel;
import br.app.appLogin.repositories.UsuarioRepository;
import jakarta.validation.Valid;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UsuarioService {
    private static UsuarioRepository usuarioRepository;

    public UsuarioService(UsuarioRepository usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
    }

    public static UsuarioModel buscarUsuarioPorEmailSenha(String email, String senha) {
        return usuarioRepository.buscaUsuarioPorEmailSenha(email, senha);
    }

    public static UsuarioModel buscarUsuarioPorId(Long id){
        return usuarioRepository.findById(id).get();
    }

    public static List<UsuarioModel> listarUsuarios() {
        return usuarioRepository.findAll();
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

    public static boolean excluirUsuarioPorId(Long id){
        UsuarioModel user = UsuarioService.buscarUsuarioPorId(id);
        if(user != null){
            usuarioRepository.delete(user);
            return true;
        }
        return false;

    }

    public static UsuarioModel atualizarUsuario(Long id,
                                                String nome,
                                                String email,
                                                String senha) {
        UsuarioModel usuario1 = UsuarioService.buscarUsuarioPorId(id);
        usuario1.setNome(nome);
        usuario1.setEmail(email);
        usuario1.setSenha(senha);
        usuarioRepository.save(usuario1);

        return usuario1;
    }
}
