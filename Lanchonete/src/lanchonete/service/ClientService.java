package lanchonete.service;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import lanchonete.dao.ClientDao;
import lanchonete.entity.Adress;
import lanchonete.entity.Client;
import lanchonete.exceptions.PersistenceException;
import lanchonete.exceptions.ServiceException;

public class ClientService {

    private final ClientDao dao = new ClientDao();

    public void save(Client client) throws ServiceException {
        if (isValidateName(client.getName()) || isValidateTelephone(client.getTelephone()) || isValidateAdress(client.getAdress())) {
            try {
                dao.save(client);
            } catch (PersistenceException e) {
                throw new ServiceException(e.getMessage());
            }
        } else {
            throw new ServiceException("Preencha todos os campos!");
        }
    }

    public List<Client> findAll() throws ServiceException {
        List<Client> toReturn = null;
        try {
            toReturn = dao.findAll();
        } catch (PersistenceException ex) {
            throw new ServiceException(ex.getMessage());
        }
        return toReturn;
    }
    public void remove(Client client) throws ServiceException{
        try {
            dao.remove(client);
        } catch (PersistenceException ex) {
            throw new ServiceException(ex.getMessage());
        }
    }

    private boolean isValidateName(String name) {
        return name != null && name.trim().length() > 0;
    }

    private boolean isValidateTelephone(String telephone) {
        return telephone != null && telephone.trim().length() >= 8;
    }

    private boolean isValidateAdress(Adress adress) {
        boolean toReturn = false;
        if (adress.getStreet() != null && adress.getStreet().trim().length() > 0) {
            toReturn = true;
        } else if (adress.getNumber() != null) {
            toReturn = true;
        } else if (adress.getCity() != null && adress.getCity().trim().length() > 0) {
            toReturn = true;
        }
        return toReturn;
    }

    public Client findByTelephone(String telephone) throws ServiceException {
        Client toReturn = null;
        try {
            toReturn = dao.findByTelephone(telephone);
        } catch (PersistenceException ex) {
            throw new ServiceException(ex.getMessage());
        }
        return toReturn;
    }
}
