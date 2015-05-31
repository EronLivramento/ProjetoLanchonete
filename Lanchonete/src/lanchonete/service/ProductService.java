
package lanchonete.service;

import java.util.List;
import lanchonete.dao.ProductDao;
import lanchonete.entity.Product;
import lanchonete.exceptions.PersistenceException;
import lanchonete.exceptions.ServiceException;

public class ProductService {
    
    private final ProductDao dao = new ProductDao();
    
    public void save(Product product) throws ServiceException{
        if(isValidateName(product.getName())|| isValidatePrice(product.getPrice()) || isValidateStock(product.getStock())){
            try {
                dao.save(product);
            } catch (PersistenceException ex) {
                throw new ServiceException(ex.getMessage());
            }
        }else{
            throw new ServiceException("Preencha todos os dados necessarios!");
        }
    }
    public void remove(Integer id) throws ServiceException{
        try {
            dao.remove(id);
        } catch (PersistenceException ex) {
            throw new ServiceException(ex.getMessage());
        }
    }
    
    
    public List<Product> findAll() throws ServiceException{
        List<Product> list = null;
        try {
            list= dao.findAll();
        } catch (PersistenceException ex) {
            throw new ServiceException(ex.getMessage());
        }
        return list;
    }
    
    public boolean isValidateName(String name){
        return name != null && name.trim().length() > 0;
    }
    public boolean isValidatePrice(Double price){
        return price != null && price > 0;
    }
    public boolean isValidateStock(Integer stock){
        return stock != null && stock > 0;
    }
}
