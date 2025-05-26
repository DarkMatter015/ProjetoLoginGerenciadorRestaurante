package br.app.appLogin.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "usuarios")
public class UsuarioModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String nome;

    @Email(message = "O email deve ser valido")
    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private String senha;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "role_id", nullable = false)
    private RoleModel role;

    @OneToMany(mappedBy = "usuario", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<PedidoModel> pedidos = new ArrayList<>();

    // Constructors
    public UsuarioModel() {
    }

    public UsuarioModel(String nome, String email, String senha, RoleModel role) {
        this.nome = nome;
        this.email = email;
        this.senha = senha;
        this.role = role;
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getSenha() {
        return senha;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }

    public RoleModel getRole() {
        return role;
    }

    public void setRole(RoleModel role) {
        this.role = role;
    }

    public List<PedidoModel> getPedidos() {
        return pedidos;
    }
}