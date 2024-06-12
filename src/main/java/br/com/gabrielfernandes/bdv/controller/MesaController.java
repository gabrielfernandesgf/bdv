package br.com.gabrielfernandes.bdv.controller;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;

import br.com.gabrielfernandes.bdv.model.Mesa;
import br.com.gabrielfernandes.bdv.model.Mesa.Status;
import br.com.gabrielfernandes.bdv.model.Pedido;
import br.com.gabrielfernandes.bdv.model.Produto;
import br.com.gabrielfernandes.bdv.service.MesaService;
import br.com.gabrielfernandes.bdv.service.PedidoService;
import br.com.gabrielfernandes.bdv.service.ProdutoService;
import java.math.BigDecimal;

@Named
@ViewScoped
public class MesaController implements Serializable {

    private static final long serialVersionUID = 1L;

    private Mesa mesa = new Mesa();
    private Pedido pedido = new Pedido();
    private Produto produto = new Produto();
    private int quantidade = 1;

    @Inject
    private MesaService mesaService;

    @Inject
    private PedidoService pedidoService;

    @Inject
    private ProdutoService produtoService;

    public String abrirMesa() {
        mesa.setDataHoraAbertura(LocalDateTime.now());
        mesa.setStatus(Status.OCUPADO);
        mesaService.save(mesa);
        pedido = new Pedido(mesa, Arrays.asList()); // Inicializa o pedido para a mesa
        pedidoService.save(pedido);
        return "mesaAberta";
    }

    public String fecharMesa() {
        mesa.setStatus(Status.AGUARDANDO_PAGAMENTO);
        mesaService.save(mesa);
        pedidoService.fecharPedido(pedido.getId());
        return "mesaFechada";
    }

    public String liberarMesa() {
        mesa.setStatus(Status.LIVRE);
        mesaService.save(mesa);
        return "mesaLiberada";
    }

    public void addProdutoToPedido() {
        produto = produtoService.findById(produto.getId());
        if (produto != null) {
            produto.setQuantidade(quantidade);
            BigDecimal subtotal = produto.getPreco().multiply(BigDecimal.valueOf(quantidade));
            produto.setSubtotal(subtotal);
            pedido.getProdutos().add(produto);
            pedido.calcularTotal();
            pedidoService.save(pedido);
        }
    }

    public void selecionarProduto(Produto produto) {
        this.produto = produto;
        this.quantidade = 1;
    }

    public void sairDaComanda() {
        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Saindo da comanda..."));
        // Lógica adicional para sair da comanda se necessário
    }

    public void imprimirPreparo() {
        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Imprimindo preparo..."));
        // Lógica para imprimir preparo - depende da integração com uma impressora ou geração de PDF
    }

    public void finalizarVenda() {
        pedido.setStatus("Finalizada");
        pedidoService.save(pedido);
        mesa.setStatus(Status.LIVRE);
        mesaService.save(mesa);
        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Venda finalizada com sucesso!"));
    }

    public void cancelarMesa() {
        pedidoService.cancelarPedido(pedido.getId());
        mesa.setStatus(Status.LIVRE);
        mesaService.save(mesa);
        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Mesa cancelada com sucesso!"));
    }

    public String abrirComanda(Mesa mesa) {
        this.mesa = mesa;
        mesa.setStatus(Status.OCUPADO);
        mesa.setDataHoraAbertura(LocalDateTime.now());
        save();
        pedido = new Pedido(mesa, Arrays.asList()); // Inicializa o pedido para a mesa
        pedidoService.save(pedido);
        return "comanda.xhtml?faces-redirect=true"; // Redireciona para a página de comanda
    }

    public void save() {
        try {
            mesaService.save(mesa);
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Mesa salva com sucesso!"));
        } catch (Exception e) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Erro ao salvar a mesa", e.getMessage()));
        }
    }

    public List<Mesa> getMesas() {
        return mesaService.findAll();
    }

    public List<Status> getStatuses() {
        return Arrays.asList(Status.values());
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

    public static long getSerialversionuid() {
        return serialVersionUID;
    }

    public MesaService getMesaService() {
        return mesaService;
    }

    public void setMesaService(MesaService mesaService) {
        this.mesaService = mesaService;
    }

    public PedidoService getPedidoService() {
        return pedidoService;
    }

    public void setPedidoService(PedidoService pedidoService) {
        this.pedidoService = pedidoService;
    }

    public ProdutoService getProdutoService() {
        return produtoService;
    }

    public void setProdutoService(ProdutoService produtoService) {
        this.produtoService = produtoService;
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

    public Produto getProduto() {
        return produto;
    }

    public void setProduto(Produto produto) {
        this.produto = produto;
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
        return mesa.getStatus().getCor();
    }
}
