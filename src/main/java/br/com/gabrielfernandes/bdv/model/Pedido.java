package br.com.gabrielfernandes.bdv.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

@Entity
@Table(name = "pedido")
public class Pedido {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "mesa_id")
    private Mesa mesa;

    @OneToMany(mappedBy = "pedido", cascade = CascadeType.ALL)
    private List<ItemPedido> itens = new ArrayList<>();

    @Column(nullable = false)
    private BigDecimal total = BigDecimal.ZERO;

    @Column(nullable = false)
    private LocalDateTime dataHora;

    public Object getProdutos() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public enum Status {
        ABERTO,
        FINALIZADA,
        PAGO,
        CANCELADO
    }

    public enum FormaPagamento {
        DINHEIRO,
        CARTAO,
        PIX
    }

    @Enumerated(EnumType.STRING)
    private Status status;

    @Enumerated(EnumType.STRING)
    private FormaPagamento formaPagamento;

    // Construtores, Getters e Setters

    public Pedido() {
        this.itens = new ArrayList<>();
        this.status = Status.ABERTO;
        this.total = BigDecimal.ZERO;
        this.dataHora = LocalDateTime.now();
        this.formaPagamento = FormaPagamento.DINHEIRO;
    }

    public Pedido(Mesa mesa, LocalDateTime dataHora, Status status) {
        this.mesa = mesa;
        this.dataHora = dataHora;
        this.status = status;
        this.total = BigDecimal.ZERO; // Inicialize o total como zero
        this.itens = new ArrayList<>(); // Inicializando a lista de itens
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Mesa getMesa() {
        return mesa;
    }

    public void setMesa(Mesa mesa) {
        this.mesa = mesa;
    }

    public List<ItemPedido> getItens() {
        return itens;
    }

    public void setItens(List<ItemPedido> itens) {
        this.itens = itens;
    }

    public BigDecimal getTotal() {
        return total;
    }

    public void setTotal(BigDecimal total) {
        this.total = total;
    }

    public LocalDateTime getDataHora() {
        return dataHora;
    }

    public void setDataHora(LocalDateTime dataHora) {
        this.dataHora = dataHora;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public FormaPagamento getFormaPagamento() {
        return formaPagamento;
    }

    public void setFormaPagamento(FormaPagamento formaPagamento) {
        this.formaPagamento = formaPagamento;
    }

    public void calcularTotal() {
        total = itens.stream()
            .map(ItemPedido::getSubtotal)
            .reduce(BigDecimal.ZERO, BigDecimal::add);
    }
}
