package br.app.appLogin.services;

import br.app.appLogin.models.RoleModel;
import br.app.appLogin.repositories.RoleRepository;
import br.app.appLogin.repositories.UsuarioRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RoleService {

    private static final Logger logger = LoggerFactory.getLogger(UsuarioService.class);

    private final RoleRepository roleRepository;

    public RoleService(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    public List<RoleModel> listarRoles() {
        logger.info("Listando todas as roles");
        return roleRepository.findAll();
    }
}
