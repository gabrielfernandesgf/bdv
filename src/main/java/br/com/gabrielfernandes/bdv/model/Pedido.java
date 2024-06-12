package br.com.gabrielfernandes.bdv.model;

import java.math.BigDecimal;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

@Entity
public class Pedido {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private Mesa mesa;

    @OneToMany
    @JoinColumn(name="pedido_id")
    private List<Produto> produtos;

    private BigDecimal total;

    private String status;

    public Pedido() {
        this.total = BigDecimal.ZERO;
    }

    public Pedido(Mesa mesa, List<Produto> produtos) {
        this.mesa = mesa;
        this.produtos = produtos;
        this.total = produtos.stream()
                             .map(Produto::getPreco)
                             .reduce(BigDecimal.ZERO, BigDecimal::add);
        this.status = "Pendente";
    }

    // Getters e Setters

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

    public List<Produto> getProdutos() {
        return produtos;
    }

    public void setProdutos(List<Produto> produtos) {
        this.produtos = produtos;
        calcularTotal();
    }

    public BigDecimal getTotal() {
        return total;
    }

    public void setTotal(BigDecimal total) {
        this.total = total;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public void calcularTotal() {
        this.total = produtos.stream()
                             .map(Produto::getPreco)
                             .reduce(BigDecimal.ZERO, BigDecimal::add);
    }
}
