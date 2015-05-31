package lanchonete.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;
import lanchonete.entity.User;
import lanchonete.exceptions.PersistenceException;
import lanchonete.util.JDBCConection;

public class UserDao extends GenericDao {

    private static final String SAVE = "INSERT INTO USER(NAME,LOGIN,PASSWORD)VALUES(?,?,?)";
    private static final String UPDATE = "UPDATE USER SET NAME=?,LOGIN=?,PASSWORD=? WHERE ID=?";
    private static final String REMOVE = "DELETE FROM USER WHERE ID=?";
    private static final String FIND_ALL = "SELECT * FROM USER";
    private static final String FIND_BY_ID = "SELECT * FROM USER WHERE ID=?";
    private static final String FIND_BY_LOGIN = "SELECT * FROM USER WHERE LOGIN = ?";
    private static final String CHECK_USER = "SELECT * FROM USER WHERE LOGIN = ? AND PASSWORD=?";

    public boolean checkUser(User user) throws PersistenceException {
        Connection conn = null;
        PreparedStatement pstm = null;
        ResultSet rs = null;
        boolean result = false;
        try {
            conn = JDBCConection.getIntance().getConnection();
            pstm = conn.prepareStatement(CHECK_USER);
            rs = executeQuery(pstm, user.getLogin(), user.getPassword());
            result = rs.next();
        } catch (SQLException ex) {
            throw new PersistenceException("Login ou senha incorreto!");
        } finally {
            JDBCConection.close(conn, pstm, rs);
        }
        return result;
    }

    public void save(User user) throws PersistenceException {
        if (user.getId() == null) {
            insert(user);
        } else {
            update(user);
        }
    }

    private void insert(User user) throws PersistenceException {
        Connection conn = null;
        PreparedStatement pstm = null;
        try {
            conn = JDBCConection.getIntance().getConnection();
            pstm = conn.prepareStatement(SAVE);
            executeCommand(pstm, user.getName(), user.getLogin(), user.getPassword());
        } catch (SQLException e) {
            throw new PersistenceException("Não foi possivel salvar esse usuario");
        } finally {
            JDBCConection.close(conn, pstm);
        }
    }

    private void update(User user) throws PersistenceException {
        Connection conn = null;
        PreparedStatement pstm = null;
        try {
            conn = JDBCConection.getIntance().getConnection();
            pstm = conn.prepareStatement(UPDATE);
            executeCommand(pstm, user.getName(), user.getLogin(), user.getPassword(), user.getId());
        } catch (SQLException ex) {
            throw new PersistenceException("Não foi possivel alterar esse usuario!");
        } finally {
            JDBCConection.close(conn, pstm);
        }
    }

    public void remove(Integer id) throws PersistenceException {
        Connection conn = null;
        PreparedStatement pstm = null;
        try {
            conn = JDBCConection.getIntance().getConnection();
            pstm = conn.prepareStatement(REMOVE);
            executeCommand(pstm, id);
        } catch (SQLException ex) {
            throw new PersistenceException("não foi possivel deletar esse usuario!");
        } finally {
            JDBCConection.close(conn, pstm);
        }
    }

    public List<User> findAll() throws PersistenceException {
        User user = null;
        List<User> list = new LinkedList<>();
        Connection conn = null;
        PreparedStatement pstm = null;
        ResultSet rs = null;
        try {
            conn = JDBCConection.getIntance().getConnection();
            pstm = conn.prepareStatement(FIND_ALL);
            rs = executeQuery(pstm);
            while(rs.next()){
                list.add(populateUserInfo(rs));
            }
        } catch (SQLException ex) {
            throw new PersistenceException("Erro ao recuperar usuarios!");
        }finally{
            JDBCConection.close(conn, pstm, rs);
        }
        return list;
    }

    public User findbyid(Integer id) throws PersistenceException {
        User user = null;
        Connection conn = null;
        PreparedStatement pstm = null;
        ResultSet rs = null;
        try {
            conn = JDBCConection.getIntance().getConnection();
            pstm = conn.prepareStatement(FIND_BY_ID);
            rs = executeQuery(pstm, id);
            if (rs.next()) {
                user = populateUserInfo(rs);
            }
        } catch (SQLException ex) {
            throw new PersistenceException("ID não encontrado!");
        }finally{
            JDBCConection.close(conn, pstm, rs);
        }
        return user;
    }

    public User findByLogin(String login) throws PersistenceException {
        User user = null;
        Connection conn = null;
        PreparedStatement pstm = null;
        ResultSet rs = null;
        try {
            conn = JDBCConection.getIntance().getConnection();
            pstm = conn.prepareStatement(FIND_BY_LOGIN);
            rs = executeQuery(pstm, login);
            if (rs.next()) {
                user = populateUserInfo(rs);
            }
        } catch (SQLException ex) {
            throw new PersistenceException("Login não encontrado!");
        } finally {
            JDBCConection.close(conn, pstm, rs);
        }
        return user;
    }

    public static User populateUserInfo(ResultSet rs) throws SQLException {
        User toReturn = new User();
        toReturn.setId(rs.getInt("ID"));
        toReturn.setName(rs.getString("NAME"));
        toReturn.setLogin(rs.getString("LOGIN"));
        toReturn.setPassword(rs.getString("PASSWORD"));
        return toReturn;
    }

}