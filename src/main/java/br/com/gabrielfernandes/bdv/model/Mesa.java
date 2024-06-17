package br.com.gabrielfernandes.bdv.model;

import java.time.LocalDateTime;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "mesa")
public class Mesa {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    private int numero;
    private int numeroOcupantes;
    private LocalDateTime horaAbertura;
    private LocalDateTime horaFechamento;

    @Enumerated(EnumType.STRING)
    private Status status;
    
    public enum Status {
        LIVRE,
        OCUPADO,
        AGUARDANDO_PAGAMENTO
    }

    public Mesa() {
        this.numeroOcupantes = 4;
        this.status = Status.LIVRE;
        this.horaAbertura = LocalDateTime.now();

    }
    
    // Getters and Setters

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public int getNumero() {
        return numero;
    }

    public void setNumero(int numero) {
        this.numero = numero;
    }

    public int getNumeroOcupantes() {
        return numeroOcupantes;
    }

    public void setNumeroOcupantes(int numeroOcupantes) {
        this.numeroOcupantes = numeroOcupantes;
    }

    public LocalDateTime getHoraAbertura() {
        return horaAbertura;
    }

    public void setHoraAbertura(LocalDateTime horaAbertura) {
        this.horaAbertura = horaAbertura;
    }

    public LocalDateTime getHoraFechamento() {
        return horaFechamento;
    }

    public void setHoraFechamento(LocalDateTime horaFechamento) {
        this.horaFechamento = horaFechamento;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }
}
