package lanchonete.entity;

import java.util.Objects;
import lanchonete.util.SwingColumn;

public class Client {
    
    @SwingColumn(description = "id",colorOfBackgound = "")
    private Integer id;
    @SwingColumn(description = "Nome",colorOfBackgound = "")
    private String name;
    @SwingColumn(description = "Sobrenome",colorOfBackgound = "")
    private String secondName;
    @SwingColumn(description = "Telefone",colorOfBackgound = "")
    private String telephone;
    @SwingColumn(description = "Endereço",colorOfBackgound = "")
    private Adress adress;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTelephone() {
        return telephone;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    public Adress getAdress() {
        return adress;
    }

    public void setAdress(Adress adress) {
        this.adress = adress;
    }

    public String getSecondName() {
        return secondName;
    }

    public void setSecondName(String segundName) {
        this.secondName = segundName;
    }
    

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 13 * hash + Objects.hashCode(this.id);
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
        final Client other = (Client) obj;
        if (!Objects.equals(this.id, other.id)) {
            return false;
        }
        return true;
    }
    
}
