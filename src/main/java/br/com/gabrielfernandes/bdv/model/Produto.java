package br.com.gabrielfernandes.bdv.model;

import java.math.BigDecimal;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

@Entity
@Table(name = "produto")
public class Produto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String codigo;
    private String nome;
    private BigDecimal preco;
    private String descricao;
    private String imagem;

    @Enumerated(EnumType.STRING)
    private Categoria categoria;

    @Enumerated(EnumType.STRING)
    private Subcategoria subcategoria;

    @Transient
    private int quantidade;

    @Transient
    private BigDecimal subtotal;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
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

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public String getImagem() {
        return imagem;
    }

    public void setImagem(String imagem) {
        this.imagem = imagem;
    }

    public Categoria getCategoria() {
        return categoria;
    }

    public void setCategoria(Categoria categoria) {
        this.categoria = categoria;
    }

    public Subcategoria getSubcategoria() {
        return subcategoria;
    }

    public void setSubcategoria(Subcategoria subcategoria) {
        this.subcategoria = subcategoria;
    }

    public int getQuantidade() {
        return quantidade;
    }

    public void setQuantidade(int quantidade) {
        this.quantidade = quantidade;
    }

    public BigDecimal getSubtotal() {
        return subtotal;
    }

    public void setSubtotal(BigDecimal subtotal) {
        this.subtotal = subtotal;
    }

    public enum Categoria {
        ESFIHAS,
        QUIBE_FRITO,
        QUIBE_ASSADO,
        DISCO,
        SHAWARMA,
        COMBO_INDIVIDUAL,
        COMBO_FRIOS,
        PRATOS_FRIOS,
        PORCOES,
        BEBIDAS_DESTILADAS,
        WHISKY,
        DRINKS,
        CERVEJAS_600ML,
        CERVEJAS_LONG_NECK,
        REFRIGERANTES,
        AGUA,
        SUCOS,
        DIVERSOS,
    }

    public enum Subcategoria {
        SALGADAS, CALABRESA, CARNE, CARNE_COM_QUEIJO, ESCAROLA, FRANGO, FRANGO_COM_CATUPIRY, QUEIJO, SARDINHA, ZAATAR,
        DOCES, CHOCOLATE, CHOCOLATE_COM_MORANGO, CHOCOLATE_COM_QUEIJO, DOCE_DE_LEITE, NUTELLA, ROMEU_E_JULIETA,
        ADICIONAL_PARA_ESFIHAS, AZEITONA, CATUPIRY, CEBOLA, CREAM_CHEESE, ERVILHA, MILHO, MUCARELA, PALMITO, TOMATE,
        CARNE_E_CASTANHA, MUCARELA_COM_CREME_CHEESE, MUCARELA_CATUPIRY_TOMATE_E_OREGANO,
        DISCO_DE_CARNE,
        CARNE_E_CASTANHA500G_ASSADA, CARNE_E_CASTANHA1KG, MUCARELA500G_ASSADA, MUCARELA1KG_ASSADA,
        SHAWARMA_CARNE, SHAWARMA_FRANGO, SHAWARMA_FALAFEL
    }
}
