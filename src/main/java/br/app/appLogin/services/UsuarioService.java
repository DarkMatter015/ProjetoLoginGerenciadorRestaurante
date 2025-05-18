package br.app.appLogin.services;

import br.app.appLogin.dtos.UsuarioDTO;
import br.app.appLogin.exceptions.UsuarioNaoEncontradoException;
import br.app.appLogin.models.UsuarioModel;
import br.app.appLogin.repositories.UsuarioRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UsuarioService {

    private static final Logger logger = LoggerFactory.getLogger(UsuarioService.class);
    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;

    public UsuarioService(UsuarioRepository usuarioRepository, PasswordEncoder passwordEncoder) {
        this.usuarioRepository = usuarioRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public UsuarioModel findByEmail(String email) throws UsuarioNaoEncontradoException {
        logger.info("Buscando usuário com email: {}", email);
        return usuarioRepository.findByEmail(email)
                .orElseThrow(() -> new UsuarioNaoEncontradoException("Usuário com email " + email + " não encontrado"));
    }

    public List<UsuarioModel> listarUsuarios() {
        logger.info("Listando todos os usuários");
        return usuarioRepository.findAll();
    }

    public UsuarioModel buscarUsuarioPorId(Long id) throws UsuarioNaoEncontradoException {
        logger.info("Buscando usuário com ID: {}", id);
        return usuarioRepository.findById(id)
                .orElseThrow(() -> new UsuarioNaoEncontradoException("Usuário com ID: " + id + " não encontrado!"));
    }

    public UsuarioModel cadastrarUsuario(UsuarioModel usuario) {
        logger.info("Cadastrando usuário com email: {}", usuario.getEmail());
        if (usuarioRepository.findByEmail(usuario.getEmail()).isPresent()) {
            throw new IllegalArgumentException("Email já cadastrado");
        }
        usuario.setSenha(passwordEncoder.encode(usuario.getPassword()));
        return usuarioRepository.save(usuario);
    }

    public UsuarioModel atualizarUsuario(Long id, UsuarioDTO usuarioDTO) throws UsuarioNaoEncontradoException {
        logger.info("Atualizando usuário com ID: {}", id);

        UsuarioModel usuarioExistente = usuarioRepository.findById(id)
                .orElseThrow(() -> new UsuarioNaoEncontradoException("Usuário com ID: " + id + " não encontrado!"));

        if (!usuarioExistente.getEmail().equals(usuarioDTO.getEmail()) && usuarioRepository.findByEmail(usuarioDTO.getEmail()).isPresent()) {
            throw new IllegalArgumentException("Email já cadastrado!");
        }

        usuarioExistente.setNome(usuarioDTO.getNome());
        usuarioExistente.setEmail(usuarioDTO.getEmail());

        if (usuarioDTO.getSenha() != null && !usuarioDTO.getSenha().isEmpty()) {
            if (!usuarioDTO.isSenhasIguais()) {
                throw new IllegalArgumentException("As senhas precisam ser iguais!");
            }
            usuarioExistente.setSenha(passwordEncoder.encode(usuarioDTO.getSenha()));
        }

        return usuarioRepository.save(usuarioExistente);
    }

    public void excluirUsuarioPorId(Long id) throws UsuarioNaoEncontradoException {
        logger.info("Excluindo usuário com ID: {}", id);

        if (!usuarioRepository.existsById(id)) {
            throw new UsuarioNaoEncontradoException("Usuário com ID: " + id + " não encontrado!");
        }
        usuarioRepository.deleteById(id);
    }
}