package br.app.appLogin.repositories;

import br.app.appLogin.models.ProdutoModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public interface ProdutoRepository extends JpaRepository<ProdutoModel, Long> {
    boolean existsByNome(String nome);
    long countByCategoriaId(Long categoriaId);

    @Query("SELECT p.categoria.id AS categoriaId, COUNT(p) AS count " +
            "FROM ProdutoModel p " +
            "WHERE p.categoria.id IS NOT NULL " +
            "GROUP BY p.categoria.id")
    List<Map<String, Object>> countCategoriasByProduct();
}