package br.app.appLogin.repositories;

import br.app.appLogin.models.CategoriaModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CategoriaRepository extends JpaRepository<CategoriaModel, Long> {
    boolean existsByNome(String nome);
}