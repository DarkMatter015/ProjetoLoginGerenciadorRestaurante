package br.app.appLogin.services;

import br.app.appLogin.dtos.UsuarioDTO;
import br.app.appLogin.exceptions.UsuarioException;
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
import java.util.Optional;

@Service
public class UsuarioService {

    private final Logger logger = LoggerFactory.getLogger(UsuarioService.class);
    private final UsuarioRepository usuarioRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    // Constructor
    public UsuarioService(UsuarioRepository usuarioRepository, RoleRepository roleRepository, PasswordEncoder passwordEncoder) {
        this.usuarioRepository = usuarioRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
    }

    // Buscar Usuarios
    public UsuarioModel buscarUsuarioPorEmail(String email) throws UsuarioException {
        logger.info("Buscando usuário com email: {}", email);
        return usuarioRepository.findByEmail(email)
                .orElseThrow(() -> new UsuarioException("Usuário com email " + email + " não encontrado"));
    }

    public UsuarioModel buscarUsuarioPorId(Long id) throws UsuarioException {
        logger.info("Buscando usuário com ID: {}", id);
        return usuarioRepository.findById(id)
                .orElseThrow(() -> new UsuarioException("Usuário com ID: " + id + " não encontrado!"));
    }

    // CRUD
    // CREATE
    public UsuarioModel cadastrarUsuario(UsuarioModel usuario) {
        logger.info("Cadastrando usuário com email: {}", usuario.getEmail());

        usuarioRepository.findByEmail(usuario.getEmail())
                .ifPresent(u -> {
                    throw new RuntimeException("Email já cadastrado");
                });

        usuario.setSenha(passwordEncoder.encode(usuario.getSenha()));

        RoleModel userRole = roleRepository.findByName(usuario.getRole().getName())
                .orElseThrow(() -> new RuntimeException(usuario.getRole().getName() + " role not found"));

        usuario.setRole(userRole);

        return usuarioRepository.save(usuario);
    }

    // READ
    public List<UsuarioModel> listarUsuarios() {
        logger.info("Listando todos os usuários");

        return usuarioRepository.findAll();
    }

    // UPDATE
    public UsuarioModel atualizarUsuario(Long id, UsuarioDTO usuarioDTO) throws UsuarioException {
        logger.info("Atualizando usuário com ID: {}", id);

        UsuarioModel usuarioExistente = usuarioRepository.findById(id)
                .orElseThrow(() -> new UsuarioException("Usuário com ID: " + id + " não encontrado!"));

        Optional<UsuarioModel> outroUsuario = usuarioRepository.findByEmail(usuarioDTO.getEmail());
        if (outroUsuario.isPresent() && !outroUsuario.get().getId().equals(id)) {
            throw new IllegalArgumentException("Email já está em uso por outro usuário");
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

    // DELETE
    public void excluirUsuarioPorId(Long id) throws UsuarioException, UsuarioException {
        logger.info("Excluindo usuário com ID: {}", id);

        UsuarioModel usuario = usuarioRepository.findById(id)
                .orElseThrow(() -> new UsuarioException("Usuário com ID: " + id + " não encontrado!"));

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.getName() != null &&
                usuario.getNome().equals(authentication.getName())) {
            throw new UsuarioException("Você não pode excluir sua própria conta enquanto estiver logado!");
        }

        usuarioRepository.deleteById(id);
    }
}