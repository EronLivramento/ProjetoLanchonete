/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lanchonete.service;

import java.util.List;
import lanchonete.dao.RequestDao;
import lanchonete.entity.Request;
import lanchonete.exceptions.PersistenceException;
import lanchonete.exceptions.ServiceException;

/**
 *
 * @author Livramento
 */
public class RequestService {
    
    private final RequestDao dao = new RequestDao();
    
    public List<Request> findAll() throws ServiceException{
        List<Request> list = null;
        try {
            list= dao.findAll();
        } catch (PersistenceException ex) {
            throw new ServiceException(ex.getMessage());
        }
        return list;
    }
    public void save(Request request) throws ServiceException{
        try {
            dao.save(request);
        } catch (PersistenceException ex) {
            throw new ServiceException(ex.getMessage());
        }
    }
    public void remove(Request request) throws ServiceException{
        try{
            dao.remove(request);
        }catch(PersistenceException e){
            throw new ServiceException(e.getMessage());
        }
    }
}
