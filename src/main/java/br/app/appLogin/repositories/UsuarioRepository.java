package br.app.appLogin.repositories;

import br.app.appLogin.models.UsuarioModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface UsuarioRepository extends JpaRepository<UsuarioModel, Long> {
    @Query(value = "select * from usuarios where email = :email and senha = :senha limit 1", nativeQuery = true)
    public UsuarioModel buscaUsuarioPorEmailSenha(String email, String senha);
}
