package br.app.appLogin.dtos;

import br.app.appLogin.models.UsuarioModel;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public class UsuarioDTO {

    private Long id;

    @NotBlank(message = "Nome é obrigatório")
    private String nome;

    @Email(message = "Email inválido")
    @NotBlank(message = "Email é obrigatório")
    private String email;

    private String senha; // Opcional para edição
    private String confirmarSenha; // Opcional para edição

    // Método para transformar DTO para modelo de usuário
    public UsuarioModel toUsuarioModel() {
        UsuarioModel usuario = new UsuarioModel();
        usuario.setNome(nome);
        usuario.setEmail(email);
        usuario.setSenha(senha);
        return usuario;
    }

    // Método para validar senhas, se fornecidas
    public boolean isSenhasIguais() {
        if (senha == null || confirmarSenha == null) {
            return true; // Senhas não fornecidas, considera válido
        }
        return senha.equals(confirmarSenha);
    }

    // Getters e Setters
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

    public String getConfirmarSenha() {
        return confirmarSenha;
    }

    public void setConfirmarSenha(String confirmarSenha) {
        this.confirmarSenha = confirmarSenha;
    }
}