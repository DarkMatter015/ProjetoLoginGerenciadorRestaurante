package br.app.appLogin.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "pedidos")
public class PedidoModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull(message = "A data é obrigatória")
    @Column(nullable = false)
    private LocalDateTime data;

    @NotNull(message = "O status é obrigatório")
    @Column(nullable = false)
    private String status;

    @NotNull(message = "O total é obrigatório")
    @Column(nullable = false)
    private BigDecimal total;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "mesa_id", nullable = false)
    @NotNull(message = "A mesa é obrigatória")
    private MesaModel mesa;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cliente_id")
    private ClienteModel cliente;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "usuario_id", nullable = false)
    @NotNull(message = "O usuário é obrigatório")
    private UsuarioModel usuario;

    @OneToMany(mappedBy = "pedido", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<ItemPedidoModel> itensPedidos = new ArrayList<>();

    // Constructors
    public PedidoModel() {
    }

    public PedidoModel(LocalDateTime data, String status, BigDecimal total, MesaModel mesa, ClienteModel cliente, UsuarioModel usuario) {
        this.data = data;
        this.status = status;
        this.total = total;
        this.mesa = mesa;
        this.cliente = cliente;
        this.usuario = usuario;
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public LocalDateTime getData() {
        return data;
    }

    public void setData(LocalDateTime data) {
        this.data = data;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public BigDecimal getTotal() {
        return total;
    }

    public void setTotal(BigDecimal total) {
        this.total = total;
    }

    public MesaModel getMesa() {
        return mesa;
    }

    public void setMesa(MesaModel mesa) {
        this.mesa = mesa;
    }

    public ClienteModel getCliente() {
        return cliente;
    }

    public void setCliente(ClienteModel cliente) {
        this.cliente = cliente;
    }

    public UsuarioModel getUsuario() {
        return usuario;
    }

    public void setUsuario(UsuarioModel usuario) {
        this.usuario = usuario;
    }

    public List<ItemPedidoModel> getItensPedidos() {
        return itensPedidos;
    }

    public void setItensPedidos(List<ItemPedidoModel> itensPedidos) {
        this.itensPedidos = itensPedidos;
    }
}