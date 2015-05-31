package lanchonete.service;

import java.util.List;

import lanchonete.dao.UserDao;
import lanchonete.entity.User;
import lanchonete.exceptions.PersistenceException;
import lanchonete.exceptions.ServiceException;

public class UserService {

    public boolean login(User user) throws ServiceException {
        boolean result = false;

        try {
            if (user.getLogin() == null || user.getLogin().trim().equals("")) {
                throw new ServiceException("Login obrigatório!");
            } else if (user.getPassword() == null || user.getPassword().trim().equals("")) {
                throw new ServiceException("Senha obrigatória");
            } else {
                UserDao userDao = new UserDao();
                result = userDao.checkUser(user);
            }
        } catch (Exception e) {
            throw new ServiceException(e.getMessage());
        }
        return result;
    }

    public void save(User user) throws ServiceException {
        if (user.getLogin() == null || user.getLogin().trim().equals("")) {
            throw new ServiceException("Login obrigatório!");
        } else if (user.getPassword() == null || user.getPassword().trim().equals("")) {
            throw new ServiceException("Senha obrigatória");
        } else if (user.getName() == null || user.getName().trim().equals("")) {
            throw new ServiceException("Nome obrigatório!");
        } else {
            UserDao userDao = new UserDao();
            try {
                userDao.save(user);
            } catch (PersistenceException e) {
                throw new ServiceException(e.getMessage());
            }
        }
    }

    public void removeUser(Integer id) throws ServiceException {
        UserDao userDao = new UserDao();
        try {
            userDao.remove(id);
        } catch (PersistenceException e) {
            throw new ServiceException(e.getMessage());
        }
    }

    public User findByLogin(String login) throws ServiceException {
        UserDao userDao = new UserDao();
        User user = null;
        try {
            user = userDao.findByLogin(login);
        } catch (PersistenceException e) {
            throw new ServiceException(e.getMessage());
        }
        return user;
    }

    public List<User> findAllUser() throws ServiceException {
        List<User> toResult;
        try {
            UserDao userDao = new UserDao();
            toResult = userDao.findAll();
        } catch (PersistenceException e) {
            throw new ServiceException(e.getMessage());
        }
        return toResult;
    }

}
