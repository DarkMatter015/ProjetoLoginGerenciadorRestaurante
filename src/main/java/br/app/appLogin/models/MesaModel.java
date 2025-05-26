package br.app.appLogin.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "mesas")
public class MesaModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull(message = "O número é obrigatório")
    @Positive(message = "O número deve ser positivo")
    @Column(unique = true, nullable = false)
    private Integer numero;


    @Positive(message = "A capacidade deve ser positiva")
    private Integer capacidade;

    @NotNull(message = "O status é obrigatório")
    @Column(nullable = false)
    private String status;

    @OneToMany(mappedBy = "mesa", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<PedidoModel> pedidos = new ArrayList<>();

    // Constructors
    public MesaModel() {
    }

    public MesaModel(Integer numero, Integer capacidade, String status) {
        this.numero = numero;
        this.capacidade = capacidade;
        this.status = status;
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public Integer getNumero() {
        return numero;
    }

    public void setNumero(Integer numero) {
        this.numero = numero;
    }

    public Integer getCapacidade() {
        return capacidade;
    }

    public void setCapacidade(Integer capacidade) {
        this.capacidade = capacidade;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public List<PedidoModel> getPedidos() {
        return pedidos;
    }
}