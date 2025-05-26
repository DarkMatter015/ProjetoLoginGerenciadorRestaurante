package br.app.appLogin.dtos;

import br.app.appLogin.models.RoleModel;
import br.app.appLogin.models.UsuarioModel;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class UsuarioDTO {

    private Long id;

    @NotBlank(message = "Nome é obrigatório")
    @Size(max = 255, message = "Nome deve ter até 255 caracteres")
    private String nome;

    @NotBlank(message = "Email é obrigatório")
    @Email(message = "Email inválido")
    private String email;

//    @Size(min = 6, message = "Senha deve ter pelo menos 6 caracteres")
    private String senha;

//    @Size(min = 6, message = "Confirmação de senha deve ter pelo menos 6 caracteres")
    private String confirmarSenha;

    @NotBlank(message = "Role é obrigatória")
    private String roleName;

    // Constructors
    public UsuarioDTO() {
    }

    public UsuarioDTO(Long id, String nome, String email, String roleName) {
        this.id = id;
        this.nome = nome;
        this.email = email;
        this.roleName = roleName;
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

    public String getConfirmarSenha() {
        return confirmarSenha;
    }

    public void setConfirmarSenha(String confirmarSenha) {
        this.confirmarSenha = confirmarSenha;
    }

    public String getRoleName() {
        return roleName;
    }

    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }

    // Cast to UsuarioModel
    public UsuarioModel toUsuarioModel() {
        UsuarioModel usuario = new UsuarioModel();
        usuario.setNome(this.nome);
        usuario.setEmail(this.email);
        usuario.setSenha(this.senha);
        if (this.roleName != null) {
            RoleModel role = new RoleModel();
            role.setName(this.roleName);
            usuario.setRole(role);
        }
        return usuario;
    }

    // Validacao de senhas
    public boolean isSenhasIguais() {
        if (senha == null && confirmarSenha == null) {
            return true; // No password update
        }
        return senha != null && senha.equals(confirmarSenha);
    }
}