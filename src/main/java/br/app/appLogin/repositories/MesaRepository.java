package br.app.appLogin.repositories;

import br.app.appLogin.models.MesaModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface MesaRepository extends JpaRepository<MesaModel, Long> {
    boolean existsByNumero(Integer numero);
    Optional<MesaModel> findByNumero(Integer numero);
}