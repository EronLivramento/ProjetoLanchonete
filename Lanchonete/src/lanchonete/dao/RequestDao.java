/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lanchonete.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;
import lanchonete.entity.Request;
import lanchonete.exceptions.PersistenceException;
import lanchonete.util.JDBCConection;

/**
 *
 * @author Livramento
 */
public class RequestDao extends GenericDao{
    
    private static final String INSERT = "INSERT INTO REQUEST(ID_SELL,DELIVERY_FEE)VALUES(?,?)";
    private static final String FIND_ALL = "SELECT R.*,S.*, C.*,A.*, V.NAME AS NAME_VENDER " +
                                "FROM SELL S, CLIENT C,ADRESS A, REQUEST R, USER V " +
                                "WHERE R.ID_SELL = S.ID_SELL\n" +
                                "AND S.ID_CLIENT = C.ID\n" +
                                "AND C.ID_ADRESS = A.ID \n" +
                                "GROUP BY  S.id_sell";
    private static final String REMOVE = "DELETE FROM REQUEST WHERE ID=?";
    private final SellDao sellDao = new SellDao();
    
    public void save(Request request) throws PersistenceException{
        Connection conn = JDBCConection.getIntance().getConnection();
        PreparedStatement pstm = null;
        try{
            pstm = conn.prepareStatement(INSERT);
            sellDao.save(request.getSell());
            executeCommand(pstm, request.getSell().getId(),request.getDeliveryFee());
        }catch(SQLException e){
            e.printStackTrace();
            throw new PersistenceException("Erro ao salvar este pedido");
        }finally{
            JDBCConection.close(conn, pstm);
        }
    }
    public void remove(Request request) throws PersistenceException{
        Connection conn = JDBCConection.getIntance().getConnection();
        PreparedStatement pstm = null;
        try{
            pstm = conn.prepareStatement(REMOVE);
            sellDao.removeSell(request.getSell().getId());
            executeCommand(pstm, request.getId());
        }catch(SQLException e){
            throw new PersistenceException("Erro ao remover este pedido");
        }finally{
            JDBCConection.close(conn, pstm);
        }
    }
    public List<Request> findAll()throws PersistenceException{
        List<Request> request = new LinkedList<>();
        Connection conn = JDBCConection.getIntance().getConnection();
        PreparedStatement pstm = null;
        ResultSet rs = null;
        try{
            pstm = conn.prepareStatement(FIND_ALL);
            rs = executeQuery(pstm);
            while(rs.next()){
                request.add(populateRequestInfo(rs));
            }
        }catch(SQLException e){
            e.printStackTrace();
            throw new PersistenceException("Erro ao procurar os pedidos!");
        }finally{
            JDBCConection.close(conn, pstm, rs);
        }
        return request;
    }
    
    private Request populateRequestInfo(ResultSet rs) throws SQLException, PersistenceException{
        Request toReturn = new Request();
        toReturn.setId(rs.getInt("ID"));
        toReturn.setDeliveryFee(rs.getDouble("DELIVERY_FEE"));
        toReturn.setSell(SellDao.populateSellInfo(rs));
        return toReturn;
    }
}
