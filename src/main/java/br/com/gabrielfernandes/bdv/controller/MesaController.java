package br.com.gabrielfernandes.bdv.controller;

import java.io.IOException;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;

import org.primefaces.PrimeFaces;
import org.springframework.beans.factory.annotation.Autowired;

import br.com.gabrielfernandes.bdv.model.ItemPedido;
import br.com.gabrielfernandes.bdv.model.Mesa;
import br.com.gabrielfernandes.bdv.model.Pedido;
import br.com.gabrielfernandes.bdv.model.Produto;
import br.com.gabrielfernandes.bdv.service.MesaService;
import br.com.gabrielfernandes.bdv.service.PedidoService;
import br.com.gabrielfernandes.bdv.service.ProdutoService;

@Named
@ViewScoped
public class MesaController implements Serializable {

    private static final long serialVersionUID = 1L;

    private Mesa mesa = new Mesa();
    private Pedido pedido = new Pedido();
    private Long produtoSelecionadoId;
    private int quantidade = 1;
    private int numeroOcupantes;
    private LocalDateTime dataHoraAbertura;

    @Autowired
    private MesaService mesaService;

    @Autowired
    private PedidoService pedidoService;

    @Inject
    private ProdutoService produtoService;

    private Mesa mesaSelecionada;

    public String sairDaComanda() {
        return "mesa.xhtml?faces-redirect=true";
    }

    public void imprimirPreparo() {
        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Relatório enviado para a cozinha."));
    }

    public String voltarMesas() {
        return "mesa.xhtml?faces-redirect=true"; // Redireciona para a página das mesas
    }

    public String finalizarVenda() {
        BigDecimal total = pedido.getItens().stream()
                                .map(ItemPedido::getSubtotal)
                                .reduce(BigDecimal.ZERO, BigDecimal::add);
        pedido.setTotal(total);
        pedido.setStatus(Pedido.Status.FINALIZADA);
        pedidoService.save(pedido);
        return "pagamento.xhtml?faces-redirect=true";
    }

    public String finalizarPagamento() {
        pedido.setStatus(Pedido.Status.PAGO);
        pedidoService.save(pedido);
        mesa.setStatus(Mesa.Status.LIVRE);
        mesaService.save(mesa);
        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Pagamento finalizado com sucesso!"));
        return "mesa.xhtml?faces-redirect=true";
    }

    public void addProdutoToPedido() {
        Produto produtoSelecionado = produtoService.findById(produtoSelecionadoId);
        if (produtoSelecionado != null) {
            ItemPedido itemPedido = new ItemPedido();
            itemPedido.setProduto(produtoSelecionado);
            itemPedido.setQuantidade(quantidade);
            BigDecimal subtotal = produtoSelecionado.getPreco().multiply(BigDecimal.valueOf(quantidade));
            itemPedido.setSubtotal(subtotal);
            pedido.getItens().add(itemPedido);  // Adiciona à lista de itens
            pedido.calcularTotal();
            pedidoService.save(pedido);
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Produto adicionado ao pedido."));
        } else {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Erro", "Produto não encontrado."));
        }
    }

    public void selecionarProduto(Long produtoId) {
        this.produtoSelecionadoId = produtoId;
        this.quantidade = 1;
    }

    public void abrirComanda(Mesa mesa) {
        this.mesaSelecionada = mesa;
        this.dataHoraAbertura = LocalDateTime.now();
        this.numeroOcupantes = mesa.getNumeroOcupantes();
        
        if (mesa.getStatus() == Mesa.Status.LIVRE) {
            PrimeFaces.current().executeScript("PF('dlgAbrirComanda').show()");
        } else if (mesa.getStatus() == Mesa.Status.OCUPADO) {
            // Redireciona para a comanda da mesa já ocupada
            try {
                FacesContext.getCurrentInstance().getExternalContext().redirect("comanda.xhtml?mesaId=" + mesa.getId());
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "A mesa já está aguardando pagamento.", ""));
        }
    }

    public void confirmarAberturaComanda() {
        Pedido pedido = new Pedido();
        pedido.setMesa(mesaSelecionada);
        pedido.setFormaPagamento(Pedido.FormaPagamento.DINHEIRO); // Forma de pagamento padrão
        pedido.setStatus(Pedido.Status.ABERTO);
        pedido.setTotal(BigDecimal.ZERO); // Inicialize o total como zero
        pedido.setDataHora(dataHoraAbertura);
        pedidoService.save(pedido);

        mesaSelecionada.setStatus(Mesa.Status.OCUPADO);
        mesaSelecionada.setNumeroOcupantes(numeroOcupantes);
        mesaService.save(mesaSelecionada);

        FacesContext context = FacesContext.getCurrentInstance();
        try {
            context.getExternalContext().redirect("comanda.xhtml");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void save() {
        try {
            mesaService.save(mesa);
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Mesa salva com sucesso!"));
        } catch (Exception e) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Erro ao salvar a mesa", e.getMessage()));
        }
    }

    public List<String> getFormasPagamento() {
        return Arrays.asList("DINHEIRO", "CARTAO", "PIX");
    }

    public ProdutoService getProdutoService() {
        return produtoService;
    }

    public void setProdutoService(ProdutoService produtoService) {
        this.produtoService = produtoService;
    }

    public List<Mesa> getMesas() {
        return mesaService.findAll();
    }

    public List<Mesa.Status> getStatuses() {
        return Arrays.asList(Mesa.Status.values());
    }

    public List<String> getCategorias() {
        return Arrays.asList("Esfihas", "Quibe", "Discos", "Shawarma", "Combo", "PratosFrios", "Porções", "Bebidas");
    }

    public List<String> getSubcategorias() {
        return Arrays.asList("Salgadas", "Doces", "Adicional para Esfihas", "Frito", "Assado", "Adicional", "Destiladas", "Whiskys", "Drinks", "Cervejas 600ml", "Cervejas LongNeck", "Refrigerantes", "Agua", "Sucos");
    }

    public String getMesaStatusClass(Mesa mesa) {
        switch (mesa.getStatus()) {
            case LIVRE:
                return "mesa-disponivel";
            case OCUPADO:
                return "mesa-ocupada";
            case AGUARDANDO_PAGAMENTO:
                return "mesa-aguardando-pagamento";
            default:
                return "";
        }
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

    public Long getProdutoSelecionadoId() {
        return produtoSelecionadoId;
    }

    public void setProdutoSelecionadoId(Long produtoSelecionadoId) {
        this.produtoSelecionadoId = produtoSelecionadoId;
    }

    public int getQuantidade() {
        return quantidade;
    }

    public void setQuantidade(int quantidade) {
        this.quantidade = quantidade;
    }

    public List<Produto> getProdutos() {
        return produtoService.findAll();
    }

    public String getStatus() {
        return mesa.getStatus().name();
    }

    public String getStatusCor() {
        return mesa.getStatus().name(); // Alterado para retornar a string do status
    }

    public Mesa getMesaSelecionada() {
        return mesaSelecionada;
    }

    public void setMesaSelecionada(Mesa mesaSelecionada) {
        this.mesaSelecionada = mesaSelecionada;
    }

    public int getNumeroOcupantes() {
        return numeroOcupantes;
    }

    public void setNumeroOcupantes(int numeroOcupantes) {
        this.numeroOcupantes = numeroOcupantes;
    }

    public LocalDateTime getDataHoraAbertura() {
        return dataHoraAbertura;
    }

    public void setDataHoraAbertura(LocalDateTime dataHoraAbertura) {
        this.dataHoraAbertura = dataHoraAbertura;
    }
}
