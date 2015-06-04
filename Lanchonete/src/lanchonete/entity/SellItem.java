package lanchonete.entity;

import java.util.Objects;
import lanchonete.util.SwingColumn;

public class SellItem {
    
    private Integer id;
    @SwingColumn(description="Produto")
    private Product product;
    private Sell sell;
    @SwingColumn(description="Quantidade")
    private Integer qnt;
    @SwingColumn(description="Total")
    private Double totalOfItem;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public Sell getSell() {
        return sell;
    }

    public void setSell(Sell sell) {
        this.sell = sell;
    }

    public Integer getQnt() {
        return qnt;
    }

    public void setQnt(Integer qnt) {
        this.qnt = qnt;
    }

    public Double getTotalOfItem() {
        return totalOfItem;
    }

    public void setTotalOfItem(Double totalOfItem) {
        this.totalOfItem = totalOfItem;
    }
    

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 61 * hash + Objects.hashCode(this.id);
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
        final SellItem other = (SellItem) obj;
        if (!Objects.equals(this.id, other.id)) {
            return false;
        }
        return true;
    }
    
    
}
