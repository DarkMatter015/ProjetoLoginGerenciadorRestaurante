package br.app.appLogin.config;

import br.app.appLogin.models.RoleModel;
import br.app.appLogin.models.UsuarioModel;
import br.app.appLogin.repositories.RoleRepository;
import br.app.appLogin.repositories.UsuarioRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class DatabaseInitializer implements CommandLineRunner {

    private static final Logger logger = LoggerFactory.getLogger(DatabaseInitializer.class);
    private final UsuarioRepository usuarioRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    public DatabaseInitializer(UsuarioRepository usuarioRepository, RoleRepository roleRepository, PasswordEncoder passwordEncoder) {
        this.usuarioRepository = usuarioRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void run(String... args) throws Exception {
        // Initialize roles
        if (roleRepository.findByName("ADMIN").isEmpty()) {
            RoleModel adminRole = new RoleModel("ADMIN");
            roleRepository.save(adminRole);
            logger.info("Created ADMIN role");
        }
        if (roleRepository.findByName("USER").isEmpty()) {
            RoleModel userRole = new RoleModel("USER");
            roleRepository.save(userRole);
            logger.info("Created USER role");
        }
        if (roleRepository.findByName("WAITER").isEmpty()) {
            RoleModel userRole = new RoleModel("WAITER");
            roleRepository.save(userRole);
            logger.info("Created WAITER role");
        }

        // Create default admin user
        if (usuarioRepository.findByEmail("admin@example.com").isEmpty()) {
            logger.info("Creating default admin user");
            UsuarioModel admin = new UsuarioModel();
            admin.setNome("Admin");
            admin.setEmail("admin@example.com");
            admin.setSenha(passwordEncoder.encode("admin123"));
            RoleModel adminRole = roleRepository.findByName("ADMIN")
                    .orElseThrow(() -> new RuntimeException("ADMIN role not found"));
            admin.setRole(adminRole);
            usuarioRepository.save(admin);
            logger.info("Default admin user created with email: admin@example.com");
        }
    }
}