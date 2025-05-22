package br.app.appLogin.services;

import br.app.appLogin.dtos.UsuarioDTO;
import br.app.appLogin.exceptions.UsuarioNaoEncontradoException;
import br.app.appLogin.exceptions.UsuarioAtualLogadoException;
import br.app.appLogin.models.RoleModel;
import br.app.appLogin.models.UsuarioModel;
import br.app.appLogin.repositories.RoleRepository;
import br.app.appLogin.repositories.UsuarioRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UsuarioService {

    private static final Logger logger = LoggerFactory.getLogger(UsuarioService.class);
    private final UsuarioRepository usuarioRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    public UsuarioService(UsuarioRepository usuarioRepository, RoleRepository roleRepository, PasswordEncoder passwordEncoder) {
        this.usuarioRepository = usuarioRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public UsuarioModel findByEmail(String email) throws UsuarioNaoEncontradoException {
        logger.info("Buscando usuário com email: {}", email);
        return usuarioRepository.findByEmail(email)
                .orElseThrow(() -> new UsuarioNaoEncontradoException("Usuário com email " + email + " não encontrado"));
    }

    public UsuarioModel cadastrarUsuario(UsuarioModel usuario) {
        logger.info("Cadastrando usuário com email: {}", usuario.getEmail());
        if (usuarioRepository.findByEmail(usuario.getEmail()).isPresent()) {
            throw new IllegalArgumentException("Email já cadastrado");
        }
        usuario.setSenha(passwordEncoder.encode(usuario.getSenha()));
        RoleModel userRole = roleRepository.findByName("USER")
                .orElseThrow(() -> new RuntimeException("USER role not found"));
        usuario.setRole(userRole);
        return usuarioRepository.save(usuario);
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

    public UsuarioModel atualizarUsuario(Long id, UsuarioDTO usuarioDTO) throws UsuarioNaoEncontradoException {
        logger.info("Atualizando usuário com ID: {}", id);
        UsuarioModel usuarioExistente = usuarioRepository.findById(id)
                .orElseThrow(() -> new UsuarioNaoEncontradoException("Usuário com ID: " + id + " não encontrado!"));

        if (!usuarioExistente.getEmail().equals(usuarioDTO.getEmail()) &&
                usuarioRepository.findByEmail(usuarioDTO.getEmail()).isPresent()) {
            throw new IllegalArgumentException("Email já cadastrado");
        }

        usuarioExistente.setNome(usuarioDTO.getNome());
        usuarioExistente.setEmail(usuarioDTO.getEmail());

        if (usuarioDTO.getSenha() != null && !usuarioDTO.getSenha().isEmpty()) {
            if (!usuarioDTO.isSenhasIguais()) {
                throw new IllegalArgumentException("As senhas precisam ser iguais");
            }
            usuarioExistente.setSenha(passwordEncoder.encode(usuarioDTO.getSenha()));
        }

        return usuarioRepository.save(usuarioExistente);
    }

    public void excluirUsuarioPorId(Long id) throws UsuarioNaoEncontradoException, UsuarioAtualLogadoException {
        logger.info("Excluindo usuário com ID: {}", id);

        UsuarioModel usuario = usuarioRepository.findById(id)
                .orElseThrow(() -> new UsuarioNaoEncontradoException("Usuário com ID: " + id + " não encontrado!"));

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.getName() != null &&
                usuario.getEmail().equals(authentication.getName())) {
            throw new UsuarioAtualLogadoException("Você não pode excluir sua própria conta enquanto estiver logado!");
        }

        usuarioRepository.deleteById(id);
    }
}