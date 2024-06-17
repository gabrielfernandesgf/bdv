package br.com.gabrielfernandes.bdv.controller;

import java.io.IOException;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Inject;

import org.primefaces.PrimeFaces;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.context.annotation.SessionScope;

import br.com.gabrielfernandes.bdv.model.Categoria;
import br.com.gabrielfernandes.bdv.model.ItemPedido;
import br.com.gabrielfernandes.bdv.model.Mesa;
import br.com.gabrielfernandes.bdv.model.Pedido;
import br.com.gabrielfernandes.bdv.model.Produto;
import br.com.gabrielfernandes.bdv.service.CategoriaService;
import br.com.gabrielfernandes.bdv.service.MesaService;
import br.com.gabrielfernandes.bdv.service.PedidoService;
import br.com.gabrielfernandes.bdv.service.ProdutoService;

@SessionScope
@Controller
public class MesaController implements Serializable {

    private static final long serialVersionUID = 1L;

    private Mesa mesa = new Mesa();
    private Pedido pedido = new Pedido();
    private Long produtoSelecionadoId;
    private Long categoriaSelecionadaId;
    private int quantidade = 1;
    private int numeroOcupantes;
    private LocalDateTime dataHoraAbertura;
    private List<Produto> produtosFiltrados = new ArrayList<>();
    private List<Categoria> categorias = new ArrayList<>();
    
    @Autowired
    private MesaService mesaService;

    @Autowired
    private PedidoService pedidoService;

    @Inject
    private ProdutoService produtoService;

    @Inject
    private CategoriaService categoriaService;

    private Mesa mesaSelecionada;


    public void onCategoriaChange() {
        if (categoriaSelecionadaId != null) {
            produtosFiltrados = produtoService.findProdutosByCategoria(categoriaSelecionadaId);
        } else {
            produtosFiltrados = new ArrayList<>();
        }
    }

    public String sairDaComanda() {
        return "mesa.xhtml?faces-redirect=true";
    }

    public void imprimirPreparo() {
        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Relatório enviado para a cozinha."));
    }

    public String voltarMesas() {
        return "mesa.xhtml?faces-redirect=true";
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

    public void cancelarMesa(){
        pedido.setStatus(Pedido.Status.CANCELADO);
        pedidoService.save(pedido);
        mesa.setStatus(Mesa.Status.LIVRE);
        mesaService.save(mesa);
        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Mesa cancelada com sucesso!"));
    }

    public void addProdutoToPedido(Long produtoId) {
        Produto produtoSelecionado = produtoService.findById(produtoId);
        if (produtoSelecionado != null) {
            ItemPedido itemPedido = new ItemPedido();
            itemPedido.setProduto(produtoSelecionado);
            itemPedido.setQuantidade(quantidade);
    
            BigDecimal preco = produtoSelecionado.getPreco();
            BigDecimal subtotal = preco.multiply(BigDecimal.valueOf(quantidade));
    
            itemPedido.setSubtotal(subtotal);
            pedido.getItens().add(itemPedido);  // Adiciona à lista de itens
            pedido.calcularTotal();
            pedidoService.save(pedido);
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Sucesso", "Produto adicionado ao pedido!"));
        } else {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Erro", "Produto não encontrado!"));
        }
    }
    

    public void selecionarProduto(Long produtoId) {
        this.produtoSelecionadoId = produtoId;
        this.quantidade = 1;
    }

    public void abrirComandaDialog(Mesa mesa) {
        this.mesaSelecionada = mesa;

        // Verificar se a mesa está ocupada e carregar o pedido existente se houver
        if (mesa.getStatus() == Mesa.Status.OCUPADO) {
            List<Pedido> pedidosExistentes = pedidoService.findByMesaAndStatus(mesa, Pedido.Status.ABERTO);
            if (!pedidosExistentes.isEmpty()) {
                this.pedido = pedidosExistentes.get(0);  // Assumindo que deseja pegar o primeiro pedido encontrado
                try {
                    FacesContext.getCurrentInstance().getExternalContext().redirect("comanda.xhtml");
                } catch (IOException e) {
                    e.printStackTrace();
                }
                return;
            }
        }
        this.dataHoraAbertura = LocalDateTime.now();
        this.numeroOcupantes = mesa.getNumeroOcupantes();
        PrimeFaces.current().executeScript("PF('dlgAbrirComanda').show()");
    }

    public void confirmarAberturaComanda() {
        Pedido pedido = new Pedido(mesaSelecionada, dataHoraAbertura, Pedido.Status.ABERTO); 
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

    public List<Mesa> getMesas() {
        return mesaService.findAll();
    }

    public List<Mesa.Status> getStatuses() {
        return Arrays.asList(Mesa.Status.values());
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
        return mesa.getStatus().name();
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

    public Long getCategoriaSelecionadaId() {
        return categoriaSelecionadaId;
    }

    public void setCategoriaSelecionadaId(Long categoriaSelecionadaId) {
        this.categoriaSelecionadaId = categoriaSelecionadaId;
    }

    public List<Produto> getProdutosFiltrados() {
        return produtosFiltrados;
    }

    public void setProdutosFiltrados(List<Produto> produtosFiltrados) {
        this.produtosFiltrados = produtosFiltrados;
    }

    public List<Categoria> getCategorias() {
        if (categorias.isEmpty()) {
            categorias = categoriaService.findAll();
        }
        return categorias;
    }

    public void setCategorias(List<Categoria> categorias) {
        this.categorias = categorias;
    }
}
