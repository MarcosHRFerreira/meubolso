package br.com.meubolso.entity;

import java.io.Serializable;
import java.time.LocalDate;

import jakarta.persistence.*;

@Entity
@Table(name = "transacao")  
public class TransacaoEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "data", nullable = false)
    private LocalDate data;

    @Column(name = "valor", nullable = false)
    private Double valor;

    @Column(name = "descricao")
    private String descricao;

    @Column(name = "tipo", nullable = false, length = 20)
    private String tipo;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "conta_id", nullable = false)
    private ContaCorrenteEntity contaCorrente;

    // getters e setters mínimos para novos campos
    public LocalDate getData() { return data; }
    public void setData(LocalDate data) { this.data = data; }

    public String getTipo() { return tipo; }
    public void setTipo(String tipo) { this.tipo = tipo; }


public TransacaoEntity() {
    }

    public TransacaoEntity(Long id, Double valor, String descricao, ContaCorrenteEntity contaCorrente ) {
        this.id = id;
        this.valor = valor;
        this.descricao = descricao;
        this.contaCorrente = contaCorrente;
        
    }

    public static long getSerialversionuid() {
        return serialVersionUID;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Double getValor() {
        return valor;
    }

    public void setValor(Double valor) {
        this.valor = valor;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public ContaCorrenteEntity getContaCorrente() {
        return contaCorrente;
    }

    public void setContaCorrente(ContaCorrenteEntity contaCorrente) {
        this.contaCorrente = contaCorrente;
    }


}
