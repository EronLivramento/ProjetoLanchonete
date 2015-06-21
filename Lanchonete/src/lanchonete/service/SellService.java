/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lanchonete.service;

import java.util.List;
import lanchonete.dao.SellDao;
import lanchonete.entity.Sell;
import lanchonete.entity.SellItem;
import lanchonete.exceptions.PersistenceException;
import lanchonete.exceptions.ServiceException;

/**
 *
 * @author Livramento
 */
public class SellService {
    
    private final SellDao dao = new SellDao();
    
    public List<Sell> findAll() throws ServiceException{
        List<Sell> list = null;
        try {
            list= dao.findAll();
        } catch (PersistenceException ex) {
            throw new ServiceException(ex.getMessage());
        }
        return list;
    }
    public void remove(Integer id) throws ServiceException{
        try {
            dao.removeSell(id);
        } catch (PersistenceException ex) {
            throw new ServiceException(ex.getMessage());
        }
    }
    public void save(Sell sell) throws ServiceException{
        try {
            dao.save(sell);
        } catch (PersistenceException ex) {
            throw new ServiceException(ex.getMessage());
        }
    }
     public static void addItem(List<SellItem> items,SellItem item) {
        if (items.contains(item)) {
            int index = items.indexOf(item);
            SellItem temp = (SellItem) items.get(index);
            int qnt = temp.getQnt();
            temp.setQnt(qnt + item.getQnt());
        } else {
            items.add(item);
        }
    }
}
