package br.app.appLogin.repositories;

import br.app.appLogin.models.MesaModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MesaRepository extends JpaRepository<MesaModel, Long> {
    boolean existsByNumero(Integer numero);
}