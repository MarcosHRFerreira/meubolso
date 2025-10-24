package br.com.meubolso.entity;


import java.io.Serializable;
import jakarta.persistence.*;
import java.util.List;
import java.util.ArrayList;
import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name = "conta_corrente")
public class ContaCorrenteEntity implements Serializable {
    private static final long serialVersionUID = 1L;
   
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "banco", nullable = false, length = 100)
    private String banco;

    @Column(name = "agencia", length = 20)
    private String agencia;


    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "usuario_id", nullable = false)
    private UsuarioEntity usuario;


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }


    public String getBanco() { return banco; }
    public void setBanco(String banco) { this.banco = banco; }

    public String getAgencia() { return agencia; }
    public void setAgencia(String agencia) { this.agencia = agencia; }

    public UsuarioEntity getUsuario() { return usuario; }
    public void setUsuario(UsuarioEntity usuario) { this.usuario = usuario; }

    @JsonIgnore
    @OneToMany(mappedBy = "contaCorrente")
    private List<TransacaoEntity> transacoes = new ArrayList<>();

    public List<TransacaoEntity> getTransacoes() { return transacoes; }
    public void setTransacoes(List<TransacaoEntity> transacoes) { this.transacoes = transacoes; }
}
