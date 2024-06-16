package br.com.gabrielfernandes.bdv.model;

import java.util.List;

public class SelectItem {
    private String label;
    private String value;
    private List<Produto> produtos;

    public SelectItem(String label, String value, List<Produto> produtos) {
        this.label = label;
        this.value = value;
        this.produtos = produtos;
    }

    // Getters and setters
    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public List<Produto> getProdutos() {
        return produtos;
    }

    public void setProdutos(List<Produto> produtos) {
        this.produtos = produtos;
    }
}
