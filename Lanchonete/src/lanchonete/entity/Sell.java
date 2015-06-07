package lanchonete.entity;

import java.sql.Date;
import java.util.List;
import java.util.Objects;
import lanchonete.util.SwingColumn;

public class Sell {

    @SwingColumn(description = "id")
    private Integer id;
    @SwingColumn(description = "Vendedor")
    private User vender;
    @SwingColumn(description = "Data da Venda")
    private Date dateOfSale;
    @SwingColumn(description = "Total")
    private Double total;
    private List<SellItem> itens;
    private Double deliveryFee;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public User getVender() {
        return vender;
    }

    public void setVender(User vender) {
        this.vender = vender;
    }

    public Double getTotal() {
        return total;
    }

    public void setTotal(Double total) {
        this.total = total;
    }

    public Date getDateOfSale() {
        return dateOfSale;
    }

    public void setDateOfSale(Date dateOfSale) {
        this.dateOfSale = dateOfSale;
    }

    public List<SellItem> getItens() {
        return itens;
    }

    public void setItens(List<SellItem> itens) {
        this.itens = itens;
    }

    public Double getDeliveryFee() {
        return deliveryFee;
    }

    public void setDeliveryFee(Double deliveryFee) {
        this.deliveryFee = deliveryFee;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 97 * hash + Objects.hashCode(this.id);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Sell other = (Sell) obj;
        if (!Objects.equals(this.id, other.id)) {
            return false;
        }
        return true;
    }

}
