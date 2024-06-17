package br.com.gabrielfernandes.bdv.controller;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import br.com.gabrielfernandes.bdv.model.Categoria;
import br.com.gabrielfernandes.bdv.model.ItemPedido;
import br.com.gabrielfernandes.bdv.model.Mesa;
import br.com.gabrielfernandes.bdv.model.Pedido;
import br.com.gabrielfernandes.bdv.model.Produto;
import br.com.gabrielfernandes.bdv.service.MesaService;
import br.com.gabrielfernandes.bdv.service.PedidoService;
import br.com.gabrielfernandes.bdv.service.ProdutoService;

@Named
@ViewScoped
public class ComandaController implements Serializable {

    private static final long serialVersionUID = 1L;

    @Inject
    private ProdutoService produtoService;

    @Inject
    private PedidoService pedidoService;

    @Inject
    private MesaService mesaService;

    private List<Categoria> categorias;
    private Long categoriaSelecionadaId;
    private List<Produto> produtosFiltrados;
    private Map<String, List<Produto>> produtosPorCategoria;
    private List<Produto> produtosSelecionados = new ArrayList<>();
    private Mesa mesa;
    private Pedido pedido;
    private int quantidade;

    @PostConstruct
    public void init() {
        categorias = produtoService.findAllCategorias();
        produtosPorCategoria = new HashMap<>();
        for (Categoria categoria : categorias) {
            produtosPorCategoria.put(categoria.getNome(), produtoService.findProdutosByCategoria(categoria.getId()));
        }
        mesa = new Mesa();
        pedido = new Pedido();
        pedido.setMesa(mesa);
    }

    public void addProdutoToPedido(Long produtoId) {
        Produto produto = produtoService.findProdutoById(produtoId);
        ItemPedido itemPedido = new ItemPedido();
        itemPedido.setProduto(produto);
        itemPedido.setQuantidade(1);
        itemPedido.setSubtotal(produto.getPreco().multiply(BigDecimal.valueOf(itemPedido.getQuantidade())));
        itemPedido.setPedido(pedido);
        pedido.getItens().add(itemPedido);
        calcularTotal();
    }

    public void removeProdutoFromPedido(Long produtoId) {
        List<ItemPedido> itens = pedido.getItens().stream()
                .filter(item -> !item.getProduto().getId().equals(produtoId))
                .collect(Collectors.toList());
        pedido.setItens(itens);
        calcularTotal();
    }

    public void onCategoriaChange(Long categoriaId) {
        if (categoriaId != null) {
            produtosFiltrados = produtoService.findProdutosByCategoria(categoriaId);
        } else {
            produtosFiltrados = null;
        }
    }

    public void salvarComanda() {
        try {
            if (mesa.getNumeroOcupantes() == 0) {
                mesa.setNumeroOcupantes(4); // ou qualquer valor padrão
            }

            if (mesa.getStatus() == null) {
                mesa.setStatus(Mesa.Status.OCUPADO);
            }

            mesaService.save(mesa);
            pedido.setMesa(mesa);
            pedidoService.save(pedido);
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Comanda salva com sucesso!"));
        } catch (Exception e) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Erro", "Erro ao salvar comanda: " + e.getMessage()));
        }
    }

    public void finalizarVenda() {
        if (mesa == null) {
            FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Erro", "Mesa não encontrada.");
            FacesContext.getCurrentInstance().addMessage(null, message);
            return;
        }
    
        mesaService.save(mesa);
        pedido.setMesa(mesa);
        pedido.setStatus(Pedido.Status.PAGO);
        pedido.setMesa(mesa);
        pedidoService.save(pedido);
        mesa.setStatus(Mesa.Status.LIVRE);
        mesaService.save(mesa);
        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Venda finalizada com sucesso!"));
    }

    public void calcularTotal() {
        BigDecimal total = pedido.getItens().stream()
            .map(ItemPedido::getSubtotal)
            .reduce(BigDecimal.ZERO, BigDecimal::add);
        pedido.setTotal(total);
    }

    public void finalizarPagamento(){
        if (pedido != null) {
            pedido.setStatus(Pedido.Status.PAGO);
            pedidoService.save(pedido);
            mesa.setStatus(Mesa.Status.LIVRE);
            mesaService.save(mesa);
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Pagamento finalizado com sucesso!"));
            pedido = new Pedido(); // Reiniciar o pedido
        } else {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "Aviso", "Nenhum pedido para finalizar."));
        }
    }

    public void cancelarMesa() {
        if (pedido != null) {
            pedido.setStatus(Pedido.Status.CANCELADO);
            pedidoService.save(pedido);
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Sucesso", "Mesa cancelada."));
            pedido = new Pedido(); // Reiniciar o pedido
        } else {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "Aviso", "Nenhum pedido para cancelar."));
        }
    }

    // Getters and setters

    public int getQuantidade() {
        return quantidade;
    }

    public void setQuantidade(int quantidade) {
        this.quantidade = quantidade;
    }

    public List<Categoria> getCategorias() {
        return categorias;
    }

    public Long getCategoriaSelecionadaId() {
        return categoriaSelecionadaId;
    }

    public void setCategoriaSelecionadaId(Long categoriaSelecionadaId) {
        this.categoriaSelecionadaId = categoriaSelecionadaId;
    }

    public List<Produto> getProdutosFiltrados() {
        return produtosFiltrados;
    }

    public Map<String, List<Produto>> getProdutosPorCategoria() {
        return produtosPorCategoria;
    }

    public List<Produto> getProdutosSelecionados() {
        return produtosSelecionados;
    }

    public Mesa getMesa() {
        return mesa;
    }

    public void setMesa(Mesa mesa) {
        this.mesa = mesa;
    }

    public Pedido getPedido() {
        return pedido;
    }

    public void setPedido(Pedido pedido) {
        this.pedido = pedido;
    }

    public void imprimirPreparo() {
        List<ItemPedido> itensPreparo = pedido.getItens().stream()
                .filter(item -> item.getProduto().isPrecisaPreparo())
                .collect(Collectors.toList());
    
        if (!itensPreparo.isEmpty()) {
            StringBuilder sb = new StringBuilder("Itens para preparo:\n");
            for (ItemPedido item : itensPreparo) {
                sb.append(item.getProduto().getNome()).append(" - Quantidade: ").append(item.getQuantidade()).append("\n");
            }
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(sb.toString()));
        } else {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "Aviso", "Nenhum item precisa de preparo."));
        }
    }
}
