package lanchonete.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;
import lanchonete.entity.Client;
import lanchonete.exceptions.PersistenceException;
import lanchonete.util.JDBCConection;

public class ClientDao extends GenericDao{
    
    private static final String INSERT = "INSERT INTO CLIENT(NAME,SECOND_NAME,TELEPHONE,ID_ADRESS)VALUES(?,?,?,?)";
    private static final String UPDATE = "UPDATE CLIENT SET NAME = ?,SECOND_NAME = ?, TELEPHONE = ?,ID_ADRESS = ? WHERE ID= ?";
    private static final String REMOVE = "DELETE FROM CLIENT WHERE ID = ? AND ID_ADRESS = ?";
    private static final String FIND_ALL = "SELECT * FROM CLIENT";
    private static final String FIND_BY_TELEPHONE = "SELECT * FROM CLIENT WHERE TELEPHONE = ?";
    private static final String FIND_BY_NAME = "SELECT * FROM CLIENT WHERE NAME = ?";
    private static final String FIND_BY_ID = "SELECT * FROM CLIENT WHERE ID = ?";
    private static final AdressDao adress = new AdressDao();
    
    
   public void save(Client client) throws PersistenceException{
       if(client.getId() == null){
           insert(client);
       }else{
           update(client);
       }
   }

    private void insert(Client client) throws PersistenceException {
        Connection conn = null;
        PreparedStatement pstm = null;
        try{
            conn = JDBCConection.getIntance().getConnection();
            pstm = conn.prepareStatement(INSERT);
            int idAdress = adress.insert(client.getAdress());
            executeCommand(pstm, client.getName(),client.getSecondName(),
                    client.getTelephone(),idAdress);
        }catch(SQLException e){
            throw new PersistenceException("Erro ao salvar cliente");
        }finally{
            JDBCConection.close(conn, pstm);
        }
    }

    private void update(Client client) throws PersistenceException {
        Connection conn = null;
        PreparedStatement pstm = null;
        try{
            conn = JDBCConection.getIntance().getConnection();
            pstm = conn.prepareStatement(UPDATE);
            adress.update(client.getAdress());
            executeCommand(pstm, client.getName(),client.getSecondName(),
                    client.getTelephone(),client.getAdress().getId(),client.getId());
        }catch(SQLException e){
            throw new PersistenceException("Erro ao atualizar cliente");
        }finally{
            JDBCConection.close(conn, pstm);
        }        
    }
    public void remove(Client client) throws PersistenceException{
        Connection conn = null;
        PreparedStatement pstm = null;
        try{
            conn = JDBCConection.getIntance().getConnection();
            pstm = conn.prepareStatement(REMOVE);
            adress.remove(client.getAdress().getId());
            executeCommand(pstm, client.getId(),client.getAdress().getId());
        }catch(SQLException e){
            throw new PersistenceException("Erro ao deletar cliente");
        }finally{
            JDBCConection.close(conn, pstm);
        }
    }
    public List<Client> findAll() throws PersistenceException{
        List<Client> list = new LinkedList<>();
        Connection conn = null;
        PreparedStatement pstm = null;
        ResultSet rs = null;
        try{
            conn = JDBCConection.getIntance().getConnection();
            pstm = conn.prepareStatement(FIND_ALL);
            rs = executeQuery(pstm);
            while(rs.next()){
                list.add(populateClientInfo(rs));
            }
        }catch(SQLException e){
            throw new PersistenceException("Erro ao buscar clientes");
        }finally{
            JDBCConection.close(conn, pstm,rs);
        }
        return list;
    }
    public List<Client> findByName(String name) throws PersistenceException{
        List<Client> list = new LinkedList<>();
        Connection conn = null;
        PreparedStatement pstm = null;
        ResultSet rs = null;
        try{
            conn = JDBCConection.getIntance().getConnection();
            pstm = conn.prepareStatement(FIND_BY_NAME);
            rs = executeQuery(pstm,name);
            while(rs.next()){
                list.add(populateClientInfo(rs));
            }
        }catch(SQLException e){
            throw new PersistenceException("Erro ao buscar clientes");
        }finally{
            JDBCConection.close(conn, pstm,rs);
        }
        return list;
    }
    public Client findByTelephone(String telephone) throws PersistenceException{
        Client client = null;
        Connection conn = null;
        PreparedStatement pstm = null;
        ResultSet rs = null;
        try{
            conn = JDBCConection.getIntance().getConnection();
            pstm = conn.prepareStatement(FIND_BY_TELEPHONE);
            rs = executeQuery(pstm,telephone);
            if(rs.next()){
                client = (populateClientInfo(rs));
            }
        }catch(SQLException e){
            throw new PersistenceException("Erro ao buscar cliente");
        }finally{
            JDBCConection.close(conn, pstm,rs);
        }
        return client;
    }
    public Client findById(Integer id) throws PersistenceException{
        Client client = null;
        Connection conn = null;
        PreparedStatement pstm = null;
        ResultSet rs = null;
        try{
            conn = JDBCConection.getIntance().getConnection();
            pstm = conn.prepareStatement(FIND_BY_ID);
            rs = executeQuery(pstm,id);
            if(rs.next()){
                client = (populateClientInfo(rs));
            }
        }catch(SQLException e){
            throw new PersistenceException("Erro ao buscar cliente");
        }finally{
            JDBCConection.close(conn, pstm,rs);
        }
        return client;
    }
    public static Client populateClientInfo(ResultSet rs) throws SQLException, PersistenceException{
        Client toReturn = new Client();
        toReturn.setId(rs.getInt("ID"));
        toReturn.setName(rs.getString("NAME"));
        toReturn.setSecondName(rs.getString("SECOND_NAME"));
        toReturn.setTelephone(rs.getString("TELEPHONE"));
        toReturn.setAdress(adress.findById(rs.getInt("ID_ADRESS")));
        return toReturn;
    }
}
