package br.app.appLogin.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "produtos")
public class ProdutoModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "O nome é obrigatório")
    @Column(nullable = false)
    private String nome;

    @NotNull(message = "O preço é obrigatório")
    @Positive(message = "O preço deve ser positivo")
    @Column(nullable = false)
    private BigDecimal preco;

    @NotNull(message = "A disponibilidade é obrigatória")
    @Column(nullable = false)
    private Boolean disponivel;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "categoria_id", nullable = false)
    @NotNull(message = "A categoria é obrigatória")
    private CategoriaModel categoria;

    @OneToMany(mappedBy = "produto", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<ItemPedidoModel> itensPedidos = new ArrayList<>();

    // Constructors
    public ProdutoModel() {
    }

    public ProdutoModel(String nome, BigDecimal preco, Boolean disponivel, CategoriaModel categoria) {
        this.nome = nome;
        this.preco = preco;
        this.disponivel = disponivel;
        this.categoria = categoria;
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public BigDecimal getPreco() {
        return preco;
    }

    public void setPreco(BigDecimal preco) {
        this.preco = preco;
    }

    public Boolean getDisponivel() {
        return disponivel;
    }

    public void setDisponivel(Boolean disponivel) {
        this.disponivel = disponivel;
    }

    public CategoriaModel getCategoria() {
        return categoria;
    }

    public void setCategoria(CategoriaModel categoria) {
        this.categoria = categoria;
    }

    public List<ItemPedidoModel> getItensPedidos() {
        return itensPedidos;
    }

    public void setItensPedidos(List<ItemPedidoModel> itensPedidos) {
        this.itensPedidos = itensPedidos;
    }
}