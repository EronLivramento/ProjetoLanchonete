package lanchonete.entity;

/**
 *
 * @author Livramento
 */
public enum TypeProduct {
    
    DRINK("bebidas"),FOOD("alimentos");
    private final String description;

    TypeProduct(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }

}
